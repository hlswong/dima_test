package com.sahk.earlyliteracy.widgets;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class InfiniteViewPager extends ViewPager {

    public InfiniteViewPager(Context context) {
        super(context);
        setScroller();
    }

    public InfiniteViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setScroller();
    }

    @Override
    public void setAdapter(PagerAdapter pagerAdapter) {
        super.setAdapter(pagerAdapter);
        setCurrentItem(0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (getAdapter().getCount() == 0) {
            super.setCurrentItem(item, smoothScroll);
            return;
        }
        item = getOffsetAmount() + (item % getAdapter().getCount());
        super.setCurrentItem(item, smoothScroll);
    }

    public void setCurrentItem(int item, boolean smoothScroll, boolean disableInfinite) {
        if (disableInfinite) {
            super.setCurrentItem(item, smoothScroll);
        } else {
            setCurrentItem(item, smoothScroll);
        }
    }

    @Override
    public int getCurrentItem() {
        if (getAdapter().getCount() == 0) {
            return super.getCurrentItem();
        }
        int position = super.getCurrentItem();
        if (getAdapter() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infinitePagerAdapter = (InfinitePagerAdapter) getAdapter();
            return (position % infinitePagerAdapter.getRealCount());
        } else {
            return super.getCurrentItem();
        }
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    private int getOffsetAmount() {
        if (getAdapter().getCount() == 0) {
            return 0;
        }
        if (getAdapter() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getAdapter();
            return infAdapter.getRealCount() * 100;
        } else {
            return 0;
        }
    }

    private void setScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field field = viewpager.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this, new DecelerateScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DecelerateScroller extends Scroller {
        public DecelerateScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 1000 /*1 secs*/);
        }
    }
}