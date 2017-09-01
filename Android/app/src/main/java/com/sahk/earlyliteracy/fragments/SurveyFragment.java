package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import java.util.List;

import com.google.android.gms.analytics.HitBuilders;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.adapters.SurveyAdapter;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.database.ItemScore;
import com.sahk.earlyliteracy.database.ItemSurvey;

public class SurveyFragment extends BaseFragment {

    private ImageButton imageButtonClose;
    private ImageButton imageButtonNext;
    private ViewPager viewPager;
    private SurveyAdapter adapter;

    public static SurveyFragment newInstance() {
        return new SurveyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_survey, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_HIDE);
        imageButtonClose = (ImageButton) view.findViewById(R.id.imageButtonClose);
        imageButtonNext = (ImageButton) view.findViewById(R.id.imageButtonNext);
        imageButtonNext.setEnabled(false);
        imageButtonNext.setImageResource(R.drawable.continue_on);
//        WebView webView = (WebView) view.findViewById(R.id.webView);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("https://goo.gl/forms/pQpkeAOO4RtzJywu1");
        setListener();
        viewPager = (ViewPager)view.findViewById(R.id.viewPagerSurvey);
        adapter = new SurveyAdapter(getActivity());
        adapter.setSurveyAdapterListener(new SurveyAdapter.SurveyAdapterListener() {
            @Override
            public void onItemUpdated(boolean enableNextBtn) {
                    if(viewPager.getCurrentItem()>=3) {
                        if (enableNextBtn) {
                            imageButtonNext.setImageResource(R.drawable.button_submit);
                            imageButtonNext.setContentDescription("提交");
                        }else{
                            imageButtonNext.setImageResource(R.drawable.button_submit_pressed);
                            imageButtonNext.setContentDescription("提交");
                        }
                    }else{
                        if (enableNextBtn) {
                            imageButtonNext.setImageResource(R.drawable.button_continue);
                            imageButtonNext.setContentDescription("繼續");
                        }else{
                            imageButtonNext.setImageResource(R.drawable.continue_on);
                            imageButtonNext.setContentDescription("繼續");
                        }
                    }
                imageButtonNext.setEnabled(enableNextBtn);
            }
        });
        viewPager.setAdapter(adapter);
        return view;
    }

    private void setListener() {
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.getContainer().onBackPressed();
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mGaTracker!=null) {
                    int numQues = viewPager.getCurrentItem() + 1;
                    if (!sharedPreferences.getBoolean("survey_ga_" + String.valueOf(numQues) , false)) {

                        View curView = viewPager.findViewWithTag("pos" +  String.valueOf(numQues) );
                        if (numQues == 1 || numQues == 2 || numQues == 3 )
                        {
                            RadioButton opt1 = (RadioButton)curView.findViewById(R.id.surveyOpt1);
                            RadioButton opt2 = (RadioButton)curView.findViewById(R.id.surveyOpt2);
                            RadioButton opt3 = (RadioButton)curView.findViewById(R.id.surveyOpt3);
                            RadioButton opt4 = (RadioButton)curView.findViewById(R.id.surveyOpt4);

                            String ans = "1";
                            if ( opt1.isChecked())
                            {
                                ans = "1";
                            }
                            else if ( opt2.isChecked())
                            {
                                ans = "2";
                            }
                            else if ( opt3.isChecked())
                            {
                                ans = "3";
                            }
                            else if ( opt4.isChecked())
                            {
                                ans = "4";
                            }

                            mGaTracker.setScreenName("Survey Screen");
                            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                            mGaTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("6_Survey_Page")
                                    .setAction("Ques" + String.valueOf(numQues) )
                                    .setLabel( ans )
                                    .build());
                        }
                        else
                        if (numQues == 4 )
                        {
                            CheckBox opt1 = (CheckBox)curView.findViewById(R.id.surveyOpt1);
                            CheckBox opt2 = (CheckBox)curView.findViewById(R.id.surveyOpt2);
                            CheckBox opt3 = (CheckBox)curView.findViewById(R.id.surveyOpt3);
                            CheckBox opt4 = (CheckBox)curView.findViewById(R.id.surveyOpt4);

                            String ans1 = "N", ans2 = "N", ans3 = "N", ans4 = "N";
                            if ( opt1.isChecked())
                            {
                                ans1 = "Y";
                            }
                            if ( opt2.isChecked())
                            {
                                ans2 = "Y";
                            }
                            if ( opt3.isChecked())
                            {
                                ans3 = "Y";
                            }
                            if ( opt4.isChecked())
                            {
                                ans4 = "Y";
                            }

                            mGaTracker.setScreenName("Survey Screen");
                            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                            mGaTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("6_Survey_Page")
                                    .setAction("Ques" + String.valueOf(numQues) + "a" )
                                    .setLabel( ans1 )
                                    .build());
                            mGaTracker.setScreenName("Survey Screen");
                            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                            mGaTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("6_Survey_Page")
                                    .setAction("Ques" + String.valueOf(numQues) + "b" )
                                    .setLabel( ans2 )
                                    .build());
                            mGaTracker.setScreenName("Survey Screen");
                            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                            mGaTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("6_Survey_Page")
                                    .setAction("Ques" + String.valueOf(numQues) + "c" )
                                    .setLabel( ans3 )
                                    .build());
                            mGaTracker.setScreenName("Survey Screen");
                            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                            mGaTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("6_Survey_Page")
                                    .setAction("Ques" + String.valueOf(numQues) + "d" )
                                    .setLabel( ans4 )
                                    .build());
                        }
                        else
                        if (numQues == 5 )
                        {
                            EditText txtAns = (EditText)curView.findViewById(R.id.surveyAnswer);
                            mGaTracker.setScreenName("Survey Screen");
                            mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());
                            mGaTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("6_Survey_Page")
                                    .setAction("Ques" + String.valueOf(numQues) )
                                    .setLabel( txtAns.getText().toString() )
                                    .build());
                        }

                        sharedPreferences.edit().putBoolean( "survey_ga_" + String.valueOf(numQues) , true).apply();
                    }
                }


                if(viewPager.getCurrentItem()==4){
                    List<ItemSurvey> list = adapter.getSurveyItemList();
                    //TODO: send the survey result to google play

                    //Added by Rex
                    //Do not have any Place to Catch theme Count
                    if (!sharedPreferences.getBoolean("is_ans_survey", false)) {
                        sharedPreferences.edit().putBoolean("is_ans_survey", true).apply();

                        // Counted to Reward
                        for (int i = 1; i <= 6 ; i++) {
                            if (tableTheme.isFinishedAllBasicGame(i)) {
                                ItemScore itemScore = new ItemScore();
                                itemScore.setThemeID(i);
                                if (tableScore.get(i) == null) {
                                    itemScore.setScore(200);
                                    tableScore.insert(itemScore);
                                } else {
                                    itemScore.setScore(tableScore.get(i).getScore() + 200 );
                                    tableScore.update(itemScore);
                                }
                            }
                        }
                    }
                    myFragment.getContainer().onBackPressed();
                } else {
                    if (viewPager.getCurrentItem() == 3) {
                        imageButtonNext.setImageResource(R.drawable.button_submit);
                    } else if (viewPager.getCurrentItem() == 2) {
                        imageButtonNext.setImageResource(R.drawable.continue_on);
                        imageButtonNext.setEnabled(false);
                    } else {
                        imageButtonNext.setImageResource(R.drawable.button_continue);
                        imageButtonNext.setEnabled(true);
                    }

                    int nextPosition = getItem(+1);
                    if (nextPosition >= 4) {
                        nextPosition = 4;
                    }
                    viewPager.setCurrentItem(nextPosition, true);
                    adapter.setCurrentPosition(nextPosition);
                    //Added by Rex
                    if (nextPosition < 4) {
                        imageButtonNext.setEnabled(false);
                    }
                }
            }
        });
    }
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}