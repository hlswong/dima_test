package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.adapters.CoverFlowAdapter;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.constants.GlobalValue;
import com.sahk.earlyliteracy.database.ItemTheme;
import com.sahk.earlyliteracy.widgets.CustomTextView;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class GameListFragment extends BaseFragment {

    private CoverFlowAdapter coverFlowAdapter;
    private CustomTextView customTextViewPage;
    private CustomTextView customTextViewTitle;
    private FeatureCoverFlow featureCoverFlow;
    private ImageButton imageButtonArrowLeft;
    private ImageButton imageButtonArrowRight;
    private int coverFlowPosition;
    private int themeID, currentGame; //Add by Rex
    private int selectedGameId;
    private RelativeLayout relativeLayoutBackground;
    private String themeName;

    private TextSwitcher mTitle;

    //Edit by Rex
    public static GameListFragment newInstance(int themeID, String themeName, int currentGame) {
        Bundle bundle = new Bundle();
        bundle.putString("themeName", themeName);
        bundle.putInt("themeID", themeID);
        bundle.putInt("currentGame", currentGame); //Add by Rex
        GameListFragment gameListFragment = new GameListFragment();
        gameListFragment.setArguments(bundle);
        return gameListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_gamelist, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_MENU);
        themeID = getArguments().getInt("themeID");
        themeName = getArguments().getString("themeName");
        currentGame = getArguments().getInt("currentGame"); //Add by Rex
        setView(view);
        setViewPager();
        setListener();
        setTheme();
        setTitle();

        // Add by Rex
        if ( currentGame != -1 )
        {
            featureCoverFlow.scrollToPosition( tableGame.get(currentGame).getOrder() - 1 );
        }

        return view;
    }

    private void setView(View view) {
        customTextViewTitle = (CustomTextView) view.findViewById(R.id.customTextViewTitle);
        imageButtonArrowLeft = (ImageButton) view.findViewById(R.id.imageButtonArrowLeft);
        imageButtonArrowRight = (ImageButton) view.findViewById(R.id.imageButtonArrowRight);
        relativeLayoutBackground = (RelativeLayout) view.findViewById(R.id.relativeLayoutBackground);
        customTextViewPage = (CustomTextView) view.findViewById(R.id.customTextViewPage);
        featureCoverFlow = (FeatureCoverFlow) view.findViewById(R.id.featureCoverFlow);
        mTitle = (TextSwitcher) view.findViewById(R.id.title);
    }



    @SuppressWarnings("unchecked")
    private void setListener() {
//        customTextViewPage.setContentDescription("測試");

        featureCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ItemTheme item = (ItemTheme)coverFlowAdapter.getItem(position);
                GlobalValue.gLastGameId = item.getGameID();
                //Comment By Rex
//                if (tableGame.get(item.getGameID()).getBasic() != 0 || item.getGameID()==9) {
                //Added By Rex
                if ( ( tableGame.get(item.getGameID()).getBasic() == 1 ) || (tableTheme.isFinishedAllBasicGame(themeID)) ) {
                    if (item.getGameID() == 9) {
                        myFragment.getContainer().setFragmentChild(GameIntroFragment.newInstance(themeID, themeName, 9, 1));
                        //
                    } else if (item.getGameID() == 10) {
                        //checkGameZip(item.getGameID());
                        goToGameIntro(item.getGameID());
                    } else {
                        //checkGameZip(item.getGameID());
                        goToGameIntro(item.getGameID());
                    }
                }
//                }
            }
        });
        featureCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                ItemTheme item = (ItemTheme)coverFlowAdapter.getItem(position);
                customTextViewPage.setText(String.valueOf(position + 1));
            }

            @Override
            public void onScrolling() {
            }
        });
        imageButtonArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (featureCoverFlow.getScrollPosition() == 0) {
                    coverFlowPosition = coverFlowAdapter.getCount() - 1;
                } else {
                    coverFlowPosition--;
                }
                featureCoverFlow.scrollToPosition(coverFlowPosition);
            }
        });
        imageButtonArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (featureCoverFlow.getScrollPosition() == coverFlowAdapter.getCount() - 1) {
                    coverFlowPosition = 0;
                } else {
                    coverFlowPosition++;
                }
                featureCoverFlow.scrollToPosition(coverFlowPosition);
            }
        });
    }

    private void setTheme() {
        relativeLayoutBackground.setBackgroundResource(getResources().getIdentifier("bg_t" + themeID + "_1", "drawable", activity.getPackageName()));
    }

    private void setTitle() {
        customTextViewTitle.setText(themeName);
        customTextViewTitle.setContentDescription(themeName); //Add By Rex
    }

    private void setViewPager() {
//        Animation in = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top);
//        Animation out = AnimationUtils.loadAnimation(activity, R.anim.slide_out_bottom);
//        mTitle.setInAnimation(in);
//        mTitle.setOutAnimation(out);

        coverFlowAdapter = new CoverFlowAdapter(activity, themeID, tableTheme.getThemeGame(themeID));
        featureCoverFlow.setAdapter(coverFlowAdapter);
        featureCoverFlow.scrollToPosition(getGamePosition());
    }

    private int getGamePosition(){
        if(GlobalValue.gLastGameId!=-1){
            return coverFlowAdapter.findItemIndex(GlobalValue.gLastGameId);
        }
        return 0;
    }


    private void goToGameIntro(int gameID){
        Log.d("gamelist","play game with ID : " + gameID);
            myFragment.getContainer().setFragmentChild(GameIntroFragment.newInstance(themeID, themeName, gameID, 1));
    }

