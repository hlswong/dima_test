package com.sahk.earlyliteracy.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.adapters.TutorialAdapter;
import com.sahk.earlyliteracy.applications.Config;
import me.relex.circleindicator.CircleIndicator;

public class TutorialFragment extends BaseFragment {

    private ImageButton btn_left, btn_right;

    public static TutorialFragment newInstance() {
        return new TutorialFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_tutorial, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_SETTINGS);
        final ViewPager viewPagerTutorial = (ViewPager) view.findViewById(R.id.viewPagerTutorial);
        final TutorialAdapter tutorialAdapter = new TutorialAdapter(getActivity());
        viewPagerTutorial.setAdapter(tutorialAdapter);
        CircleIndicator circleIndicatorTutorial = (CircleIndicator) view.findViewById(R.id.circleIndicatorTutorial);
        circleIndicatorTutorial.setViewPager(viewPagerTutorial);

        btn_left = (ImageButton)view.findViewById(R.id.btn_left);
        btn_left.setVisibility(View.INVISIBLE);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = 0;
                position = viewPagerTutorial.getCurrentItem() - 1;
                if ( position < 0 ) return;
                viewPagerTutorial.setCurrentItem( position  ,true);
            }
        });

        btn_right = (ImageButton)view.findViewById(R.id.btn_right);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = 0;
                position = viewPagerTutorial.getCurrentItem() + 1;
                if ( position > tutorialAdapter.getCount() ) return;
                viewPagerTutorial.setCurrentItem( position  ,true);
            }
        });

        viewPagerTutorial.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if (position != 0 )
                {
                    btn_left.setVisibility(View.VISIBLE);
                }else
                {
                    btn_left.setVisibility(View.INVISIBLE);
                }
                if ((position+1) != tutorialAdapter.getCount() )
                {
                    btn_right.setVisibility(View.VISIBLE);
                }else
                {
                    btn_right.setVisibility(View.INVISIBLE);
                }
            }
        });

        return view;
    }
}