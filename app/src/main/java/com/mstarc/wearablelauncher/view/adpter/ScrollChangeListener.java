package com.mstarc.wearablelauncher.view.adpter;

import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by wangxinzhi on 17-2-12.
 */

public class ScrollChangeListener implements WearableListView.OnScrollListener {
    private static final String TAG = ScrollChangeListener.class.getSimpleName();

    public ScrollChangeListener(ViewGroup indicator) {
        this.mIndicator = indicator;
        mIndicator.getChildAt(0).setSelected(true);
    }

    ViewGroup mIndicator;

    @Override
    public void onScroll(int i) {

    }

    @Override
    public void onAbsoluteScrollChange(int i) {

    }

    @Override
    public void onScrollStateChanged(int i) {

    }

    @Override
    public void onCentralPositionChanged(int i) {
        int childCount = mIndicator.getChildCount();
        int toSelectedIndicator = i/3;
        Log.d(TAG, "onCentralPositionChanged "+i+" toSelectedIndicator: "+toSelectedIndicator+" childCount: "+childCount);
        for(int j=0; j< childCount;j++){
            if(j == toSelectedIndicator){
                mIndicator.getChildAt(j).setSelected(true);
            }else {
                mIndicator.getChildAt(j).setSelected(false);
            }
        }

    }
}
