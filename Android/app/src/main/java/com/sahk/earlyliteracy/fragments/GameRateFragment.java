package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;

public class GameRateFragment extends BaseFragment {

    private int gameID;
    private int themeID;
    private String themeName;

    public static GameRateFragment newInstance(int themeID, String themeName, int gameID, int level) {
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        bundle.putInt("gameID", gameID);
        bundle.putInt("level", level);
        GameRateFragment gameRateFragment = new GameRateFragment();
        gameRateFragment.setArguments(bundle);
        return gameRateFragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_gamenine, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
        themeID = getArguments().getInt("themeID", themeID);
        themeName = getArguments().getString("themeName", themeName);
        gameID = getArguments().getInt("gameID");
        myFragment.getContainer().setFragmentChild(GameEndFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level")));
        return view;
    }
}