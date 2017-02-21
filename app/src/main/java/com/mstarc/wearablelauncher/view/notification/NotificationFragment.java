package com.mstarc.wearablelauncher.view.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mstarc.wearablelauncher.R;
import com.mstarc.wearablelauncher.view.common.DepthPageTransformer;

/**
 * Created by wangxinzhi on 17-2-19.
 */

public class NotificationFragment extends Fragment {
    public NotificationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_subfragment_layout, container,
                false);
        view.setTag(DepthPageTransformer.ITEM_RIGHT_OR_BOTTOM);
        return view;
    }
}
