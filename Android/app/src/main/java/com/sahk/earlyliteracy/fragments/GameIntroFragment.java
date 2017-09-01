package com.sahk.earlyliteracy.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.sahk.earlyliteracy.BaseActivity;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.database.ItemGame;
import com.sahk.earlyliteracy.database.TableGamePlay;
import com.sahk.earlyliteracy.utils.CommonUtil;

import static com.sahk.earlyliteracy.applications.Config.NEWCOMER_SCORE;

public class GameIntroFragment extends BaseFragment {

    private ImageButton imageButtonStart;
    private ImageView imageViewIntro;
    private RelativeLayout relativeLayoutBackground;
    private ProgressBar progressBarDownload;
    private LinearLayout downloadLayout;
    private TextView customTextViewDownload;

    private int themeID, gameID;
    private String themeName;
    private int mProgress=0;

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==0){
                Log.d("downloadgame","download progress: " + mProgress);
                progressBarDownload.setProgress(mProgress);
            }else if (msg.what==1){
                customTextViewDownload.setText("正在安裝遊戲...");
                new Decompress().execute(gameID);
                //downloadLayout.setVisibility(View.GONE);
            }
        }
    };


    public static GameIntroFragment newInstance(int themeID, String themeName, int gameID, int level) {
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        bundle.putInt("gameID", gameID);
        bundle.putInt("level", level);
        GameIntroFragment gameIntroFragment = new GameIntroFragment();
        gameIntroFragment.setArguments(bundle);
        return gameIntroFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_gameintro, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_GAMEINTRO);
        themeID = getArguments().getInt("themeID");
        themeName = getArguments().getString("themeName");
        gameID = getArguments().getInt("gameID");
        setView(view);
        setListener();
        setIntro(themeID, gameID);
        if(gameID==9) {
            imageButtonStart.setEnabled(true);
        }else{
            imageButtonStart.setVisibility(View.INVISIBLE);
            //new Decompress().execute(gameID);
            //downloadLayout.setVisibility(View.VISIBLE);
            checkGameZip(gameID);
        }

        //Comment By Rex
