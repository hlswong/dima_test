package com.sahk.earlyliteracy.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.constants.Collection;

public class TutorialAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private List<Integer> tutorialList = Collection.TUTORIAL_LIST;
    private List<String> tutorialString = Collection.TUTORIAL_STRING;

    public TutorialAdapter(Context context) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tutorialList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, final int position) {
        View view = layoutInflater.inflate(R.layout.viewpager_tutorial, viewGroup, false);
        ImageView imageViewTutorial = (ImageView) view.findViewById(R.id.imageViewTutorial);
        imageViewTutorial.setBackgroundResource(tutorialList.get(position));
        imageViewTutorial.setContentDescription(tutorialString.get(position));
        viewGroup.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object) {
        viewGroup.removeView((RelativeLayout) object);
    }
}