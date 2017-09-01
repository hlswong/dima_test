package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.widgets.CustomTextView;

public class MenuFragment extends BaseFragment {

    private ImageButton imageButtonGame, imageButtonVideoTutorial;
    private int themeID;
    private RelativeLayout relativeLayoutBackground;
    private String themeName;
    private CustomTextView customTextViewTitle;

    public static MenuFragment newInstance(int themeID, String themeName) {
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        MenuFragment menuFragment = new MenuFragment();
        menuFragment.setArguments(bundle);
        return menuFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_menu, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_MENU);
        themeID = getArguments().getInt("themeID");
        themeName = getArguments().getString("themeName");
        setView(view);
        setListener();
        setTheme();
        setTitle();
        return view;
    }

    private void setView(View view) {
        customTextViewTitle = (CustomTextView) view.findViewById(R.id.customTextViewTitle);
        imageButtonGame = (ImageButton) view.findViewById(R.id.imageButtonGame);
        imageButtonVideoTutorial = (ImageButton) view.findViewById(R.id.imageButtonVideoTutorial);
        relativeLayoutBackground = (RelativeLayout) view.findViewById(R.id.relativeLayoutBackground);
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        imageButtonGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(GameListFragment.newInstance(themeID, themeName, -1)); //Edit by Rex
            }
        });
        imageButtonVideoTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(VideoListFragment.newInstance(themeID, themeName));
            }
        });
    }

    private void setTheme() {
        imageButtonGame.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_game", "drawable", activity.getPackageName()));
        imageButtonVideoTutorial.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_video", "drawable", activity.getPackageName()));
        relativeLayoutBackground.setBackgroundResource(getResources().getIdentifier("bg_t" + themeID + "_6", "drawable", activity.getPackageName()));
    }

    private void setTitle() {
        customTextViewTitle.setText(themeName);
    }
}
