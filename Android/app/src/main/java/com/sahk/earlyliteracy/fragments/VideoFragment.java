package com.sahk.earlyliteracy.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.sahk.earlyliteracy.BaseActivity;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.utils.CommonUtil;
import com.sahk.earlyliteracy.widgets.CustomTextView;

import static com.sahk.earlyliteracy.applications.Config.SERVER_BASE_URL;
import static com.sahk.earlyliteracy.applications.Config.VIDEO_JQANDA;
import static com.sahk.earlyliteracy.applications.Config.VIDEO_SONG;
import static com.sahk.earlyliteracy.applications.Config.VIDEO_SQANDAA;
import static com.sahk.earlyliteracy.applications.Config.VIDEO_THEME;

public class VideoFragment extends BaseFragment implements BaseActivity.GetVideoFragment {

    private boolean isDownloading = false;
    private boolean isFirstTime;
    private CustomTextView customTextViewDownload;
    private DownloadVideo downloadVideo;
    private MediaPlayer mediaPlayer;
    private int themeID, videoType;
    private ProgressBar progressBarDownload;
    private LinearLayout linearLayoutDownload;
    private String themeName;
    private VideoView videoView;

    public static VideoFragment newInstance(int themeID, String themeName, int videoType, boolean isFirstTime) {
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        bundle.putInt("videoType", videoType);
        bundle.putBoolean("isFirstTime", isFirstTime);
        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_video, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_VIDEO);
        downloadVideo = new DownloadVideo();
        mediaPlayer = new MediaPlayer();
        themeID = getArguments().getInt("themeID");
        themeName = getArguments().getString("themeName");
        videoType = getArguments().getInt("videoType");
        isFirstTime = getArguments().getBoolean("isFirstTime");
        setView(view);
        setListener();
        checkVideo();
        ((BaseActivity)activity).checkTurnOffMusic(); // Add By Rex
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (downloadVideo != null && isDownloading && downloadVideo.getStatus() != AsyncTask.Status.FINISHED) {
            downloadVideo.cancel(true);
        }
    }

    private void setView(View view) {
        customTextViewDownload = (CustomTextView) view.findViewById(R.id.customTextViewDownload);
        linearLayoutDownload = (LinearLayout) view.findViewById(R.id.linearLayoutDownload);
        progressBarDownload = (ProgressBar) view.findViewById(R.id.progressBarDownload);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(activity);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayerPrepared) {
                mediaPlayer = mediaPlayerPrepared;
                if (sharedPreferences.getBoolean("sound", false)) {
                    mediaPlayer.setVolume(1f, 1f);
                } else {
                    mediaPlayer.setVolume(0f, 0f);
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isFirstTime) {
                    sharedPreferences.edit().putBoolean(themeID + "_first", true).apply();
                    myFragment.getContainer().removeLastFragment();
                    myFragment.getContainer().setFragmentChild(MenuFragment.newInstance(themeID, themeName));
                } else {
                    myFragment.getContainer().onBackPressed();
                }
            }
        });
    }

    private String mVideoFile;

    private void checkVideo() {
        mVideoFile = null;
        switch (videoType) {
            case VIDEO_THEME:
                mVideoFile = "theme" + themeID + ".mp4";
                break;
            case VIDEO_SONG:
                mVideoFile = "theme" + themeID + "_song.mp4";
                break;
            case VIDEO_JQANDA:
                mVideoFile = "theme" + themeID + "_jr_qanda.mp4";
                break;
            case VIDEO_SQANDAA:
                mVideoFile = "theme" + themeID + "_sr_qanda.mp4";
                break;
        }
        File folder = new File(activity.getFilesDir() + File.separator + "video");
        File file = new File(folder + File.separator + mVideoFile);
        if (!file.exists()) {
            if (folder.exists() || folder.mkdirs()) {
                if(CommonUtil.checkNetworkConnected(context)) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setCancelable(false);   //Added By Rex
                    adb.setMessage("下載遊戲或影片會使用數據，您是否確定要下載？");
                    adb.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            File folder = new File(activity.getFilesDir() + File.separator + "video");
                            File file = new File(folder + File.separator + mVideoFile);
                            downloadVideo.execute(file.toString(), mVideoFile);

                            if (!sharedPreferences.getBoolean("sound", false)) {
                                new android.app.AlertDialog.Builder(activity)
                                        .setCancelable(false)   //Added By Rex
                                        .setMessage("程式現設定為靜音，請點擊【音效】以開啟")
                                        .setCancelable(false)
                                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                sharedPreferences.edit().putBoolean("sound", true).apply();
                                                ImageButton imageButtonSound = (ImageButton) activity.findViewById(R.id.imageButtonSound);
                                                imageButtonSound.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.button_sound_on));
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }
                    });
                    adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            //Added By Rex
                            skipVideo();
                        }
                    });
                    AlertDialog alertDialog = adb.create();
                    alertDialog.show();
                }else {
                    android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(getActivity());
                    adb.setCancelable(false);   //Added By Rex
                    adb.setMessage("首次進入時需下載遊戲或影片經流動網絡下載可能產生額外費用，建議以wifi連線下載。是否要連線？");
                    adb.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
                    adb.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            //Added By Rex
                            skipVideo();
                        }
                    });
                    android.app.AlertDialog alertDialog = adb.create();
                    alertDialog.show();
                }


            }
        } else {
            videoView.setVideoPath(file.toString());
            videoView.start();
        }
    }

    @Override
    public void setSound() {
        if (mediaPlayer != null) {
            if (sharedPreferences.getBoolean("sound", true)) {
                mediaPlayer.setVolume(1f, 1f);
            } else {
                mediaPlayer.setVolume(0f, 0f);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void skipVideo() {
        if (downloadVideo != null && isDownloading && downloadVideo.getStatus() != AsyncTask.Status.FINISHED) {
            downloadVideo.cancel(true);
        } else {
            sharedPreferences.edit().putBoolean("theme" + themeID + "_first", false).apply();
        }
        if (isFirstTime) {
            myFragment.getContainer().removeLastFragment();
            myFragment.getContainer().setFragmentChild(MenuFragment.newInstance(themeID, themeName));
        } else {
            myFragment.getContainer().onBackPressed();
        }
    }

    private class DownloadVideo extends AsyncTask<String, String, String> {

        private FileOutputStream fileOutputStream;
        private InputStream inputStream;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDownloading = true;
            linearLayoutDownload.setVisibility(View.VISIBLE);
            progressBarDownload.setMax(100);
        }

        @Override
        protected String doInBackground(String... input) {
            try {
                URL url = new URL(SERVER_BASE_URL + "/video/" + input[1]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                inputStream = new BufferedInputStream(url.openStream(), 8192);
                fileOutputStream = new FileOutputStream(new File(input[0] + ".tmp"));
                byte data[] = new byte[1024];
                int countSize;
                int fileSize = urlConnection.getContentLength();
                long currentSize = 0;
                while ((countSize = inputStream.read(data)) != -1) {
                    currentSize += countSize;
                    publishProgress(String.valueOf(currentSize * 100 / fileSize));
                    fileOutputStream.write(data, 0, countSize);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return input[0];
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            customTextViewDownload.setText("正在下載影片( " + progress[0] + " %)");
            progressBarDownload.setProgress(Integer.parseInt(progress[0]));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(String output) {
            File file = new File(output + ".tmp");
            linearLayoutDownload.setVisibility(View.GONE);
            isDownloading = false;
            if (file.renameTo(new File(output))) {
                checkVideo();
            }
        }
    }
}