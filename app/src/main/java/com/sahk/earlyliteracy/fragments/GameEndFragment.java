package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;

import com.hedgehog.ratingbar.RatingBar;
import com.sahk.earlyliteracy.BaseActivity;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.database.ItemStage;

public class GameEndFragment extends BaseFragment {

    private ImageButton imageButtonAgain;
    private ImageButton imageButtonClose;
    private ImageButton imageButtonNext;
    private ImageButton imageButtonOther;
    private ImageButton imageButtonRating;
    private ImageButton imageButtonSubmit;
    private int gameID;
    private int themeID;
    private int mLevel;
    private RatingBar ratingBarGame;
    private RelativeLayout relativeLayoutBackground;
    private RelativeLayout relativeLayoutRating;
    private String themeName;
    private float numGameRate;

    public static GameEndFragment newInstance(int themeID, String themeName, int gameID, int level) {
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        bundle.putInt("gameID", gameID);
        bundle.putInt("level", level);
        GameEndFragment gameEndFragment = new GameEndFragment();
        gameEndFragment.setArguments(bundle);
        return gameEndFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_gameend, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_GAMEEND);
        themeID = getArguments().getInt("themeID", themeID);
        themeName = getArguments().getString("themeName", themeName);
        gameID = getArguments().getInt("gameID");
        mLevel = getArguments().getInt("level");
        setView(view);
        setListener();
        setTheme();
        if (getArguments().getInt("level") == 2) {
            imageButtonNext.setVisibility(View.GONE);
        }
        if (!sharedPreferences.getBoolean("t" + String.valueOf(themeID) + "_g" + String.valueOf(gameID) + "_s" + String.valueOf(getArguments().getInt("level")) + "_survery", false)) {
            imageButtonRating.setVisibility(View.VISIBLE);

            //Added By Rex
            if (!sharedPreferences.getBoolean("t" + String.valueOf(themeID) + "_g" + String.valueOf(gameID) + "_s" + String.valueOf(getArguments().getInt("level")) + "_firstpoprate", false)) {
                relativeLayoutRating.setVisibility(View.VISIBLE);
                myFragment.setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
                sharedPreferences.edit().putBoolean("t" + String.valueOf(themeID) + "_g" + String.valueOf(gameID) + "_s" + String.valueOf(getArguments().getInt("level")) + "_firstpoprate", true).apply();
            }

        } else {
            imageButtonRating.setVisibility(View.GONE);
        }
        return view;
    }

    private void setView(View view) {
        imageButtonAgain = (ImageButton) view.findViewById(R.id.imageButtonAgain);
        imageButtonClose = (ImageButton) view.findViewById(R.id.imageButtonClose);
        imageButtonNext = (ImageButton) view.findViewById(R.id.imageButtonNext);
        imageButtonOther = (ImageButton) view.findViewById(R.id.imageButtonOther);
        imageButtonRating = (ImageButton) view.findViewById(R.id.imageButtonRating);
        imageButtonSubmit = (ImageButton) view.findViewById(R.id.imageButtonSubmit);
        ratingBarGame = (RatingBar) view.findViewById(R.id.ratingBarGame);
        relativeLayoutBackground = (RelativeLayout) view.findViewById(R.id.relativeLayoutBackground);
        relativeLayoutRating = (RelativeLayout) view.findViewById(R.id.relativeLayoutRating);
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        imageButtonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add By Rex
                if(mGaTracker!=null){
                    mGaTracker.setScreenName("Game Screen");
                    mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                    mGaTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("2_Game_Complete")
                            .setAction( "Theme ID:" + String.valueOf(themeID) + ",Game ID:" + String.valueOf(gameID) + ",Level ID:" + String.valueOf(mLevel) )
                            .setLabel("Complete")
                            .build());
                }

                if(gameID==9){
                    //play level 1 again
                    myFragment.getContainer().setFragmentChild(GameNineFragment.newInstance(themeID, themeName, mLevel));
                }else{
                    myFragment.getContainer().setFragmentChild(WebGameFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level")));
                }

            }
        });
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutRating.setVisibility(View.GONE);
                ((BaseActivity)activity).setToolbarMode(Config.TOOLBAR_STATUS_GAMEEND);//Added By Rex
            }
        });
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add By Rex
                if(mGaTracker!=null){
                    mGaTracker.setScreenName("Game Screen");
                    mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                    mGaTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("2_Game_Complete")
                            .setAction( "Theme ID:" + String.valueOf(themeID) + ",Game ID:" + String.valueOf(gameID) + ",Level ID:2" )
                            .setLabel("Complete")
                            .build());
                }

                if(gameID==9){
                    //play next level(2)
                    myFragment.getContainer().setFragmentChild(GameNineFragment.newInstance(themeID, themeName, 2));
                }else {
                    myFragment.getContainer().setFragmentChild(WebGameFragment.newInstance(themeID, themeName, gameID, 2)); //Edit by Rex
                }
            }
        });
        imageButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(GameListFragment.newInstance(themeID, themeName, gameID));
            }
        });
        imageButtonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutRating.setVisibility(View.VISIBLE);
                ((BaseActivity)activity).setToolbarMode(Config.TOOLBAR_STATUS_HIDE);//Added By Rex
            }
        });
        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutRating.setVisibility(View.GONE);
                ((BaseActivity)activity).setToolbarMode(Config.TOOLBAR_STATUS_GAMEEND);  //Added By Rex
                imageButtonRating.setVisibility(View.GONE);
                sharedPreferences.edit().putBoolean("t" + themeID + "_g" + gameID + "_s" + getArguments().getInt("level") + "_survery", true).apply();
                myFragment.getContainer().tracker.send(new HitBuilders.SocialBuilder()
                        .setCustomDimension((int) numGameRate, "game1")
                        .setAction("test")
                        .build());
                Log.d("GameRate:", String.valueOf(numGameRate) );

                //Comment By Rex
//                myFragment.getContainer().tracker.send(new HitBuilders.SocialBuilder()
//                        .setCustomDimension((int) ratingBarGame.getRating(), "game1")
//                        .setAction("test")
//
//                        .build());
//                Log.d("test", String.valueOf(ratingBarGame.getRating()));

                //Add By Rex
                if(mGaTracker!=null){
                    mGaTracker.setScreenName("Game Screen");
                    mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                    mGaTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("5_Game_User_Rate")
                            .setAction( "Theme ID:" + String.valueOf(themeID) + ",Game ID:" + String.valueOf(gameID) + ",Level ID:" + String.valueOf(getArguments().getInt("level")) )
                            .setLabel( String.valueOf(numGameRate ) )
                            .build());
                }
            }
        });
        relativeLayoutRating.setOnClickListener(null);

        numGameRate = 3;
        ratingBarGame.setStar(numGameRate);
        ratingBarGame.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener()
        {
            @Override
            public void onRatingChange(float RatingCount) {
                numGameRate = RatingCount;
            }
        });
    }

    private void setTheme() {
        imageButtonAgain.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_again", "drawable", activity.getPackageName()));
        imageButtonNext.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_next", "drawable", activity.getPackageName()));
        imageButtonOther.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_other", "drawable", activity.getPackageName()));
        relativeLayoutBackground.setBackgroundResource(getResources().getIdentifier("bg_t" + themeID + "_6", "drawable", activity.getPackageName()));
    }
}