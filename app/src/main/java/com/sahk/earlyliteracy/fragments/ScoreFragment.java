package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.database.ItemGame;
import com.sahk.earlyliteracy.database.ItemGamePlay;
import com.sahk.earlyliteracy.database.ItemScore;
import com.sahk.earlyliteracy.database.ItemStage;
import com.sahk.earlyliteracy.database.ItemTheme;
import com.sahk.earlyliteracy.database.TableStage;
import com.sahk.earlyliteracy.widgets.StrokeTextView;

public class ScoreFragment extends BaseFragment {

    private int gameID;
    private int themeID;
    private String themeName;
    private Handler handler = new Handler();
    private RelativeLayout relativeLayoutBackground;
    private StrokeTextView strokeTextViewTitle;
    private TextView textViewScore;

    @SuppressWarnings("unchecked")
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (getArguments().getBoolean("newcomer")) {
                switch (gameID) {
                    case 9:
                        myFragment.getContainer().setFragmentChild(GameNineFragment.newInstance(themeID, themeName, 1));
                        break;
//                    case 10:
//                        myFragment.getContainer().setFragmentChild(GameTenFragment.newInstance(getArguments().getInt("theme")));
//                        break;
                    default:
                        myFragment.getContainer().setFragmentChild(WebGameFragment.newInstance(themeID, themeName, gameID, 1));
                        break;
                }
            } else {
                myFragment.getContainer().setFragmentChild(RewardFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level"),0));
            }
        }
    };

    public static ScoreFragment newInstance(int themeID, String themeName, int gameID, boolean newcomer, int score, int level) {
        myFragment.getContainer().setGameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        bundle.putInt("gameID", gameID);
        bundle.putBoolean("newcomer", newcomer);
        bundle.putInt("score", score);
        bundle.putInt("level", level);
        ScoreFragment scoreFragment = new ScoreFragment();
        scoreFragment.setArguments(bundle);
        return scoreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_score, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
        themeID = getArguments().getInt("themeID");
        themeName = getArguments().getString("themeName");
        gameID = getArguments().getInt("gameID");
        setView(view);
        setTheme();
        addScore();
        return view;
    }

    private void setView(View view) {
        relativeLayoutBackground = (RelativeLayout) view.findViewById(R.id.relativeLayoutBackground);
        strokeTextViewTitle = (StrokeTextView) view.findViewById(R.id.strokeTextViewTitle);
        textViewScore = (TextView) view.findViewById(R.id.textViewScore);
    }

    private void setTheme() {
        relativeLayoutBackground.setBackgroundResource(getResources().getIdentifier("bg_t" + themeID + "_4", "drawable", activity.getPackageName()));
    }

    private void setTitle() {
        strokeTextViewTitle.setText(tableTheme.get(themeID, gameID).getGameName());
    }

    private void addScore() {
        handler.postDelayed(runnable, 5000);
        textViewScore.setText(String.valueOf(getArguments().getInt("score")));

        //Add By Rex
        if(mGaTracker!=null){
            if (!sharedPreferences.getBoolean("t" + String.valueOf(themeID) + "_g" + String.valueOf(gameID) + "_ga_1b", false)) {
                mGaTracker.setScreenName("Game Screen");
                mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                mGaTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("1_Start_Game")
                        .setAction("Theme ID:" + String.valueOf(themeID) + ",Game ID:" + String.valueOf(gameID))
                        .setLabel("Complete Game")
                        .build());
                sharedPreferences.edit().putBoolean("t" + String.valueOf(themeID) + "_g" + String.valueOf(gameID) + "_ga_1b" , true).apply();
            }



            ItemStage itemStage = tableStage.get(themeID,gameID,getArguments().getInt("level"));
            int percentScore = (int)( (double)getArguments().getInt("score") / (double)itemStage.getFullMark() * 100 ) ;
            mGaTracker.setScreenName("Game Screen");
            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mGaTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("3_Game_Score")
                    .setAction( "Theme ID:" + String.valueOf(themeID) + ",Game ID:" + String.valueOf(gameID) + ",Level ID:" + String.valueOf(getArguments().getInt("level")) )
                    .setLabel( String.valueOf( percentScore ) + "%"  )
                    .build());

            if ( percentScore == 100 )
            {
                mGaTracker.setScreenName("Game Screen");
                mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                mGaTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("4_Game_Full_Score")
                        .setAction( "Theme ID:" + String.valueOf(themeID) + ",Game ID:" + String.valueOf(gameID) + ",Level ID:" + String.valueOf(getArguments().getInt("level")) )
                        .setLabel( "Full Score" )
                        .build());
            }
        }

        if (getArguments().getBoolean("newcomer")) {
            //Added By Rex
            ItemTheme itemTheme = new ItemTheme(
                    themeID,
                    gameID,
                    tableTheme.get(themeID, gameID).getGameName(),
                    tableTheme.get(themeID, gameID).getThemePath(),
                    1
            );
            tableTheme.update(itemTheme);

            ItemGamePlay itemGamePlay = new ItemGamePlay(
                    themeID,
                    gameID,
                    getArguments().getInt("level"),
                    0,
                    getArguments().getInt("score"));
            tableGamePlay.insert(itemGamePlay);
        } else {
            setTitle();
            ItemGame itemGame = new ItemGame(
                    tableGame.get(gameID).getGameID(),
                    tableGame.get(gameID).getGameType(),
                    tableGame.get(gameID).getGameVersion(),
                    tableGame.get(gameID).getDownloadPath(),
                    tableGame.get(gameID).getGamePath(),
                    tableGame.get(gameID).getBasic(),
                    tableGame.get(gameID).getHasUpdate(),
                    tableGame.get(gameID).getOrder());
            tableGame.update(itemGame);
            ItemTheme itemTheme = new ItemTheme(
                    themeID,
                    gameID,
                    tableTheme.get(themeID, gameID).getGameName(),
                    tableTheme.get(themeID, gameID).getThemePath(),
                    1
            );
            tableTheme.update(itemTheme);
            ItemGamePlay itemGamePlay = new ItemGamePlay(
                    themeID,
                    gameID,
                    getArguments().getInt("level"),
                    tableGamePlay.get(themeID, gameID).getPlayTime() + 1,
                    tableGamePlay.get(themeID, gameID).getScore() + getArguments().getInt("score"));
            tableGamePlay.updateRecord(itemGamePlay);
            if (tableTheme.isFinishedAllBasicGame(themeID)) {
                ItemScore itemScore = new ItemScore();
                itemScore.setThemeID(themeID);
                if (tableScore.get(themeID) == null) {
                    itemScore.setScore(getArguments().getInt("score"));
                    tableScore.insert(itemScore);
                } else {
                    itemScore.setScore(tableScore.get(themeID).getScore() + getArguments().getInt("score"));
                    tableScore.update(itemScore);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}