package com.sahk.earlyliteracy.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.database.ItemGame;
import com.sahk.earlyliteracy.database.ItemStage;
import com.sahk.earlyliteracy.database.ItemTheme;
import com.sahk.earlyliteracy.database.ItemVideo;
import com.sahk.earlyliteracy.utils.CommonUtil;
import com.sahk.earlyliteracy.utils.FileUtil;

import static com.sahk.earlyliteracy.database.TableGame.COLUMN_BASIC;
import static com.sahk.earlyliteracy.database.TableGame.COLUMN_DOWNLOADPATH;
import static com.sahk.earlyliteracy.database.TableGame.COLUMN_GAMEID;
import static com.sahk.earlyliteracy.database.TableGame.COLUMN_GAMEPATH;
import static com.sahk.earlyliteracy.database.TableGame.COLUMN_GAMETYPE;
import static com.sahk.earlyliteracy.database.TableGame.COLUMN_GAMEVERSION;
import static com.sahk.earlyliteracy.database.TableStage.COLUMN_FULLMARK;
import static com.sahk.earlyliteracy.database.TableStage.COLUMN_PASSINGMARK;
import static com.sahk.earlyliteracy.database.TableStage.COLUMN_STAGEID;
import static com.sahk.earlyliteracy.database.TableTheme.COLUMN_THEMEID;
import static com.sahk.earlyliteracy.database.TableVideo.COLUMN_JQANDAVIDEO;
import static com.sahk.earlyliteracy.database.TableVideo.COLUMN_SONGVIDEO;
import static com.sahk.earlyliteracy.database.TableVideo.COLUMN_SQANDAVIDEO;
import static com.sahk.earlyliteracy.database.TableVideo.COLUMN_THEMEVIDEO;
import static com.sahk.earlyliteracy.database.TableVideo.COLUMN_VIDEOVERSION;

public class SplashFragment extends BaseFragment {

    private ImageButton imageButtonStart;
    private RelativeLayout relativeLayoutDownload;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_splash, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
        setView(view);
        setListener();
        checkNetwork();
        return view;
    }

    private void setView(View view) {
        imageButtonStart = (ImageButton) view.findViewById(R.id.imageButtonStart);
        relativeLayoutDownload = (RelativeLayout) view.findViewById(R.id.relativeLayoutDownload);
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        imageButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(DisclaimerFragment.newInstance());
            }
        });
    }

    private void checkNetwork() {
        if (CommonUtil.checkNetworkConnected(activity)) {
            fetchGameData();
        }
    }

    private void fetchGameData() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setMaxRetriesAndTimeout(2, 3000);

        //Added By Rex
        asyncHttpClient.get(Config.SERVER_BASE_URL + Config.ADV_JSON , new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("fetchAdvData failure", String.format("[%d] %s", statusCode, responseString));
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.attention)
                        .setMessage(R.string.server_connection_fail)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activity.finish();
                            }
                        })
                        .setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fetchGameData();
                            }
                        })
                        .create()
                        .show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("fetchAdvData success", jsonArray.toString());
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectAdv = jsonArray.getJSONObject(i);
                        String show = jsonObjectAdv.getString("show");
                        String img = jsonObjectAdv.getString("img");
                        String link = jsonObjectAdv.getString("link");

                        if (show.equalsIgnoreCase("Y"))
                        {
                            Config.AD_SHOW = "Y";
                            Config.AD_IMG = img;
                            Config.AD_URL = link;
                            break;
                        }
                    }
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });


        /////asyncHttpClient.get(Config.SERVER_BASE_URL + "/json/game_n.json", new JsonHttpResponseHandler() {
        //asyncHttpClient.get(Config.SERVER_BASE_URL + "/json/game_n2.json", new JsonHttpResponseHandler() {
