package com.sahk.earlyliteracy.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.sahk.earlyliteracy.BaseActivity;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.constants.GlobalValue;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class GameNineFragment extends BaseFragment implements BaseActivity.GetWebGameFragment{


    private ImageView imgQuestionBg= null;
    private ImageView imgAngle= null;

    private ImageView imgQuestionLeft= null;
    private ImageView imgQuestionRight= null;

    private ImageButton btnRecord= null;
    private ImageButton btnPlay= null;
    private ImageButton btnAnswer= null;
    private ImageButton btnNext= null;
    private Button btnCountDown = null;
    private Button btnScore = null;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    private boolean isRecording = false;
    private boolean isPlaying  = false;
    private boolean isPlayingAns  = false;
    private int themeID;
    ////private  String mFilename;
    private  String mFilename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/g9_result.3gp" ;
    private Timer mTimer;
    private  int timeCount = 15;
    private Handler mHandler;
    private int mLevel = 1;
    private int mQuestionNo =1;
    private int mGameScore =0;
    private boolean isNewLevel = true;
    private boolean isRecorded = false;
    private String themeName;


    public static GameNineFragment newInstance(int intTheme, String themeName, int intLevel) {
        Bundle bundle = new Bundle();
        bundle.putInt("intTheme", intTheme);
        bundle.putString("themeName", themeName);
        bundle.putInt("intLevel", intLevel);

        GameNineFragment gameNineFragment = new GameNineFragment();
        gameNineFragment.setArguments(bundle);
        GlobalValue.gLastGameId = 9;
        return gameNineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_gamenine, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_GAME);
        ((BaseActivity)activity).checkTurnOffMusic(); // Add By Rex

        themeID = getArguments().getInt("intTheme");
        themeName = getArguments().getString("themeName");
        mLevel = getArguments().getInt("intLevel");
        //init game by level


        setView(view);
       mHandler = new Handler(){
           @Override
           public void handleMessage(Message msg) {
               if(timeCount>0){
                   btnCountDown.setText(""+timeCount);
               }else{
                   stopRecording();
               }
           }
       };

        return view;
    }

    private void setView(View view) {
        imgQuestionBg = (ImageView) view.findViewById(R.id.img_question_bg);
        imgAngle = (ImageView)view.findViewById(R.id.img_angle);
        imgQuestionLeft = (ImageView) view.findViewById(R.id.img_question_left);
        imgQuestionRight = (ImageView) view.findViewById(R.id.img_question_right);
        btnRecord = (ImageButton) view.findViewById(R.id.btn_record);
        btnPlay = (ImageButton) view.findViewById(R.id.btn_play);
        btnAnswer = (ImageButton) view.findViewById(R.id.btn_ans);
        btnNext = (ImageButton) view.findViewById(R.id.btn_next);
        btnCountDown = (Button)view.findViewById(R.id.btn_countDown);
        btnScore = (Button)view.findViewById(R.id.btn_score);

        String imgBgStr = "g9_board_"+themeID;
        imgQuestionBg.setImageResource(getResources().getIdentifier(imgBgStr, "drawable",context.getPackageName()));
        String imgAngleStr = "g9_angel_t"+themeID;
        imgAngle.setImageResource(getResources().getIdentifier(imgAngleStr, "drawable",context.getPackageName()));


        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecorded = true;
                if(isRecording){
                    stopRecording();
                    isRecording = false;
                }else{
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                                0);

                    } else {
                        boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                        if (!hasPermission) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                        }else {
                            startRecording();
                            isRecording = true;
                        }
                    }
                    //startRecording();
                }

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    stopPlaying();
                }else{
                    startPlaying();
                }
                isPlaying = !isPlaying;
            }
        });

        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayingAns == true) {
                    stopPlayingAnswer();
                    isPlayingAns = false;
                    return;

                }else{
                    startPlayingAnswer(mLevel);
                    isPlayingAns = true;
                    return;
                }
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stopPlayingAnswer();
                int maxQuestionNo = 0;
                if(themeID ==1 && mLevel ==1) {
                    maxQuestionNo = 2;
                }else if(themeID ==1 && mLevel ==2) {
                    maxQuestionNo = 8;
                }else if(themeID ==2 && mLevel ==1) {
                    maxQuestionNo = 4;
                }else if(themeID ==2 && mLevel ==2) {
                    maxQuestionNo = 6;
                }else if(themeID ==3 && mLevel ==1) {
                    maxQuestionNo = 2;
                }else if(themeID ==3 && mLevel ==2) {
                    maxQuestionNo = 8;
                }else if(themeID ==4 && mLevel ==1) {
                    maxQuestionNo = 2;
                }else if(themeID ==4 && mLevel ==2) {
                    maxQuestionNo = 8;
                }else if(themeID ==5 && mLevel ==1) {
                    maxQuestionNo = 3;
                }else if(themeID ==5 && mLevel ==2) {
                    maxQuestionNo = 7;
                }else if(themeID ==6 && mLevel ==1) {
                    maxQuestionNo = 3;
                }else if(themeID ==6 && mLevel ==2) {
                    maxQuestionNo = 7;
                }

                if(mQuestionNo <maxQuestionNo) {
                    setGameStage(mLevel, ++mQuestionNo);
                }else{
                    myFragment.getContainer().setFragmentChild(ScoreFragment.newInstance(themeID, themeName, 9, false, mGameScore, mLevel));
                }
            }
        });
        setGameStage(mLevel, 1);
        if(mLevel==1) {
            startPlayingIntro();
        }else{
            stopPlayingIntro();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }else {
                    startRecording();
                    isRecording = true;
                }
            }else{
                //User denied Permission.
            }
        }else  if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
                isRecording = true;
            }
        }
    }

    private void setGameStage(int level, int questionNo){
        //Added By Rex
        isRecorded = false;
        isNewLevel = true;
        mLevel = level;
        mQuestionNo = questionNo;
        String imgLeftName = "g9_t"+themeID+"_l"+ level + "_"+questionNo + "i";
        imgQuestionLeft.setImageResource(getResources().getIdentifier(imgLeftName, "drawable",context.getPackageName()));
        String imgRightName = "g9_t"+themeID+"_l"+ level + "_"+questionNo + "t";
        imgQuestionRight.setImageResource(getResources().getIdentifier(imgRightName, "drawable",context.getPackageName()));

        btnPlay.setEnabled(false);
        btnPlay.setImageResource(R.drawable.g9_play_dis);
    }


    private void startRecording() {

        ////mFilename = Environment.getExternalStorageDirectory().getAbsolutePath();
        ////mFilename += "/g9_result.3gp";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFilename);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        //button status
        btnRecord.setEnabled(true);

        //Comment By Rex
//        if ( mFilename.isEmpty() ) {
//            btnPlay.setEnabled(false);
//        }
        //Add By Rex
        btnPlay.setEnabled(false);

        btnAnswer.setEnabled(false);
        btnNext.setEnabled(false);
        btnRecord.setImageResource(R.drawable.g9_rec_stop);
        btnPlay.setImageResource(R.drawable.g9_play_dis);
        btnAnswer.setImageResource(R.drawable.g9_ans_dis);
        btnNext.setImageResource(R.drawable.g9_next_dis);
        btnCountDown.setVisibility(View.VISIBLE);

        try {
            mRecorder.prepare();
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    Message msg = mHandler.obtainMessage();
                    msg.what = timeCount;
                    msg.sendToTarget();
                    timeCount --;
                    Log.d("timer"," count : " + timeCount);

                }
            },0,1000);
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        isRecording = false;
        if(mRecorder!= null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }

        timeCount = 15;
        if(mTimer!=null) {
            mTimer.cancel();
            mTimer = null;
        }

        if(isNewLevel) {
            if(mLevel ==1) {
                mGameScore += 10;
            }else{
                mGameScore += 20;
            }
            isNewLevel = false;
            btnScore.setText(mGameScore+"");
        }

        btnRecord.setEnabled(true);
        //Commented by Rex
