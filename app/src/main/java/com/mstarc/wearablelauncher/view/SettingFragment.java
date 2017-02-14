package com.mstarc.wearablelauncher.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mstarc.wearablelauncher.R;
import com.mstarc.wearablelauncher.view.adpter.ScrollChangeListener;
import com.mstarc.wearablelauncher.view.adpter.SettingListAdapter;

/**
 * Created by wangxinzhi on 17-2-12.
 */

public class SettingFragment extends Fragment {
    static final String TAG = SettingFragment.class.getSimpleName();
    public WearableListView mSettingListView;
    private static SettingFragment INSTANCE;
    public static SettingFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SettingFragment();
        }
        return INSTANCE;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView.this:" + this);
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.setting, container, false);
        mSettingListView = (WearableListView)
                rootView.findViewById(R.id.setting_list_view);
        mSettingListView.setAdapter(new SettingListAdapter(getActivity()));
        mSettingListView.addItemDecoration(new DecorationSettingItem(getActivity(), LinearLayoutManager.VERTICAL));
        ViewGroup indicator = (ViewGroup) rootView.findViewById(R.id.setting_indicator);
        mSettingListView.addOnScrollListener(new ScrollChangeListener(indicator));
        return rootView;
    }
}