//        asyncHttpClient.get(Config.SERVER_BASE_URL + "/json/game_n_rex.json", new JsonHttpResponseHandler() {
        asyncHttpClient.get(Config.SERVER_BASE_URL + Config.GAME_JSON , new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                imageButtonStart.setVisibility(View.GONE);
                relativeLayoutDownload.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("fetchGameData failure", String.format("[%d] %s", statusCode, responseString));
                new AlertDialog.Builder(activity)
                        .setCancelable(false)   //Added By Rex
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.attention)
                        .setMessage(R.string.server_connection_fail)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activity.finish();
                            }
                        })
                        .setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fetchGameData();
                            }
                        })
                        .create()
                        .show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("fetchGameData success", jsonArray.toString());
                try {
                    List<ItemGame> localGames = tableGame.getAll();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectGame = jsonArray.getJSONObject(i);
                        ItemGame itemGame = new ItemGame();
                        itemGame.setGameID(jsonObjectGame.getInt(COLUMN_GAMEID));
                        itemGame.setGameType(jsonObjectGame.getString(COLUMN_GAMETYPE));
                        itemGame.setGameVersion(jsonObjectGame.getInt(COLUMN_GAMEVERSION));
                        itemGame.setDownloadPath(jsonObjectGame.getString(COLUMN_DOWNLOADPATH));
                        itemGame.setGamePath(jsonObjectGame.getString(COLUMN_GAMEPATH));
                        itemGame.setBasic(jsonObjectGame.getInt(COLUMN_BASIC));
                        itemGame.setOrder(jsonObjectGame.getInt("order"));
//
//                        //hardcode to test, to remove
//                        if(//itemGame.getGameID() == 1 ||
//                                itemGame.getGameID() == 2 ||
//                                itemGame.getGameID() == 3
////                                itemGame.getGameID() == 4 ||
////                                itemGame.getGameID() == 9 ||
////                                itemGame.getGameID() == 10 ||
////                                itemGame.getGameID() == 11 ||
////                                itemGame.getGameID() == 18 ||
//                                //itemGame.getGameID() == 19
//                                ){
//                            itemGame.setBasic(1);
//                        }else{
//                            itemGame.setBasic(0);
//                        }

                        if (tableGame.isGameDataDownloaded(jsonObjectGame.getInt(COLUMN_GAMEID))) {
                            if (tableGame.get(jsonObjectGame.getInt(COLUMN_GAMEID)).getGameVersion() != jsonObjectGame.getInt(COLUMN_GAMEVERSION)) {
                                itemGame.setHasUpdate(1);
                                tableGame.update(itemGame);
                                removeGameFile(itemGame.getGameID());
                            }
                        } else {
                            itemGame.setHasUpdate(1);
                            tableGame.insert(itemGame);
                        }

                        for(int j = 0; j<localGames.size();j++){
                            if(localGames.get(j).getGameID() == itemGame.getGameID()){
                                localGames.remove(j);
                                break;
                            }
                        }

                        JSONArray jsonArrayTheme = jsonObjectGame.getJSONArray("theme");
                        for (int j = 0; j < jsonArrayTheme.length(); j++) {
                            JSONObject jsonObjectTheme = jsonArrayTheme.getJSONObject(j);
                            ItemTheme itemTheme = new ItemTheme();
                            itemTheme.setThemeID(jsonObjectTheme.getInt("themeID"));
                            itemTheme.setGameID(jsonObjectGame.getInt("gameID"));
                            itemTheme.setGameName(jsonObjectTheme.getString("gameName"));
                            itemTheme.setThemePath(jsonObjectTheme.getString("themePath"));
                            if (tableTheme.isThemeDataDownloaded(jsonObjectTheme.getInt("themeID"), jsonObjectGame.getInt("gameID"))) {
                                itemTheme.setFinished(tableTheme.get(jsonObjectTheme.getInt("themeID"), jsonObjectGame.getInt("gameID")).getFinished());
                                tableTheme.update(itemTheme);
                            } else {
                                itemTheme.setFinished(0);
                                tableTheme.insert(itemTheme);
                            }
                            JSONArray jsonArrayStage = jsonObjectTheme.getJSONArray("stage");
                            for (int k = 0; k < jsonArrayStage.length(); k++) {
                                JSONObject jsonObjectStage = jsonArrayStage.getJSONObject(k);
                                ItemStage itemStage = new ItemStage();
                                itemStage.setStageID(jsonObjectStage.getInt(COLUMN_STAGEID));
                                itemStage.setThemeID(jsonObjectTheme.getInt(COLUMN_THEMEID));
                                itemStage.setGameID(jsonObjectGame.getInt(COLUMN_GAMEID));
                                itemStage.setFullMark(jsonObjectStage.getInt(COLUMN_FULLMARK));
                                itemStage.setPassingMark(jsonObjectStage.getInt(COLUMN_PASSINGMARK));
                                if (tableStage.isStageDataDownloaded(jsonObjectStage.getInt(COLUMN_STAGEID), jsonObjectTheme.getInt(COLUMN_THEMEID), jsonObjectGame.getInt(COLUMN_GAMEID))) {
                                    tableStage.update(itemStage);
                                } else {
                                    tableStage.insert(itemStage);
                                }
                            }
                        }
                    }

                    //remove games if not exist on server
                    for(ItemGame game: localGames){
                        removeGame(game.getGameID());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    fetchVideoData();
                }
            }
        });
    }

    private void removeGame(int gameId){
        removeGameFile(gameId);

        tableGamePlay.deleteByGameId(gameId);
        tableStage.deleteByGameId(gameId);
        tableTheme.deleteThemeByGameId(gameId);
        tableGame.delete(gameId);
    }

    private void removeGameFile(int gameId){
        //delete game.zip
        String filePath = activity.getFilesDir() + File.separator + "game" +gameId + ".zip";
        File file = new File(filePath);
        if(file.exists()) {
            FileUtil.deleteRecursive(file);
        }

        //delete game directory
        String dirPath = activity.getFilesDir() + File.separator + "game" + gameId + "/";
        File dir = new File(dirPath);
        if(dir.exists()) {
            FileUtil.deleteRecursive(dir);
        }
    }

    private void fetchVideoData() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setMaxRetriesAndTimeout(2, 3000);
        asyncHttpClient.get(Config.SERVER_BASE_URL + Config.VIDEO_JSON , new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("fetchVideoData failure", String.format("[%d] %s", statusCode, responseString));
                new AlertDialog.Builder(activity)
                        .setCancelable(false)   //Added By Rex
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.attention)
                        .setMessage(R.string.server_connection_fail)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fetchVideoData();
                            }
                        })
                        .create()
                        .show();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("fetchVideoData success", jsonArray.toString());
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ItemVideo itemVideo = new ItemVideo();
                        itemVideo.setThemeID(jsonObject.getInt(COLUMN_THEMEID));
                        itemVideo.setThemeVideo(jsonObject.getString(COLUMN_THEMEVIDEO));
                        itemVideo.setSongVideo(jsonObject.getString(COLUMN_SONGVIDEO));
                        itemVideo.setJQAndAVideo(jsonObject.getString(COLUMN_JQANDAVIDEO));
                        itemVideo.setSQAndAVideo(jsonObject.getString(COLUMN_SQANDAVIDEO));
                        if(jsonObject.has(COLUMN_VIDEOVERSION)) {
                            itemVideo.setVideoVersion(jsonObject.getInt(COLUMN_VIDEOVERSION));
                        }
                        if (tableVideo.isVideoDataDownloaded() && itemVideo.getVideoVersion()>0) {
                            if (tableVideo.get(jsonObject.getInt(COLUMN_THEMEID)).getVideoVersion() != jsonObject.getInt(COLUMN_VIDEOVERSION)) {
                                itemVideo.setHasUpdate(1);
                                tableVideo.update(itemVideo);
                            }
                        } else {
                            itemVideo.setHasUpdate(1);
                            tableVideo.insert(itemVideo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    imageButtonStart.setVisibility(View.VISIBLE);
                    relativeLayoutDownload.setVisibility(View.GONE);
                }
            }
        });
    }
}