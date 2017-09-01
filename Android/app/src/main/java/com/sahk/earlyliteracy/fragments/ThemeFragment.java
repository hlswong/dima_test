package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.constants.GlobalValue;

import static com.sahk.earlyliteracy.applications.Config.THEME1_NAME;
import static com.sahk.earlyliteracy.applications.Config.THEME2_NAME;
import static com.sahk.earlyliteracy.applications.Config.THEME3_NAME;
import static com.sahk.earlyliteracy.applications.Config.THEME4_NAME;
import static com.sahk.earlyliteracy.applications.Config.THEME5_NAME;
import static com.sahk.earlyliteracy.applications.Config.THEME6_NAME;
import static com.sahk.earlyliteracy.applications.Config.VIDEO_THEME;

public class ThemeFragment extends BaseFragment {

    private ImageButton imageButtonBody, imageButtonFamily, imageButtonRead, imageButtonSchool, imageButtonNature, imageButtonCycle;

    @SuppressWarnings("unchecked")
    public static ThemeFragment newInstance() {
        ThemeFragment themeFragment = new ThemeFragment();
        myFragment.getContainer().setThemeFragment(themeFragment);
        return new ThemeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_theme, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_THEME);
        setView(view);
        setListener();
        GlobalValue.gLastGameId = -1;
        return view;
    }

    private void setView(View view) {
        imageButtonBody = (ImageButton) view.findViewById(R.id.imageButtonBody);
        imageButtonFamily = (ImageButton) view.findViewById(R.id.imageButtonFamily);
        imageButtonRead = (ImageButton) view.findViewById(R.id.imageButtonRead);
        imageButtonSchool = (ImageButton) view.findViewById(R.id.imageButtonSchool);
        imageButtonNature = (ImageButton) view.findViewById(R.id.imageButtonNature);
        imageButtonCycle = (ImageButton) view.findViewById(R.id.imageButtonCycle);
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        imageButtonBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("theme1_first", true)) {
                    myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(1, THEME1_NAME, VIDEO_THEME, true));
                } else {
                    myFragment.getContainer().setFragmentChild(MenuFragment.newInstance(1, THEME1_NAME));
                }
                GlobalValue.gLastThemeId =1;
            }
        });
        imageButtonFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("theme2_first", true)) {
                    myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(2, THEME2_NAME, VIDEO_THEME, true));
                } else {
                    myFragment.getContainer().setFragmentChild(MenuFragment.newInstance(2, THEME2_NAME));
                }
                GlobalValue.gLastThemeId =2;
            }
        });
        imageButtonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("theme4_first", true)) {
                    myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(4, THEME3_NAME, VIDEO_THEME, true));
                } else {
                    myFragment.getContainer().setFragmentChild(MenuFragment.newInstance(4, THEME3_NAME));
                }
                GlobalValue.gLastThemeId =4;
            }
        });
        imageButtonSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("theme3_first", true)) {
                    myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(3, THEME4_NAME, VIDEO_THEME, true));
                } else {
                    myFragment.getContainer().setFragmentChild(MenuFragment.newInstance(3, THEME4_NAME));
                }
                GlobalValue.gLastThemeId =3;
            }
        });
        imageButtonNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("theme5_first", true)) {
                    myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(5, THEME5_NAME, VIDEO_THEME, true));
                } else {
                    myFragment.getContainer().setFragmentChild(MenuFragment.newInstance(5, THEME5_NAME));
                }
                GlobalValue.gLastThemeId =5;
            }
        });
        imageButtonCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("theme6_first", true)) {
                    myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(6, THEME6_NAME, VIDEO_THEME, true));
                } else {
                    myFragment.getContainer().setFragmentChild(MenuFragment.newInstance(6, THEME6_NAME));
                }
                GlobalValue.gLastThemeId =6;
            }
        });
    }
}