package com.sahk.earlyliteracy.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.constants.Collection;
import com.sahk.earlyliteracy.database.ItemSurvey;

public class SurveyAdapter extends PagerAdapter {

    public Context context; //Added By Rex
    float fontSize;

    public interface SurveyAdapterListener{
        public void onItemUpdated(boolean enableNextBtn);
    }

    private LayoutInflater layoutInflater;
    private List<ItemSurvey> list = Collection.getSurveyList();
    private int currentPosition=0;


    private SurveyAdapterListener surveyAdapterListener;


    public SurveyAdapter(Context context) {
        this.context = context;
        float desity = context.getResources().getDisplayMetrics().density;
        float width = context.getResources().getDisplayMetrics().widthPixels;
        float scaledFactor = (width/desity)/640;
        fontSize = scaledFactor * context.getSharedPreferences("preferences", Context.MODE_PRIVATE).getInt("fontSize", 18);
        //Added By Rex

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void  setCurrentPosition(int position){
        this.currentPosition = position;
    }

    public void setSurveyAdapterListener(SurveyAdapterListener surveyAdapterListener) {
        this.surveyAdapterListener = surveyAdapterListener;
    }

    public List<ItemSurvey> getSurveyItemList(){
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, final int position) {
        ItemSurvey item = list.get(position);
        View view = null;

        if(item.getType().equals(ItemSurvey.TYPE_RADIO)) {
            view = layoutInflater.inflate(R.layout.item_survey_radio, viewGroup, false);
            view.setTag("pos"+String.valueOf(position+1)); //Add by Rex

            TextView lbTitle = (TextView)view.findViewById(R.id.surveyQuestion);

            lbTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            //Added By Rex

            lbTitle.setText(item.getQuestionNo() + ". " + item.getQuestion());

            RadioGroup opts = (RadioGroup)view.findViewById(R.id.surveyItems);
            opts.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if(i == R.id.surveyOpt1){
                        list.get(currentPosition).setAnswer("a");
                    }else if(i == R.id.surveyOpt2){
                        list.get(currentPosition).setAnswer("b");
                    }else if(i == R.id.surveyOpt3){
                        list.get(currentPosition).setAnswer("c");
                    }else if(i == R.id.surveyOpt4){
                        list.get(currentPosition).setAnswer("d");
                    }
                    Log.d("survey","question #:" + list.get(currentPosition).getQuestionNo() + "  ans: "+ list.get(currentPosition).getAnswer());
                    if(surveyAdapterListener!=null){
                        surveyAdapterListener.onItemUpdated(true);
                    }
                }
            });

            RadioButton opt1 = (RadioButton)view.findViewById(R.id.surveyOpt1);
            RadioButton opt2 = (RadioButton)view.findViewById(R.id.surveyOpt2);
            RadioButton opt3 = (RadioButton)view.findViewById(R.id.surveyOpt3);
            RadioButton opt4 = (RadioButton)view.findViewById(R.id.surveyOpt4);

            opt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            opt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            opt3.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            opt4.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            //Added By Rex

            opt1.setText(item.getOption1());
            opt2.setText(item.getOption2());
            opt3.setText(item.getOption3());
            opt4.setText(item.getOption4());

            viewGroup.addView(view);
        }else if (item.getType().equals(ItemSurvey.TYPE_CHECKBOX)) {
            view = layoutInflater.inflate(R.layout.item_survey_checkbox, viewGroup, false);
            view.setTag("pos"+String.valueOf(position+1)); //Add by Rex

            TextView lbTitle = (TextView)view.findViewById(R.id.surveyQuestion);
            lbTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            //Added By Rex
            lbTitle.setText(item.getQuestionNo() + ". " + item.getQuestion());

            CheckBox opt1 = (CheckBox)view.findViewById(R.id.surveyOpt1);
            CheckBox opt2 = (CheckBox)view.findViewById(R.id.surveyOpt2);
            CheckBox opt3 = (CheckBox)view.findViewById(R.id.surveyOpt3);
            CheckBox opt4 = (CheckBox)view.findViewById(R.id.surveyOpt4);

            opt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            opt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            opt3.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            opt4.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            //Added By Rex

            opt1.setText(item.getOption1());
            opt2.setText(item.getOption2());
            opt3.setText(item.getOption3());
            opt4.setText(item.getOption4());

            opt1.setOnCheckedChangeListener(checkboxChangedListener);
            opt2.setOnCheckedChangeListener(checkboxChangedListener);
            opt3.setOnCheckedChangeListener(checkboxChangedListener);
            opt4.setOnCheckedChangeListener(checkboxChangedListener);

            viewGroup.addView(view);
        } else if(item.getType().equals(ItemSurvey.TYPE_TEXT)){
            view = layoutInflater.inflate(R.layout.item_survey_text, viewGroup, false);
            view.setTag("pos"+String.valueOf(position+1)); //Add by Rex

            TextView lbTitle = (TextView)view.findViewById(R.id.surveyQuestion);
            lbTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            //Added By Rex

            lbTitle.setText(item.getQuestionNo() + ". " + item.getQuestion());

            EditText txtAns = (EditText)view.findViewById(R.id.surveyAnswer);
            txtAns.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            //Added By Rex

            txtAns.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //do nth
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    list.get(currentPosition).setAnswer(charSequence.toString());
                    Log.d("survey","question #:" +  list.get(currentPosition).getQuestionNo() + "  ans: "+  list.get(currentPosition).getAnswer());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // do nth
                }
            });
            viewGroup.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object) {
        viewGroup.removeView((RelativeLayout) object);
    }

    private CheckBox.OnCheckedChangeListener checkboxChangedListener =  new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            ItemSurvey item = list.get(currentPosition);
            if(compoundButton.getId() == R.id.surveyOpt1){
                if(isChecked && !item.getAnswer().contains("a")){
                    String ans = item.getAnswer().concat("a");
                    item.setAnswer(ans);
                }else if(!isChecked){
                    String ans = item.getAnswer().replace("a","");
                    item.setAnswer(ans);
                }
            }else if(compoundButton.getId() == R.id.surveyOpt2){
                if(isChecked && !item.getAnswer().contains("b")){
                    String ans = item.getAnswer().concat("b");
                    item.setAnswer(ans);
                }else if(!isChecked){
                    String ans = item.getAnswer().replace("b","");
                    item.setAnswer(ans);
                }
            }else if(compoundButton.getId() == R.id.surveyOpt3){
                if(isChecked &&  !item.getAnswer().contains("c")){
                    String ans = item.getAnswer().concat("c");
                    item.setAnswer(ans);
                }else if(!isChecked){
                    String ans = item.getAnswer().replace("c","");
                    item.setAnswer(ans);
                }
            }else if(compoundButton.getId() == R.id.surveyOpt4){
                if(isChecked && !item.getAnswer().contains("d")){
                    String ans = item.getAnswer().concat("d");
                    item.setAnswer(ans);
                }else if(!isChecked){
                    String ans = item.getAnswer().replace("d","");
                    item.setAnswer(ans);
                }
            }
            Log.d("survey","question #:" + item.getQuestionNo() + "  ans: "+ item.getAnswer());
            if(surveyAdapterListener!=null){
                surveyAdapterListener.onItemUpdated(true);
            }
        }
    };

}