//    @SuppressWarnings("unchecked")
//    private void checkGameZip(int gameID) {
//        File file = new File(activity.getFilesDir() + "/game" + gameID + ".zip");
//        if (!file.exists()) {
//            selectedGameId = gameID;
//            if(CommonUtil.checkNetworkConnected(context)) {
//                new DownloadGame().execute(selectedGameId);
//            }else {
//                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
//                adb.setMessage("首次進入時需下載遊戲或影片經流動網絡下載可能產生額外費用，建議以wifi連線下載。是否要連線？");
//                adb.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
//                    }
//                });
//                adb.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                AlertDialog alertDialog = adb.create();
//                alertDialog.show();
//            }
//        } else {
//            Log.d("gamelist","play game with ID : " + gameID);
//            myFragment.getContainer().setFragmentChild(GameIntroFragment.newInstance(themeID, themeName, gameID, 1));
//        }
//    }

//    private class DownloadGame extends AsyncTask<Integer, Integer, Integer> {
//
//        @Override
//        protected Integer doInBackground(Integer... input) {
//            try {
//                URL url = new URL(tableGame.get(input[0]).getDownloadPath());
//                URLConnection urlConnection = url.openConnection();
//                urlConnection.connect();
//                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
//                String filePath = activity.getFilesDir() + File.separator + "game" + input[0] + ".zip";
//                FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
//                byte data[] = new byte[1024];
//                int countSize;
//                int fileSize = urlConnection.getContentLength();
//                int currentSize = 0;
//                while ((countSize = inputStream.read(data)) != -1) {
//                    currentSize += countSize;
//                    publishProgress(input[0], (currentSize * 100) / fileSize);
//                    fileOutputStream.write(data, 0, countSize);
//                }
//                fileOutputStream.flush();
//                fileOutputStream.close();
//                inputStream.close();
//                ItemGame itemGame = new ItemGame();
//                itemGame.setGameID(input[0]);
//                itemGame.setGameType(tableGame.get(input[0]).getGameType());
//                itemGame.setGameVersion(tableGame.get(input[0]).getGameVersion());
//                itemGame.setDownloadPath(tableGame.get(input[0]).getDownloadPath());
//                itemGame.setGamePath(tableGame.get(input[0]).getGamePath());
//                itemGame.setBasic(tableGame.get(input[0]).getBasic());
//                itemGame.setOrder(tableGame.get(input[0]).getOrder());
//                itemGame.setHasUpdate(0);
//                tableGame.update(itemGame);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return input[0];
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            coverFlowAdapter.downloadingGameID = progress[0];
//            coverFlowAdapter.progress = progress[1];
//            coverFlowAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        protected void onPostExecute(Integer output) {
//            coverFlowAdapter.downloadingGameID = 0;
//            coverFlowAdapter.progress = 0;
//            coverFlowAdapter.notifyDataSetChanged();
//            checkGameZip(output);
//        }
//
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        featureCoverFlow.releaseAllMemoryResources();
        featureCoverFlow.clearCache();
        featureCoverFlow = null;
    }
}