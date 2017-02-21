package com.mstarc.wearablelauncher.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mstarc.wearablelauncher.MainActivity;
import com.mstarc.wearablelauncher.R;
import com.mstarc.wearablelauncher.view.adpter.ScrollChangeListener;
import com.mstarc.wearablelauncher.view.adpter.SettingListAdapter;
import com.mstarc.wearablelauncher.view.common.DepthPageTransformer;

/**
 * Created by wangxinzhi on 17-2-12.
 */

public class SettingFragment extends Fragment implements WearableListView.ClickListener {
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
        mSettingListView.setClickListener(this);
        rootView.setTag(DepthPageTransformer.ITEM_RIGHT_OR_BOTTOM);
        return rootView;
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        SettingListAdapter.ViewHolder holder = (SettingListAdapter.ViewHolder) viewHolder;
        Log.d(TAG, "onClick: " + holder.getmItemId());
        switch (holder.getmItemId()) {
            case SETTINGS:
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
//                getActivity().getFragmentManager().beginTransaction().replace(android.R.id.content, new DetailSettingFragment());
                break;
            default:
                break;

        }

    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
