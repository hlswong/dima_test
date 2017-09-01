package com.sahk.earlyliteracy;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.LinkedList;
import java.util.List;

import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.constants.GlobalValue;
import com.sahk.earlyliteracy.database.TableReward;
import com.sahk.earlyliteracy.font.TypefaceManager;
import com.sahk.earlyliteracy.fragments.AwardFragment;
import com.sahk.earlyliteracy.fragments.GameListFragment;
import com.sahk.earlyliteracy.fragments.GameNineFragment;
import com.sahk.earlyliteracy.fragments.SettingsFragment;
import com.sahk.earlyliteracy.fragments.WebGameFragment;

public abstract class BaseActivity<Fragment> extends AppCompatActivity {

    public Tracker tracker;
    public Activity activity;
    public TypefaceManager typefaceManager;
    private ImageButton imageButtonBack, imageButtonHome, imageButtonSkip, imageButtonAward, imageButtonSound, imageButtonSettings, imageButtonReadme, imageButtonBgMusic;
    private List<Fragment> childFragmentStack;
    private long firstTime;
    private RelativeLayout relativeLayoutToolbar;
    private SharedPreferences sharedPreferences;
    private Fragment mCurrentFragment;
    private Fragment mNewFragment;
    private Context mContext;
    boolean isPlayed = false; // Add By Rex
    private MediaPlayer m = new MediaPlayer(); // Add By Rex

    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
            tracker = googleAnalytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        setup();
        initView();
        init(savedInstanceState);
        mContext = this;
    }

    private void setup() {
        tracker = getDefaultTracker();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        childFragmentStack = new LinkedList<>();
        typefaceManager = new TypefaceManager(getResources().getAssets());
        TableReward tableReward = new TableReward(this);
        if (tableReward.getCount() == 0) {
            tableReward.initReward();
        }

        //Added By Rex
        if(!sharedPreferences.getBoolean("isAppInstalled",false)){
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

            ApplicationInfo appInfo = this.getApplicationInfo();

            // Shortcut name
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name ));
            shortcut.putExtra("duplicate", false); // Just create once

            // Setup activity shoud be shortcut object
            ComponentName componentName = new ComponentName("com.sahk.earlyliteracy" , "com.sahk.earlyliteracy.MainActivity" );
//            Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(componentName);

//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setClass(this, MainActivity.class);

