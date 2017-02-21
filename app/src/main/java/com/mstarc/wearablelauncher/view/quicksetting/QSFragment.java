package com.mstarc.wearablelauncher.view.quicksetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mstarc.wearablelauncher.R;
import com.mstarc.wearablelauncher.view.common.DepthPageTransformer;
import com.mstarc.wearablelauncher.view.common.HorizontalViewPager;

import java.util.ArrayList;

/**
 * Created by wangxinzhi on 17-2-19.
 */

public class QSFragment extends Fragment {

    /**
     * QuickSetting Fragment Definition.
     */
    public QSFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quicksetting_subfragment_layout, container,
                false);
        HorizontalViewPager page = (HorizontalViewPager) view.findViewById(R.id.pager);
        page.setAdapter(new PageAdapter(getFragmentManager()));
        view.setTag(DepthPageTransformer.ITEM_LEFT_OR_TOP);

        return view;
    }

    class PageAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> mFragments = new ArrayList<>();

        public PageAdapter(FragmentManager fm) {
            super(fm);
            QSItemFragment fragment = new QSItemFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(QSItemFragment.TAG, R.layout.quicksetting1);
            fragment.setArguments(bundle);
            mFragments.add(fragment);

            fragment = new QSItemFragment();
            bundle = new Bundle();
            bundle.putInt(QSItemFragment.TAG, R.layout.quicksetting2);
            fragment.setArguments(bundle);
            mFragments.add(fragment);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    public static class QSItemFragment extends Fragment {

        int mLayoutRes = 0;
        public static final String TAG = QSItemFragment.class.getSimpleName();

        public QSItemFragment() {

        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            mLayoutRes = args.getInt(TAG);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Log.d(TAG,"onCreateView mLayoutRes:"+mLayoutRes);
            View view = inflater.inflate(mLayoutRes, container,
                    false);
            return view;
        }
    }
}
