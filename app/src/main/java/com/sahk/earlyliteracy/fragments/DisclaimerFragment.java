package com.sahk.earlyliteracy.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.utils.CommonUtil;

public class DisclaimerFragment extends BaseFragment {

    private CheckBox checkBoxShowDisclaimer;
    private ImageButton imageButtonAgree, imageButtonClose;
    private RelativeLayout relativeLayoutAD;
    private WebView webViewDisclaimer;
    private WebView webViewAd;

    public static DisclaimerFragment newInstance() {
        return new DisclaimerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_disclaimer, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
        setView(view);
        setWebView();
        setListener();

        if (!sharedPreferences.getBoolean("show_disclaimer", true)) {
            showAD();
        }
        return view;
    }

    private void setView(View view) {
        checkBoxShowDisclaimer = (CheckBox) view.findViewById(R.id.checkBoxShowDisclaimer);
        imageButtonAgree = (ImageButton) view.findViewById(R.id.imageButtonAgree);
        imageButtonClose = (ImageButton) view.findViewById(R.id.imageButtonClose);
        relativeLayoutAD = (RelativeLayout) view.findViewById(R.id.relativeLayoutAD);
        webViewDisclaimer = (WebView) view.findViewById(R.id.webViewDisclaimer);
        webViewAd = (WebView) view.findViewById(R.id.wvAdv);
        checkBoxShowDisclaimer.setTypeface(typefaceManager.getYoungMedium());
    }



    private void setListener() {
        imageButtonAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAD();
                sharedPreferences.edit().putBoolean("show_disclaimer", !checkBoxShowDisclaimer.isChecked()).apply();
            }
        });
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(ThemeFragment.newInstance());
            }
        });
    }

    private void setWebView() {
        webViewDisclaimer.setBackgroundColor(0);
        webViewDisclaimer.getBackground().setAlpha(0);
        Resources res = getResources();
        float desity =  res.getDisplayMetrics().density;
        float width = res.getDisplayMetrics().widthPixels;
        float scaledWidth = width/desity;

        Log.d("fonts","desity: " + desity + " ,  width: " + width + " , sw: " + scaledWidth);

        float fontSize = res.getDimension(R.dimen.web_font_medium_sp);
        fontSize = fontSize* (scaledWidth/640)*(scaledWidth/640);

        Log.d("font","font size disclaimer: "+ fontSize);
        webViewDisclaimer.getSettings().setDefaultFontSize((int)fontSize);
        webViewDisclaimer.loadUrl("file:///android_asset/disclaimer.html");
    }

    @SuppressWarnings("unchecked")
    private void showAD() {
        if (Config.AD_SHOW.equalsIgnoreCase("Y") && CommonUtil.checkNetworkConnected(activity)) {
            myFragment.setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
            //Edited By Rex
            String html = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body style=\"background-color:transparent;\">\n" +
                    "<div >"+
                    "<a href=\""+Config.AD_URL+"\">"+
                    "<img src=\""+Config.AD_IMG+"\" style=\"width:100%;height:auto;\">"+
                    "</a>" +
                    "</div>\n" +
                    "</body></html>";
            webViewAd.loadData(html,  "text/html", "UTF-8");
//            webViewAd.getSettings().setUseWideViewPort(true);
//            webViewAd.getSettings().setLoadWithOverviewMode(true);
//            webViewAd.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//            webViewAd.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            webViewAd.setScrollbarFadingEnabled(false);
            webViewAd.setBackgroundColor(Color.TRANSPARENT);
            relativeLayoutAD.setVisibility(View.VISIBLE);
        } else {
            myFragment.getContainer().setFragmentChild(ThemeFragment.newInstance());
        }
    }
}