package com.mstarc.wearablelauncher.view.clock;

/**
 * Created by wangxinzhi on 17-2-19.
 */

/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mstarc.wearablelauncher.MainActivity;
import com.mstarc.wearablelauncher.R;
import com.mstarc.wearablelauncher.view.common.DepthPageTransformer;
import com.mstarc.wearablelauncher.view.common.VerticalViewPager;
import com.mstarc.wearablelauncher.view.notification.NotificationFragment;
import com.mstarc.wearablelauncher.view.quicksetting.QSFragment;


public class IdleFragment extends Fragment {
    private static final String TAG = IdleFragment.class.getSimpleName();

    private static final boolean DEBUG = true;

    private static final int NUM_PAGES = 3;

    private static final int QUICKSETTING_PAGE_INDEX = 0;

    private static final int CLOCK_PAGE_INDEX = 1;

    private static final int NOTIFICATION_PAGE_INDEX = 2;


    private VerticalViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private Handler mHandlerTime = new Handler();

    private QSFragment mQSFragment = new QSFragment();

    private ClockFragment mClockFragment = new ClockFragment();

    private NotificationFragment mNotificationFragment = new NotificationFragment();

    private final Runnable mTimerRun = new Runnable() {
        public void run() {
            backToClock();
        }
    };

    public void backToClock() {
        if (mPager != null && mPager.getCurrentItem() != CLOCK_PAGE_INDEX) {
            mPager.setCurrentItem(CLOCK_PAGE_INDEX, false);
        }
    }

    public IdleFragment() {
    }

    public void setPageListener(PageListener mPageListener) {
        this.mPageListener = mPageListener;
    }

    private PageListener mPageListener;

    public interface PageListener {
        void onPageSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.idle_fragment_layout, container, false);

        mPager = (VerticalViewPager) view.findViewById(R.id.vpager);
        mPagerAdapter = new IdlePagerAdapter(this.getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(NUM_PAGES);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
                if(mPageListener!=null) {
                    mPageListener.onPageSelected(position);
                }
            }
        });

        mPager.setCurrentItem(CLOCK_PAGE_INDEX, false);
        mPager.setPageTransformer(true, new DepthPageTransformer(DepthPageTransformer.VERTICAL));
        view.setTag(DepthPageTransformer.ITEM_CENTER);
        return view;
    }

    private class IdlePagerAdapter extends FragmentStatePagerAdapter {

        public IdlePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == QUICKSETTING_PAGE_INDEX) {
                return mQSFragment;
            } else if (position == NOTIFICATION_PAGE_INDEX) {
                return mNotificationFragment;
            } else {
                return mClockFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}

