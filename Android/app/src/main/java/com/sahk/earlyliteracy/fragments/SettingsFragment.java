package com.sahk.earlyliteracy.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.HitBuilders;

import com.sahk.earlyliteracy.BaseActivity;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.utils.CommonUtil;

public class SettingsFragment extends BaseFragment {

    private Button buttonFacebookShare;
    private CallbackManager callbackManager;
    private RadioButton radioButtonFontSmall, radioButtonFontNormal, radioButtonFontLarge;
    private ShareDialog shareDialog;
    private TextView textViewDisclaimer, textViewHowToUse, textViewPrivacy, textViewSAHK, textViewSurvey,
                     textViewParent, textViewBrowse, textViewCopyright ;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_settings, viewGroup, false);

        //Comment By Rex
//        Log.i("TAG", "Setting screen name: " + "SettingsFragment");
//        ((BaseActivity)activity).tracker.setScreenName("Image~" + "SettingsFragment");
//        ((BaseActivity)activity).tracker.send(new HitBuilders.ScreenViewBuilder().build());
//        ((BaseActivity)activity).tracker.send(new HitBuilders.EventBuilder()
//                .setCategory("Action")
//                .setAction("Go to Setting Page")
//                .build());

        float desity =  getResources().getDisplayMetrics().density;
        float width = getResources().getDisplayMetrics().widthPixels;
        float scaledFactor = (width/desity)/640;
        float fontSize = scaledFactor * sharedPreferences.getInt("fontSize", 18);

        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_SETTINGS);
        buttonFacebookShare = (Button) view.findViewById(R.id.buttonFacebookShare);
        radioButtonFontSmall = (RadioButton) view.findViewById(R.id.radioButtonFontSmall);
        radioButtonFontNormal = (RadioButton) view.findViewById(R.id.radioButtonFontNormal);
        radioButtonFontLarge = (RadioButton) view.findViewById(R.id.radioButtonFontLarge);
        TextView textViewFontSize = (TextView) view.findViewById(R.id.textViewFontSize);
        TextView textViewFacebookShare = (TextView) view.findViewById(R.id.textViewFacebookShare);
        textViewDisclaimer = (TextView) view.findViewById(R.id.textViewDisclaimer);
        textViewHowToUse = (TextView) view.findViewById(R.id.textViewHowToUse);
        textViewPrivacy = (TextView) view.findViewById(R.id.textViewPrivacy);
        textViewSAHK = (TextView) view.findViewById(R.id.textViewSAHK);
        textViewSurvey = (TextView) view.findViewById(R.id.textViewSurvey);
        textViewParent = (TextView) view.findViewById(R.id.textViewParent);
        textViewBrowse = (TextView) view.findViewById(R.id.textViewBrowse);
        textViewCopyright = (TextView) view.findViewById(R.id.textViewCopyright);

        buttonFacebookShare.setTypeface(typefaceManager.getYoungMedium());
        textViewFontSize.setTypeface(typefaceManager.getYoungBold());
        textViewFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textViewFacebookShare.setTypeface(typefaceManager.getYoungBold());
        textViewFacebookShare.setTextSize(TypedValue.COMPLEX_UNIT_SP,fontSize);
        textViewDisclaimer.setTypeface(typefaceManager.getYoungBold());
        textViewDisclaimer.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textViewHowToUse.setTypeface(typefaceManager.getYoungBold());
        textViewHowToUse.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textViewPrivacy.setTypeface(typefaceManager.getYoungBold());
        textViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textViewSAHK.setTypeface(typefaceManager.getYoungBold());
        textViewSAHK.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textViewSurvey.setTypeface(typefaceManager.getYoungBold());
        textViewSurvey.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textViewParent.setTypeface(typefaceManager.getYoungBold());
        textViewParent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textViewBrowse.setTypeface(typefaceManager.getYoungBold());
        textViewBrowse.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textViewCopyright.setTypeface(typefaceManager.getYoungBold());
        textViewCopyright.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);


        setListener();
        switch (sharedPreferences.getInt("fontSize", 18)) {
            case 14:
                radioButtonFontSmall.setChecked(true);
                break;
            case 18:
                radioButtonFontNormal.setChecked(true);
                break;
            case 24:
                radioButtonFontLarge.setChecked(true);
                break;
            default:
                radioButtonFontNormal.setChecked(true);
                break;
        }
        return view;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        buttonFacebookShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonUtil.checkNetworkConnected(context)) {
                    shareDialog = new ShareDialog(getActivity());
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse("http://sahk.kanhan.com/download.html")) //Edited By Rex
                                .setImageUrl(Uri.parse( Config.SERVER_BASE_URL+"facebook/bg01.png" )) //Edited By Rex
                                .setContentTitle("香港耀能協會 - 學前語文秘笈")
                                .build();
                        shareDialog.show(linkContent);
                    }
//                    .setImageUrl(Uri.parse(Config.SERVER_BASE_URL + "facebook/bg01.png" )) //Edited By Rex
                }else{
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setCancelable(false);   //Added By Rex
                    adb.setMessage("需連結網絡方能分享。是否要連線？");
                    adb.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
                    adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = adb.create();
                    alertDialog.show();
                }
            }
        });
        radioButtonFontSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putInt("fontSize", 14).apply();
                radioButtonFontSmall.setChecked(true);
                myFragment.getContainer().removeLastFragment();
                myFragment.getContainer().setFragmentChild(SettingsFragment.newInstance());
            }
        });
        radioButtonFontNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putInt("fontSize", 18).apply();
                radioButtonFontNormal.setChecked(true);
                myFragment.getContainer().removeLastFragment();
                myFragment.getContainer().setFragmentChild(SettingsFragment.newInstance());
            }
        });
        radioButtonFontLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putInt("fontSize", 24).apply();
                radioButtonFontLarge.setChecked(true);
                myFragment.getContainer().removeLastFragment();
                myFragment.getContainer().setFragmentChild(SettingsFragment.newInstance());
            }
        });
        textViewDisclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(2));
                myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(2));
            }
        });
        textViewHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(TutorialFragment.newInstance());
            }
        });
        textViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(1));
                myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(1));
            }
        });
        textViewSAHK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(0));
                myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(0));
            }
        });
        textViewSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(SurveyFragment.newInstance());
            }
        });

        textViewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Commented By Rex
//                myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(3));
                //Added By Rex
                myFragment.getContainer().setFragmentChild(ParentFragment.newInstance());
            }
        });

        textViewBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(4));
            }
        });

        textViewCopyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().setFragmentChild(SettingsContentFragment.newInstance(5));
            }
        });


    }
}