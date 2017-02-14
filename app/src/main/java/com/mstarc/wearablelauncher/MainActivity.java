package com.mstarc.wearablelauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mstarc.wearablelauncher.view.HorizontalViewPager;
import com.mstarc.wearablelauncher.view.SettingFragment;


public class MainActivity extends FragmentActivity {

    private static final int NUM_PAGES = 2;


    private static final int IDLE_PAGE_INDEX = 0;

    private static final int SETTING_PAGE_INDEX = 1;

    private HorizontalViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (HorizontalViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(NUM_PAGES);
        mPager.setCurrentItem(IDLE_PAGE_INDEX, true);


//        setContentView(R.layout.clock_subfragment_layout);
//        setContentView(R.layout.setting);
//        WearableListView listView = (WearableListView) findViewById(R.id.setting_list_view);
//        listView.setAdapter(new SettingListAdapter(this));
//        listView.addItemDecoration(new DecorationSettingItem(this, LinearLayoutManager.VERTICAL));
//        ViewGroup indicator = (ViewGroup) findViewById(R.id.setting_indicator);
//        listView.addOnScrollListener(new ScrollChangeListener(indicator));
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == IDLE_PAGE_INDEX) {
                return ClockFragment.getInstance();
            } else if (position == SETTING_PAGE_INDEX) {
                return SettingFragment.getInstance();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public static class ClockFragment extends Fragment {
        private static ClockFragment INSTANCE;

        public static ClockFragment getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new ClockFragment();
            }
            return INSTANCE;
        }

        public ClockFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.clock_subfragment_layout, container, false);
        }
    }
}
