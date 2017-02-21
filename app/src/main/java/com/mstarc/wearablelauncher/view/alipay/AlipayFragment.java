package com.mstarc.wearablelauncher.view.alipay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mstarc.wearablelauncher.MainActivity;
import com.mstarc.wearablelauncher.R;
import com.mstarc.wearablelauncher.view.common.DepthPageTransformer;

/**
 * Created by wangxinzhi on 17-2-19.
 */

public class AlipayFragment extends Fragment {
    public AlipayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alipay_layout, container,
                false);
        view.setTag(DepthPageTransformer.ITEM_LEFT_OR_TOP);
        return view;
    }
}

