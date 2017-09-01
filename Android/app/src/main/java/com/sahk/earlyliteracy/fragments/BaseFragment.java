package com.sahk.earlyliteracy.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.analytics.Tracker;

import com.sahk.earlyliteracy.applications.MyApplication;
import com.sahk.earlyliteracy.applications.MyFragment;
import com.sahk.earlyliteracy.database.TableGame;
import com.sahk.earlyliteracy.database.TableGamePlay;
import com.sahk.earlyliteracy.database.TableReward;
import com.sahk.earlyliteracy.database.TableScore;
import com.sahk.earlyliteracy.database.TableStage;
import com.sahk.earlyliteracy.database.TableTheme;
import com.sahk.earlyliteracy.database.TableVideo;
import com.sahk.earlyliteracy.font.TypefaceManager;

public abstract class BaseFragment extends Fragment {

    public static MyFragment myFragment;
    public static MyApplication context;
    public Activity activity;
    public SharedPreferences sharedPreferences;
    public TableGame tableGame;
    public TableGamePlay tableGamePlay;
    public TableReward tableReward;
    public TableScore tableScore;
    public TableStage tableStage;
    public TableTheme tableTheme;
    public TableVideo tableVideo;
    public TypefaceManager typefaceManager;
    protected Tracker mGaTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (MyApplication) getActivity().getApplicationContext();
        mGaTracker = context.getDefaultTracker();
        typefaceManager = new TypefaceManager(getActivity().getAssets());
        sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = activity;
            myFragment = (MyFragment) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        } finally {
            tableGame = new TableGame(activity);
            tableGamePlay = new TableGamePlay(activity);
            tableReward = new TableReward(activity);
            tableScore = new TableScore(activity);
            tableStage = new TableStage(activity);
            tableTheme = new TableTheme(activity);
            tableVideo = new TableVideo(activity);
        }
    }
}