package com.sahk.earlyliteracy.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import java.io.File;

import com.sahk.earlyliteracy.BaseActivity;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.constants.GlobalValue;

public class WebGameFragment extends BaseFragment implements BaseActivity.GetWebGameFragment {

    private int gameID;
    private int themeID;
    private String themeName;
    private String currentUrl;
    private WebView webViewGame;
    private RelativeLayout relativeLayoutLoading; //Added By Rex
    private Handler handler = new Handler(); //Added By Rex

    public static WebGameFragment newInstance(int themeID, String themeName, int gameID, int level) {
        myFragment.getContainer().setGameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        bundle.putInt("gameID", gameID);
        bundle.putInt("level", level);
        WebGameFragment webGameFragment = new WebGameFragment();
        webGameFragment.setArguments(bundle);
        GlobalValue.gLastGameId = gameID;
        return webGameFragment;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_webgame, viewGroup, false);
        ((BaseActivity)activity).checkTurnOffMusic(); // Add By Rex
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_GAME);
        gameID = getArguments().getInt("gameID");
        themeID = getArguments().getInt("themeID");
        themeName = getArguments().getString("themeName");
        //Added By Rex
        relativeLayoutLoading = (RelativeLayout) view.findViewById(R.id.relativeLayoutLoading);

        webViewGame = (WebView) view.findViewById(R.id.webViewGame);
        webViewGame.getSettings().setSupportZoom(false);
        webViewGame.getSettings().setJavaScriptEnabled(true);
        //webViewGame.getSettings().setDomStorageEnabled(true);
        //webViewGame.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }

//        webViewGame.setDrawingCacheEnabled(true);
//        webViewGame.getSettings().setAllowContentAccess(true);
//        webViewGame.getSettings().setAllowFileAccess(true);
        webViewGame.setWebChromeClient(new WebChromeClient());

        webViewGame.clearCache(true);
        webViewGame.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!currentUrl.equals(url)) {
                    //if(!url.contains("about:blank")){
                        myFragment.getContainer().setFragmentChild(ScoreFragment.newInstance(themeID, themeName, gameID, false, Integer.parseInt(url.split("totalScore=")[1]), Integer.parseInt(url.split("level=")[1].split("&")[0])));
                    //}

                }
                //if(webViewGame!=null){
                    webViewGame.loadUrl("javascript:playIntro();");
                //}
            }
        });
        File file = new File(activity.getFilesDir() + tableGame.get(gameID).getGamePath() + tableTheme.get(themeID, gameID).getThemePath());
        currentUrl = "file://" + file + "?level=" + getArguments().getInt("level");
        webViewGame.loadUrl(currentUrl);

        Log.d("gameView","view hardware accelerated: " + webViewGame.isHardwareAccelerated());

        //Added By Rex
        ((BaseActivity)activity).setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
        handler.postDelayed(runnable, 3000);

        return view;
    }

    //Added By Rex
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ((BaseActivity)activity).setToolbarMode(Config.TOOLBAR_STATUS_GAME);
            webViewGame.setVisibility(View.VISIBLE);
            relativeLayoutLoading.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        webViewGame.destroy();
        //destroyWebView();
    }

    private void destroyWebView() {
        //mWebViewParent.removeAllViews();
        if(webViewGame != null) {
            webViewGame.clearHistory();
            webViewGame.clearCache(true);
            //webViewGame.loadUrl("about:blank");
            webViewGame.freeMemory();
            webViewGame.pauseTimers();
            webViewGame.destroy();
            //webViewGame = null;
        }
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showGameIntro() {
        myFragment.getContainer().removeLastFragment();
        myFragment.getContainer().setFragmentChild(GameIntroFragment.newInstance(themeID, themeName, gameID, getArguments().getInt("level")));
    }
}