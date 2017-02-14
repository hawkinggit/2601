package com.mstarc.wearablelauncher.view;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by wangxinzhi on 17-2-12.
 */

public class SettingItem extends LinearLayout implements WearableListView.Item {
    public SettingItem(Context context) {
        super(context);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public float getProximityMinValue() {
        return 1f;
    }

    @Override
    public float getProximityMaxValue() {
        return 1f;
    }

    @Override
    public float getCurrentProximityValue() {
        return 1f;
    }

    @Override
    public void setScalingAnimatorValue(float v) {

    }

    @Override
    public void onScaleUpStart() {

    }

    @Override
    public void onScaleDownStart() {

    }
}
