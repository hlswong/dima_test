package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;

public class GameTenFragment extends BaseFragment {

    public static GameTenFragment newInstance(int intTheme) {
        Bundle bundle = new Bundle();
        bundle.putInt("intTheme", intTheme);
        GameTenFragment gameTenFragment = new GameTenFragment();
        gameTenFragment.setArguments(bundle);
        return gameTenFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_gameten, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_GAME);
        return view;
    }
}