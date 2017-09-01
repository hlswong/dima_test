package com.sahk.earlyliteracy.widgets;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class PagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {

    boolean redraw = false;
    private InfiniteViewPager infiniteViewPager;
    private Point mCenter = new Point();
    private Point mInitialTouch = new Point();

    public PagerContainer(Context context) {
        super(context);
        init();
    }

    public PagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setClipChildren(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        try {
            infiniteViewPager = (InfiniteViewPager) getChildAt(0);
            infiniteViewPager.addOnPageChangeListener(this);
        } catch (Exception e) {
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }

    public InfiniteViewPager getViewPager() {
        return infiniteViewPager;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenter.x = w / 2;
        mCenter.y = h / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialTouch.x = (int) motionEvent.getX();
                mInitialTouch.y = (int) motionEvent.getY();
            default:
                motionEvent.offsetLocation(mCenter.x - mInitialTouch.x, mCenter.y - mInitialTouch.y);
                break;
        }
        return infiniteViewPager.dispatchTouchEvent(motionEvent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (redraw) invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        redraw = (state != ViewPager.SCROLL_STATE_IDLE);
    }
}