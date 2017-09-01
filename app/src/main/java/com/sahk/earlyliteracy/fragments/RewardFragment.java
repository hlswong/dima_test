package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.constants.GlobalValue;
import com.sahk.earlyliteracy.database.ItemReward;

public class RewardFragment extends BaseFragment {

    private ImageView imageViewReward;
    private Handler handler = new Handler();
    private int gameID;
    private int themeID;
    private int level; //Added by Rex
    private RelativeLayout relativeLayoutBackground;
    private String themeName;
    private int gotoAward;

    @SuppressWarnings("unchecked")
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //Commented by Rex
//            myFragment.getContainer().setFragmentChild(AwardFragment.newInstance(themeID));
            //Added by Rex
            myFragment.getContainer().setFragmentChild(RewardFragment.newInstance(themeID, themeName, gameID, level , 1 ));
        }
    };

    public static RewardFragment newInstance(int themeID, String themeName, int gameID, int level , int gotoAward) {
        myFragment.getContainer().setGameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        bundle.putInt("gameID", gameID);
        bundle.putInt("level", level);
        bundle.putInt("gotoAward", gotoAward); //Added By Rex
        RewardFragment rewardFragment = new RewardFragment();
        rewardFragment.setArguments(bundle);
        return rewardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_reward, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
        themeID = getArguments().getInt("themeID");
        themeName = getArguments().getString("themeName");
        gameID = getArguments().getInt("gameID");
        level = getArguments().getInt("level"); //Added by Rex
        gotoAward = getArguments().getInt("gotoAward"); //Added by Rex
        setView(view);
        setTheme();
        checkScore();
        return view;
    }

    private void setView(View view) {
        imageViewReward = (ImageView) view.findViewById(R.id.imageViewReward);
        relativeLayoutBackground = (RelativeLayout) view.findViewById(R.id.relativeLayoutBackground);
    }

    private void setTheme() {
        relativeLayoutBackground.setBackgroundResource(getResources().getIdentifier("bg_t" + themeID + "_7", "drawable", activity.getPackageName()));
    }

    @SuppressWarnings("unchecked")
    private void checkScore() {
        if (tableScore.get(themeID) != null) {

            //Commented by Rex
//            if (tableScore.get(themeID).getScore() >= 4080) {
//                imageViewReward.setImageResource(android.R.color.transparent);
//                handler.postDelayed(runnable, 5000);
//            } else
//            if (tableScore.get(themeID).getScore() >= 3300 && !tableReward.isRedeemed(16) && !tableReward.isRedeemed(8)) {
//                tableReward.update(new ItemReward(16, "item", "guitar", 1));
//                tableReward.update(new ItemReward(8, "monster", "monster4", 1));
//                imageViewReward.setImageResource(R.drawable.reward_guitar);
//                handler.postDelayed(runnable, 5000);
//            } else if (tableScore.get(themeID).getScore() >= 2800 && !tableReward.isRedeemed(15) && !tableReward.isRedeemed(7)) {
//                tableReward.update(new ItemReward(15, "item", "skateboard", 1));
//                tableReward.update(new ItemReward(7, "monster", "monster3", 1));
//                imageViewReward.setImageResource(R.drawable.reward_skateboard);
//                handler.postDelayed(runnable, 5000);
//            } else if (tableScore.get(themeID).getScore() >= 2100 && !tableReward.isRedeemed(14) && !tableReward.isRedeemed(6)) {
//                tableReward.update(new ItemReward(14, "item", "bag", 1));
//                tableReward.update(new ItemReward(6, "monster", "monster2", 1));
//                imageViewReward.setImageResource(R.drawable.reward_bag);
//                handler.postDelayed(runnable, 5000);
//            } else if (tableScore.get(themeID).getScore() >= 1500 && !tableReward.isRedeemed(13)) {
//                tableReward.update(new ItemReward(13, "item", "teats", 1));
//                imageViewReward.setImageResource(R.drawable.reward_teats);
//                handler.postDelayed(runnable, 5000);
//            } else if (tableScore.get(themeID).getScore() >= 1060 && !tableReward.isRedeemed(5)) {
//                tableReward.update(new ItemReward(5, "monster", "monster1", 1));
//                imageViewReward.setImageResource(R.drawable.mon1);
//                handler.postDelayed(runnable, 5000);
//            } else if (tableScore.get(themeID).getScore() >= 800 && !tableReward.isRedeemed(12) && !tableReward.isRedeemed(4)) {
//                tableReward.update(new ItemReward(12, "item", "hand", 1));
//                tableReward.update(new ItemReward(4, "egg", "egg4", 1));
//                imageViewReward.setImageResource(R.drawable.reward_hand);
//                handler.postDelayed(runnable, 5000);
//            } else if (tableScore.get(themeID).getScore() >= 600 && !tableReward.isRedeemed(11) && !tableReward.isRedeemed(3)) {
//                tableReward.update(new ItemReward(11, "item", "spray", 1));
//                tableReward.update(new ItemReward(3, "egg", "egg3", 1));
//                imageViewReward.setImageResource(R.drawable.reward_spray);
//                handler.postDelayed(runnable, 5000);
//            } else if (tableScore.get(themeID).getScore() >= 400 && !tableReward.isRedeemed(10) && !tableReward.isRedeemed(2)) {
//                tableReward.update(new ItemReward(10, "item", "lamp", 1));
//                tableReward.update(new ItemReward(2, "egg", "egg2", 1));
//                imageViewReward.setImageResource(R.drawable.reward_lamp);
//                handler.postDelayed(runnable, 5000);
//            } else if (tableScore.get(themeID).getScore() >= 200 && !tableReward.isRedeemed(9)) {
//                tableReward.update(new ItemReward(9, "item", "grass", 1));
//                imageViewReward.setImageResource(R.drawable.reward_grass);
//                handler.postDelayed(runnable, 5000);
//            } else {
//                myFragment.getContainer().setFragmentChild(GameRateFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level")));
//            }


//            int test1 = tableScore.get(themeID).getScore();
//            int test2 = (200/ GlobalValue.gTestDivide);
//            boolean test3 = !tableReward.isRedeemed(1 , themeID);

            // Added By Rex
            if ( tableTheme.isFinishedAllBasicGame(themeID) && !tableReward.isRedeemed(1 , themeID)) {
                tableReward.insert(new ItemReward(1, "egg", "egg1", 1 , themeID));
                int eggResId = getResources().getIdentifier( "egg0"+ String.valueOf(themeID), "drawable", context.getPackageName());
                imageViewReward.setImageResource(eggResId);
                handler.postDelayed(runnable, 5000);
            }
            else if (tableScore.get(themeID).getScore() >= (200/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(9 , themeID)) {
                tableReward.insert(new ItemReward(9, "item", "grass", 1 , themeID));
                imageViewReward.setImageResource(R.drawable.award01);
                handler.postDelayed(runnable, 5000);
            } else if (tableScore.get(themeID).getScore() >= (400/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(10 , themeID) && !tableReward.isRedeemed(2 , themeID)) {
                tableReward.insert(new ItemReward(10, "item", "lamp", 1 , themeID));
                tableReward.insert(new ItemReward(2, "egg", "egg2", 1 , themeID));
                imageViewReward.setImageResource(R.drawable.award02);
                handler.postDelayed(runnable, 5000);
            } else if (tableScore.get(themeID).getScore() >= (600/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(11 , themeID) && !tableReward.isRedeemed(3 , themeID)) {
                tableReward.insert(new ItemReward(11, "item", "spray", 1 , themeID));
                tableReward.insert(new ItemReward(3, "egg", "egg3", 1 , themeID));
                imageViewReward.setImageResource(R.drawable.award03);
                handler.postDelayed(runnable, 5000);
            } else if (tableScore.get(themeID).getScore() >= (800/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(12 , themeID) && !tableReward.isRedeemed(4 , themeID)) {
                tableReward.insert(new ItemReward(12, "item", "hand", 1 , themeID));
                tableReward.insert(new ItemReward(4, "egg", "egg4", 1 , themeID));
                imageViewReward.setImageResource(R.drawable.award04);
                handler.postDelayed(runnable, 5000);
            } else if (tableScore.get(themeID).getScore() >= (1060/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(5 , themeID)) {
                tableReward.insert(new ItemReward(5, "monster", "monster1", 1 , themeID));
                int eggResId = getResources().getIdentifier( "monster0"+ String.valueOf(themeID), "drawable", context.getPackageName());
                imageViewReward.setImageResource(eggResId);
                handler.postDelayed(runnable, 5000);
            } else if (tableScore.get(themeID).getScore() >= (1500/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(13 , themeID)) {
                tableReward.insert(new ItemReward(13, "item", "teats", 1 , themeID));
                imageViewReward.setImageResource(R.drawable.award05);
                handler.postDelayed(runnable, 5000);
            } else if (tableScore.get(themeID).getScore() >= (2100/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(14 , themeID) && !tableReward.isRedeemed(6 , themeID)) {
                tableReward.insert(new ItemReward(14, "item", "bag", 1 , themeID));
                tableReward.insert(new ItemReward(6, "monster", "monster2", 1 , themeID));
                imageViewReward.setImageResource(R.drawable.award06);
                handler.postDelayed(runnable, 5000);
            } else if (tableScore.get(themeID).getScore() >= (2800/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(15 , themeID) && !tableReward.isRedeemed(7 , themeID)) {
                tableReward.insert(new ItemReward(15, "item", "skateboard", 1 , themeID));
                tableReward.insert(new ItemReward(7, "monster", "monster3", 1 , themeID));
                imageViewReward.setImageResource(R.drawable.award07);
                handler.postDelayed(runnable, 5000);
            } else if (tableScore.get(themeID).getScore() >= (3300/ GlobalValue.gTestDivide)  && !tableReward.isRedeemed(16 , themeID) && !tableReward.isRedeemed(8 , themeID)) {
                tableReward.insert(new ItemReward(16, "item", "guitar", 1 , themeID));
                tableReward.insert(new ItemReward(8, "monster", "monster4", 1 , themeID));
//                imageViewReward.setImageResource(R.drawable.award08);
                imageViewReward.setImageResource(R.drawable.award09);
                handler.postDelayed(runnable, 5000);
            } else {
                if ( gotoAward == 1 )
                {
                    myFragment.getContainer().setFragmentChild(AwardFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level")));
                }
                else {
                    myFragment.getContainer().setFragmentChild(GameRateFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level")));
                }
            }
        } else {
            myFragment.getContainer().setFragmentChild(GameRateFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level")));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}