//        if(mGaTracker!=null){
//            mGaTracker.setScreenName("theme id:" + themeID + ", game id:" + gameID);
//            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        }
        return view;
    }

    private void setView(View view) {
        imageButtonStart = (ImageButton) view.findViewById(R.id.imageButtonStart);
        imageViewIntro = (ImageView) view.findViewById(R.id.imageViewIntro);
        relativeLayoutBackground = (RelativeLayout) view.findViewById(R.id.relativeLayoutBackground);
        progressBarDownload = (ProgressBar)view.findViewById(R.id.progressBarDownload);
        downloadLayout = (LinearLayout)view.findViewById(R.id.linearLayoutDownload);
        customTextViewDownload = (TextView)view.findViewById(R.id.customTextViewDownload);
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        imageButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkNewcomer();
            }
        });
    }

    private void setIntro(int themeID, int gameID) {
        relativeLayoutBackground.setBackgroundResource(getResources().getIdentifier("into_t" + themeID, "drawable", activity.getPackageName()));
        if (gameID == 16) {
            if (themeID >= 1 && themeID <= 3) {
                imageViewIntro.setImageResource(getResources().getIdentifier("intro" + gameID + "_a", "drawable", activity.getPackageName()));
            } else {
                imageViewIntro.setImageResource(getResources().getIdentifier("intro" + gameID + "_b", "drawable", activity.getPackageName()));
            }
        } else {
            imageViewIntro.setImageResource(getResources().getIdentifier("intro" + gameID, "drawable", activity.getPackageName()));
        }
    }

    @SuppressWarnings("unchecked")
    private void checkNewcomer() {
        //Add By Rex
        if(mGaTracker!=null){
            if (!sharedPreferences.getBoolean("t" + String.valueOf(themeID) + "_g" + String.valueOf(gameID) + "_ga_1a", false)) {
                mGaTracker.setScreenName("Game Screen");
                mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                mGaTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("1_Start_Game")
                        .setAction("Theme ID:" + String.valueOf(themeID) + ",Game ID:" + String.valueOf(gameID))
                        .setLabel("Start Game")
                        .build());
                sharedPreferences.edit().putBoolean("t" + String.valueOf(themeID) + "_g" + String.valueOf(gameID) + "_ga_1a" , true).apply();
            }

            mGaTracker.setScreenName("Game Screen");
            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mGaTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("2_Game_Complete")
                    .setAction( "Theme ID:" + String.valueOf(themeID) + ",Game ID:" + String.valueOf(gameID) + ",Level ID:1" )
                    .setLabel("Start")
                    .build());
        }

        TableGamePlay tableGamePlay = new TableGamePlay(getActivity());
        if (tableGamePlay.isNewComer(themeID, gameID)) {
            if(gameID==9){
                myFragment.getContainer().setFragmentChild(ScoreFragment.newInstance(themeID, themeName, gameID, true, NEWCOMER_SCORE, 1));
            }else {
                myFragment.getContainer().setFragmentChild(ScoreFragment.newInstance(themeID, themeName, gameID, true, NEWCOMER_SCORE, 1));
            }
        } else {
            if(gameID==9){
                //by default, start level 1
                myFragment.getContainer().setFragmentChild(GameNineFragment.newInstance(themeID, themeName, 1));
            }else {
                myFragment.getContainer().setFragmentChild(WebGameFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level")));
            }
        }
    }

    private void checkGameZip(int gameID) {
        File file = new File(activity.getFilesDir() + "/game" + gameID + ".zip");
        if (!file.exists()) {
            if(CommonUtil.checkNetworkConnected(context)) {
                downloadLayout.setVisibility(View.VISIBLE);
                new DownloadGame().execute(gameID);
            }else {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setCancelable(false);   //Added By Rex
                adb.setMessage("首次進入時需下載遊戲或影片經流動網絡下載可能產生額外費用，建議以wifi連線下載。是否要連線？");
                adb.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                    }
                });
                adb.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        } else {
            mhandler.sendEmptyMessage(1);
        }
    }

    private class Decompress extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            imageButtonStart.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Integer... input) {
            InputStream inputStream;
            ZipInputStream zipInputStream;
            try {
                inputStream = new FileInputStream(new File(activity.getFilesDir() + File.separator + "game" + input[0] + ".zip"));
                zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
                ZipEntry zipEntry;
                byte[] buffer = new byte[1024];
                int count;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.isDirectory()) {
                        File file = new File(activity.getFilesDir() + File.separator + "game" + input[0] + File.separator + zipEntry.getName());
                        if (file.mkdirs()) {
                            continue;
                        }
                    } else {
                        FileOutputStream fout = new FileOutputStream(activity.getFilesDir() + File.separator + "game" + input[0] + File.separator + zipEntry.getName());
                        while ((count = zipInputStream.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }
                        fout.close();
                    }
                    zipInputStream.closeEntry();
                }

                zipInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void output) {
            imageButtonStart.setVisibility(View.VISIBLE);
            imageButtonStart.setEnabled(true);
            downloadLayout.setVisibility(View.GONE);
        }
    }


    private class DownloadGame extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... input) {
            try {
                URL url = new URL(tableGame.get(input[0]).getDownloadPath());
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                String filePath = activity.getFilesDir() + File.separator + "game" + input[0] + ".zip";
                FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
                byte data[] = new byte[1024];
                int countSize;
                int fileSize = urlConnection.getContentLength();
                int currentSize = 0;
                while ((countSize = inputStream.read(data)) != -1) {
                    currentSize += countSize;
                    int downloadProgress = (int)((currentSize * 100.0f) / fileSize);
                    Log.d("downloadgame","publish progress: " + downloadProgress);
                    publishProgress(downloadProgress);
                    fileOutputStream.write(data, 0, countSize);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                ItemGame itemGame = new ItemGame();
                itemGame.setGameID(input[0]);
                itemGame.setGameType(tableGame.get(input[0]).getGameType());
                itemGame.setGameVersion(tableGame.get(input[0]).getGameVersion());
                itemGame.setDownloadPath(tableGame.get(input[0]).getDownloadPath());
                itemGame.setGamePath(tableGame.get(input[0]).getGamePath());
                itemGame.setBasic(tableGame.get(input[0]).getBasic());
                itemGame.setOrder(tableGame.get(input[0]).getOrder());
                itemGame.setHasUpdate(0);
                tableGame.update(itemGame);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return input[0];
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //mProgress = progress[0];
            //mhandler.sendEmptyMessage(0);
            Log.d("downloadgame","update progress: " + progress[0]);
            progressBarDownload.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Integer output) {
            mhandler.sendEmptyMessage(1);
        }


    }
}

