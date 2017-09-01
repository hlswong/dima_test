package com.sahk.earlyliteracy.widgets;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class InfinitePagerAdapter extends PagerAdapter {

    private PagerAdapter pagerAdapter;

    public InfinitePagerAdapter(PagerAdapter pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
    }

    @Override
    public int getCount() {
        if (getRealCount() == 0) {
            return 0;
        }
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return pagerAdapter.getCount();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        int virtualPosition = position % getRealCount();
        return pagerAdapter.instantiateItem(viewGroup, virtualPosition);
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object) {
        int virtualPosition = position % getRealCount();
        pagerAdapter.destroyItem(viewGroup, virtualPosition, object);
    }

    @Override
    public void finishUpdate(ViewGroup viewGroup) {
        pagerAdapter.finishUpdate(viewGroup);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return pagerAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        pagerAdapter.restoreState(parcelable, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return pagerAdapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup viewGroup) {
        pagerAdapter.startUpdate(viewGroup);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int virtualPosition = position % getRealCount();
        return pagerAdapter.getPageTitle(virtualPosition);
    }

    @Override
    public float getPageWidth(int position) {
        return pagerAdapter.getPageWidth(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup viewGroup, int position, Object object) {
        pagerAdapter.setPrimaryItem(viewGroup, position, object);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        pagerAdapter.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        pagerAdapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void notifyDataSetChanged() {
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return pagerAdapter.getItemPosition(object);
    }
}