//        if ( !mFilename.isEmpty() ) {
//            btnPlay.setEnabled(true);
//        }
        //Added By Rex
        if (isRecorded){
            btnPlay.setEnabled(true);
            btnPlay.setImageResource(R.drawable.g9_play);
        }
        else
        {
            btnPlay.setEnabled(false);
            btnPlay.setImageResource(R.drawable.g9_play_dis);
        }

        btnAnswer.setEnabled(true);
        btnNext.setEnabled(true);
        btnRecord.setImageResource(R.drawable.g9_rec_on);
        //Commented by Rex
//        btnPlay.setImageResource(R.drawable.g9_play);
        btnAnswer.setImageResource(R.drawable.g9_ans);
        if(isNewLevel){
            btnNext.setImageResource(R.drawable.g9_next_dis);
        }else{
            btnNext.setImageResource(R.drawable.g9_next);
        }

        btnCountDown.setVisibility(View.INVISIBLE);


    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    stopPlaying();
                    isPlaying = false;
                    return;
                }else{
                    startPlaying();
                    isPlaying = true;
                    return;
                }
            }
        });

        try {
            mPlayer.setDataSource(mFilename);
            mPlayer.prepare();
            mPlayer.start();

            //button status

            //Comment by Rex
//            btnRecord.setEnabled(false);
//            if ( !mFilename.isEmpty() ) {
//                btnPlay.setEnabled(true);
//            }
            btnPlay.setEnabled(false);
            //Added By Rex

            btnAnswer.setEnabled(false);
            btnNext.setEnabled(false);
            btnRecord.setImageResource(R.drawable.g9_rec_dis);

            //Commented By Rex
//            btnPlay.setImageResource(R.drawable.g9_stop);
            btnAnswer.setImageResource(R.drawable.g9_ans_dis);
            btnNext.setImageResource(R.drawable.g9_next_dis);

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();
            }
        });
    }

    private void stopPlaying() {
        isPlaying = false;
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        //button status
        btnRecord.setEnabled(true);

        //Added By Rex
        if (isRecorded)
        {
            btnPlay.setEnabled(true);
            btnPlay.setImageResource(R.drawable.g9_play);
        }
        else
        {
            btnPlay.setEnabled(false);
            btnPlay.setImageResource(R.drawable.g9_play_dis);
        }

        //Commented by Rex
//        btnPlay.setEnabled(!isNewLevel);
        btnAnswer.setEnabled(true);
        btnNext.setEnabled(true);
        btnRecord.setImageResource(R.drawable.g9_rec_on);

        btnAnswer.setImageResource(R.drawable.g9_ans);
        btnNext.setImageResource(R.drawable.g9_next);

        //Comment By Rex
//        if(isNewLevel){
//            btnPlay.setImageResource(R.drawable.g9_play_dis);
//        }else{
//            btnPlay.setImageResource(R.drawable.g9_play);
//        }
    }

    private void startPlayingIntro(){
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlayingIntro();
            }
        });
        try {
            String path = "gamedata/game9/vo/VO_G9_intro.mp3";

            Log.d("vo_path", "vo path:" + path);
            AssetFileDescriptor descriptor = context.getAssets().openFd(path);
            mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            //mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();

            //button status
            btnRecord.setEnabled(false);
            btnPlay.setEnabled(false);
            btnAnswer.setEnabled(false);
            btnNext.setEnabled(false);
            btnRecord.setImageResource(R.drawable.g9_rec_dis);
            btnPlay.setImageResource(R.drawable.g9_play_dis);
            btnAnswer.setImageResource(R.drawable.g9_ans_dis);
            btnNext.setImageResource(R.drawable.g9_next_dis);

        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    private void stopPlayingIntro() {

        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        //button status
        btnRecord.setEnabled(true);
        btnPlay.setEnabled(false);
        btnAnswer.setEnabled(true);
        btnNext.setEnabled(true);
        btnRecord.setImageResource(R.drawable.g9_rec_on);
        btnPlay.setImageResource(R.drawable.g9_play_dis);
        btnAnswer.setImageResource(R.drawable.g9_ans);
        btnNext.setImageResource(R.drawable.g9_next);
    }

    private void startPlayingAnswer(int level){
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();
            }
        });
        try {
            //String path = "file:///android_asset/gamedata/game9/vo/VO_G9_T"+themeID+"_L"+level+"_1a.mp3";
            String path = "gamedata/game9/vo/VO_G9_T"+themeID+"_L"+level+"_"+mQuestionNo+"a.mp3";


            Log.d("vo_path", "vo path:" + path);
            AssetFileDescriptor descriptor = context.getAssets().openFd(path);
            mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            //mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();

            //button status
            btnRecord.setEnabled(false);
            btnPlay.setEnabled(false);
            btnAnswer.setEnabled(true);
            btnNext.setEnabled(false);
            btnRecord.setImageResource(R.drawable.g9_rec_dis);
            btnPlay.setImageResource(R.drawable.g9_play_dis);
            btnAnswer.setImageResource(R.drawable.g9_stop);
            btnNext.setImageResource(R.drawable.g9_next_dis);

        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    private void stopPlayingAnswer() {
        isPlayingAns =false;
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        //button status
        btnRecord.setEnabled(true);

        //Commented By Rex
//        btnPlay.setEnabled(false);
//        if (!mFilename.isEmpty()) {
//        btnPlay.setEnabled(true);
//        }

        //Added By Rex
        if (isRecorded)
        {
            btnPlay.setEnabled(true);
            btnPlay.setImageResource(R.drawable.g9_play);
        }
        else
        {
            btnPlay.setEnabled(false);
            btnPlay.setImageResource(R.drawable.g9_play_dis);
        }

        btnAnswer.setEnabled(true);
        btnNext.setEnabled(true);
        btnRecord.setImageResource(R.drawable.g9_rec_on);
        //Commented By Rex
//        btnPlay.setImageResource(R.drawable.g9_play);
        btnAnswer.setImageResource(R.drawable.g9_ans);
        btnNext.setImageResource(R.drawable.g9_next);

    }


    @Override
    public void showGameIntro() {
        myFragment.getContainer().removeLastFragment();
        myFragment.getContainer().setFragmentChild(GameIntroFragment.newInstance(themeID, "", 9, 0));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}