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

import static com.sahk.earlyliteracy.applications.Config.VIDEO_JQANDA;
import static com.sahk.earlyliteracy.applications.Config.VIDEO_SONG;
import static com.sahk.earlyliteracy.applications.Config.VIDEO_SQANDAA;
import static com.sahk.earlyliteracy.applications.Config.VIDEO_THEME;

public class VideoListFragment extends BaseFragment {

    private CustomTextView customTextViewTitle;
    private ImageButton imageButtonVideo, imageButtonQA1, imageButtonSong, imageButtonQA2;
    private int themeID;
    private RelativeLayout relativeLayoutBackground;
    private String themeName;

    public static VideoListFragment newInstance(int themeID, String themeName) {
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        VideoListFragment videoListFragment = new VideoListFragment();
        videoListFragment.setArguments(bundle);
        return videoListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_videolist, viewGroup, false);
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
        imageButtonVideo = (ImageButton) view.findViewById(R.id.imageButtonVideo);
        imageButtonQA1 = (ImageButton) view.findViewById(R.id.imageButtonQA1);
        imageButtonSong = (ImageButton) view.findViewById(R.id.imageButtonSong);
        imageButtonQA2 = (ImageButton) view.findViewById(R.id.imageButtonQA2);
        relativeLayoutBackground = (RelativeLayout) view.findViewById(R.id.relativeLayoutBackground);
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        imageButtonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(themeID, themeName, VIDEO_THEME, false));
            }
        });
        imageButtonQA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(themeID, themeName, VIDEO_JQANDA, false));
            }
        });
        imageButtonSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(themeID, themeName, VIDEO_SONG, false));
            }
        });
        imageButtonQA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(VideoFragment.newInstance(themeID, themeName, VIDEO_SQANDAA, false));
            }
        });
    }


    private void setTheme() {
        imageButtonVideo.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_m_video", "drawable", activity.getPackageName()));
        imageButtonQA1.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_m_qa1", "drawable", activity.getPackageName()));
        imageButtonSong.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_m_song", "drawable", activity.getPackageName()));
        imageButtonQA2.setImageResource(getResources().getIdentifier("menu_t" + themeID + "_m_qa2", "drawable", activity.getPackageName()));
        relativeLayoutBackground.setBackgroundResource(getResources().getIdentifier("bg_t" + themeID + "_2", "drawable", activity.getPackageName()));
    }

    private void setTitle() {
        customTextViewTitle.setText(themeName);
    }
}