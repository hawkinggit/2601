package com.mstarc.wearablelauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.mstarc.wearablelauncher.example.ClockDrawerFragment;
import com.mstarc.wearablelauncher.view.alipay.AlipayFragment;
import com.mstarc.wearablelauncher.view.clock.IdleFragment;
import com.mstarc.wearablelauncher.view.common.DepthPageTransformer;
import com.mstarc.wearablelauncher.view.common.HorizontalViewPager;
import com.mstarc.wearablelauncher.view.settings.SettingFragment;


public class MainActivity extends FragmentActivity implements IdleFragment.PageListener {

    private static final int NUM_PAGES = 3;


    public static final int ALIPAY_PAGE_INDEX = 0;

    public static final int IDLE_PAGE_INDEX = 1;

    public static final int SETTING_PAGE_INDEX = 2;
    private static final String TAG = MainActivity.class.getSimpleName();

    private HorizontalViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private IdleFragment mIdleFragment = new IdleFragment();
    private ClockDrawerFragment mClockDrawerFragment = new ClockDrawerFragment();
    private SettingFragment mSettingFragment = new SettingFragment();
    private AlipayFragment mAlipayFragment = new AlipayFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (HorizontalViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(1);
        mPager.setCurrentItem(IDLE_PAGE_INDEX, true);
        mIdleFragment.setPageListener(this);
        mPager.setPageTransformer(true, new DepthPageTransformer(DepthPageTransformer.HORIZONTAL));

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG,"onPageSelected:"+position);
        mPager.setSwipeEnabled(position == 1 ? true : false);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == IDLE_PAGE_INDEX) {
                return mIdleFragment;
            } else if (position == SETTING_PAGE_INDEX) {
                return mSettingFragment;
            } else if (position == ALIPAY_PAGE_INDEX) {
                return mAlipayFragment;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