//            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent );
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, getClickIntent(this) );

            // Set shortcut icon
            Intent.ShortcutIconResource iconResource = Intent.ShortcutIconResource.fromContext(this, appInfo.icon);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

            this.sendBroadcast(shortcut);

            sharedPreferences.edit().putBoolean("isAppInstalled", true).apply();
        }
    }

    private Intent getClickIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return intent.setClass(context, MainActivity.class);
    }

    private void initView() {
        relativeLayoutToolbar = (RelativeLayout) findViewById(R.id.relativeLayoutToolbar);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        imageButtonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        imageButtonSkip = (ImageButton) findViewById(R.id.imageButtonSkip);
        imageButtonAward = (ImageButton) findViewById(R.id.imageButtonAward);
        imageButtonSound = (ImageButton) findViewById(R.id.imageButtonSound);
        imageButtonSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
        imageButtonReadme = (ImageButton) findViewById(R.id.imageButtonReadme);
        if (sharedPreferences.getBoolean("sound", false)) {
            imageButtonSound.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this, R.drawable.button_sound_on));
        } else {
            imageButtonSound.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this, R.drawable.button_sound_off));
        }
        imageButtonBgMusic = (ImageButton) findViewById(R.id.imageButtonBgMusic);
        setListener();
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Commented by Rex
//                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
//                adb.setMessage("遊戲會重新開始，是否繼續？");
//                adb.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        backToFirstFragment();
//                    }
//                });
//                adb.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                AlertDialog alertDialog = adb.create();
//                alertDialog.show();

                //Added by Rex
                if (childFragmentStack.size() == 1) {
                } else {
                    mCurrentFragment = childFragmentStack.get(childFragmentStack.size() - 1);
                    if(mCurrentFragment instanceof WebGameFragment || mCurrentFragment instanceof GameNineFragment){
                        AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                        adb.setCancelable(false);   //Added By Rex
                        adb.setMessage("遊戲會重新開始，是否繼續？");
                        adb.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                backToFirstFragment();
                            }
                        });
                        adb.setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = adb.create();
                        alertDialog.show();
                    }
                    else
                    {
                        backToFirstFragment();
                    }
                }
            }
        });
        imageButtonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetVideoFragment getVideoFragment = (GetVideoFragment) childFragmentStack.get(childFragmentStack.size() - 1);
                getVideoFragment.skipVideo();
            }
        });
        imageButtonAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragmentChild((Fragment) AwardFragment.newInstance(GlobalValue.gLastThemeId,"",0,0 ));
            }
        });
        imageButtonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("sound", true)) {
                    sharedPreferences.edit().putBoolean("sound", false).apply();
                    imageButtonSound.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this, R.drawable.button_sound_off));
                } else {
                    sharedPreferences.edit().putBoolean("sound", true).apply();
                    imageButtonSound.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this, R.drawable.button_sound_on));
                }
                GetVideoFragment getVideoFragment = (GetVideoFragment) childFragmentStack.get(childFragmentStack.size() - 1);
                getVideoFragment.setSound();
            }
        });
        imageButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragmentChild((Fragment) SettingsFragment.newInstance());
            }
        });
        imageButtonReadme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                adb.setCancelable(false);   //Added By Rex
                adb.setMessage("遊戲會重新開始，是否繼續？");
                adb.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GetWebGameFragment getWebGameFragment = (GetWebGameFragment) childFragmentStack.get(childFragmentStack.size() - 1);
                        getWebGameFragment.showGameIntro();
                    }
                });
                adb.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = adb.create();
                alertDialog.show();

            }
        });

        imageButtonBgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playBgMusic();
            }
        });
    }

    public abstract void init(Bundle savedInstanceState);

    public void addFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().add(R.id.frameLayout, (android.app.Fragment) fragment).commit();
        childFragmentStack.add(fragment);
    }

    private void setFragment(Fragment fragment, Fragment oldFragment) {
        setFragment(fragment, oldFragment, false);
    }

    private void setFragment(Fragment fragment, Fragment oldFragment, boolean hasSavedInstanceState) {
        if (!hasSavedInstanceState) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            if (oldFragment != null && fragment != oldFragment) {
                int curFragmentIdx = childFragmentStack.indexOf(fragment);
                int oldFragmentIdx = childFragmentStack.indexOf(oldFragment);
                Log.d("navigation","curIdx: " + curFragmentIdx + ", oldIdx: " + oldFragmentIdx );
                if(curFragmentIdx>0 && curFragmentIdx<oldFragmentIdx){
                    for(int i=oldFragmentIdx;i>curFragmentIdx;i--) {
                        childFragmentStack.remove(i);
                    }
                }
                fragmentTransaction.remove((android.app.Fragment) oldFragment);
            }
            fragmentTransaction.replace(R.id.frameLayout, (android.app.Fragment) fragment).commit();
        }
    }

    public void setFragmentChild(Fragment fragment) {
        setFragment(fragment, childFragmentStack.get(childFragmentStack.size() - 1));
        childFragmentStack.add(fragment);
    }

    public void setGameFragment() {
        childFragmentStack.remove(childFragmentStack.size() - 1);
    }

    public void setThemeFragment(Fragment fragment) {
        childFragmentStack = new LinkedList<>();
        childFragmentStack.add(fragment);
    }

    public void removeLastFragment() {
        childFragmentStack.remove(childFragmentStack.size() - 1);
    }

    public void backToFirstFragment() {
        Fragment homeFragment = childFragmentStack.get(0);
        Fragment curFragment = childFragmentStack.remove(childFragmentStack.size() - 1);
        childFragmentStack.clear();
        childFragmentStack.add(homeFragment);
        setFragment(homeFragment, curFragment);
    }

    public void setToolbarMode(int mode) {
        switch (mode) {
            case Config.TOOLBAR_STATUS_HIDE:
                relativeLayoutToolbar.setVisibility(View.GONE);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(true);
                imageButtonHome.setVisibility(View.VISIBLE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.VISIBLE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.VISIBLE);
                imageButtonAward.setEnabled(true);
                imageButtonSound.setVisibility(View.VISIBLE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.VISIBLE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.GONE);
                imageButtonReadme.setEnabled(true);
                break;
            case Config.TOOLBAR_STATUS_THEME:
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                relativeLayoutToolbar.setBackgroundResource(android.R.color.transparent);
                imageButtonBack.setVisibility(View.GONE);
                imageButtonBack.setEnabled(true);
                imageButtonHome.setVisibility(View.GONE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.GONE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.GONE);
                imageButtonAward.setEnabled(true);
                imageButtonSound.setVisibility(View.GONE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.VISIBLE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.GONE);
                imageButtonReadme.setEnabled(true);
                break;
            case Config.TOOLBAR_STATUS_VIDEO:
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                relativeLayoutToolbar.setBackgroundResource(R.drawable.background_toolbar);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(true);
                imageButtonHome.setVisibility(View.VISIBLE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.VISIBLE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.GONE);
                imageButtonAward.setEnabled(true);
                imageButtonSound.setVisibility(View.VISIBLE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.VISIBLE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.GONE);
                imageButtonReadme.setEnabled(true);
                break;
            case Config.TOOLBAR_STATUS_MENU:
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                relativeLayoutToolbar.setBackgroundResource(android.R.color.transparent);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(true);
                imageButtonHome.setVisibility(View.VISIBLE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.GONE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.VISIBLE);
                imageButtonAward.setEnabled(true);
                imageButtonSound.setVisibility(View.GONE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.VISIBLE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.GONE);
                imageButtonReadme.setEnabled(true);
                break;
            case Config.TOOLBAR_STATUS_AWARD:
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(true);
                imageButtonHome.setVisibility(View.VISIBLE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.GONE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.VISIBLE);
                imageButtonAward.setEnabled(false);
                imageButtonSound.setVisibility(View.GONE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.VISIBLE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.GONE);
                imageButtonReadme.setEnabled(true);
                break;
            case Config.TOOLBAR_STATUS_AWARD_FROM_GAME:   //Added By Rex
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(false);
                imageButtonHome.setVisibility(View.VISIBLE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.GONE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.VISIBLE);
                imageButtonAward.setEnabled(false);
                imageButtonSound.setVisibility(View.GONE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.VISIBLE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.GONE);
                imageButtonReadme.setEnabled(true);
                break;
            case Config.TOOLBAR_STATUS_GAMEINTRO:
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(true);
                imageButtonHome.setVisibility(View.GONE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.GONE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.GONE);
                imageButtonAward.setEnabled(true);
                imageButtonSound.setVisibility(View.GONE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.GONE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.VISIBLE);
                imageButtonReadme.setEnabled(false);
                break;
            case Config.TOOLBAR_STATUS_GAME:
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(true);
                imageButtonHome.setVisibility(View.VISIBLE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.GONE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.GONE);
                imageButtonAward.setEnabled(true);
                imageButtonSound.setVisibility(View.GONE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.GONE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.VISIBLE);
                imageButtonReadme.setEnabled(true);
                break;
            case Config.TOOLBAR_STATUS_GAMEEND:
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(false);
                imageButtonHome.setVisibility(View.VISIBLE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.GONE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.VISIBLE);
                imageButtonAward.setEnabled(true);
                imageButtonSound.setVisibility(View.GONE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.GONE);
                imageButtonSettings.setEnabled(true);
                imageButtonReadme.setVisibility(View.VISIBLE);
                imageButtonReadme.setEnabled(false);
                break;
            case Config.TOOLBAR_STATUS_SETTINGS:
                relativeLayoutToolbar.setVisibility(View.VISIBLE);
                relativeLayoutToolbar.setBackgroundResource(android.R.color.transparent);
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonBack.setEnabled(true);
                imageButtonHome.setVisibility(View.VISIBLE);
                imageButtonHome.setEnabled(true);
                imageButtonSkip.setVisibility(View.GONE);
                imageButtonSkip.setEnabled(true);
                imageButtonAward.setVisibility(View.GONE);
                imageButtonAward.setEnabled(true);
                imageButtonSound.setVisibility(View.GONE);
                imageButtonSound.setEnabled(true);
                imageButtonSettings.setVisibility(View.VISIBLE);
                imageButtonSettings.setEnabled(false);
                imageButtonReadme.setVisibility(View.GONE);
                imageButtonReadme.setEnabled(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (childFragmentStack.size() == 1) {
            exitByDoubleClicked();
        } else {
            mCurrentFragment = childFragmentStack.get(childFragmentStack.size() - 1);
            Fragment newFragment=null;
            if(mCurrentFragment instanceof WebGameFragment || mCurrentFragment instanceof GameNineFragment){
                for(int i = childFragmentStack.size() -1; i>0;i--){
                    Fragment theFragment = childFragmentStack.get(childFragmentStack.size() - i);
                    if(theFragment instanceof GameListFragment) {
                        newFragment = theFragment;
                        Log.d("navigation","fragment - " + i + "/" + childFragmentStack.size());
                    }
                }
//                Fragment theFragment = childFragmentStack.get(childFragmentStack.size() - 3);
//                if(theFragment instanceof GameListFragment){
//                    newFragment = theFragment;
//                    Log.d("navigation","fragment - 3");
//                }else{
//                    theFragment = childFragmentStack.get(childFragmentStack.size() - 4);
//                    if(theFragment instanceof GameListFragment) {
//                        newFragment = theFragment;
//                        Log.d("navigation","fragment - 4");
//                    }
//                }
            }
            if(newFragment == null){
                Log.d("navigation","cannot find list fragment");
                newFragment = childFragmentStack.get(childFragmentStack.size() - 2);

            }
            mNewFragment = newFragment;
            if(mCurrentFragment instanceof WebGameFragment || mCurrentFragment instanceof GameNineFragment){

                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setCancelable(false);   //Added By Rex
                adb.setMessage("遊戲會重新開始，是否繼續？");
                adb.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Fragment curFragment = childFragmentStack.remove(childFragmentStack.size() - 1);
                        setFragment(mNewFragment, mCurrentFragment);
                    }
                });
                adb.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }else {
                Log.d("navigation","not game fragment");
                Fragment curFragment = childFragmentStack.remove(childFragmentStack.size() - 1);
                setFragment(newFragment, curFragment);
            }
        }
    }


    private void exitByDoubleClicked() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            finish();
        } else {
            firstTime = System.currentTimeMillis();
           // Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
        }
    }

    public interface GetVideoFragment {
        void setSound();

        void skipVideo();
    }

    public interface GetWebGameFragment {
        void showGameIntro();
    }

    // Add By Rex
    @Override
    protected void onResume() {
        if (isPlayed) {
            m.start();
        }
        super.onResume();
    }

    // Add By Rex
    @Override
    protected void onDestroy() {
        if (isPlayed) {
            m.stop();
            m.release();
        }
        super.onDestroy();
    }

    // Add By Rex
    @Override
    protected void onPause() {
        if (isPlayed) {
            m.pause();
        }
        super.onPause();
    }

    // Add By Rex
    public void playBgMusic(){
        try {
            if (isPlayed)
            {
                imageButtonBgMusic.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this, R.drawable.button_bg_music_off));
                m.stop();
                m.release();
                isPlayed = false;
                return;
            }
            else
            {
                imageButtonBgMusic.setImageDrawable(ContextCompat.getDrawable(BaseActivity.this, R.drawable.button_bg_music_on));
                isPlayed = true;
            }

//            if (m.isPlaying()) {
//                m.stop();
//                m.release();
//                m = new MediaPlayer();
//            }
            m = new MediaPlayer();

            AssetFileDescriptor descriptor = this.getAssets().openFd("bg_music.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(true);
            m.start();
        } catch (Exception e) {
//            Log.d("Tag Action", "musicPlayStop: " + e.getMessage().toString() );
//            e.printStackTrace();
        }
    }

    // Add By Rex
    public void checkTurnOffMusic()
    {
        if (isPlayed){
            playBgMusic();
        }
    }
}