//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.internal.view.drawer;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import com.mstarc.wearablelauncher.R.id;
import com.mstarc.wearablelauncher.R.layout;
import android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter;
import android.support.wearable.internal.view.drawer.MultiPagePresenter.Ui;
import android.support.wearable.view.drawer.PageIndicatorView;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.support.wearable.view.drawer.WearableNavigationDrawer.WearableNavigationDrawerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MultiPageUi implements Ui {
    private static final String TAG = "MultiPageUi";
    private WearableNavigationDrawerPresenter mPresenter;
    @Nullable
    private ViewPager mNavigationPager;
    @Nullable
    private PageIndicatorView mPageIndicatorView;

    public MultiPageUi() {
    }

    public void initialize(WearableNavigationDrawer drawer, WearableNavigationDrawerPresenter presenter) {
        if(drawer == null) {
            throw new IllegalArgumentException("Received null drawer.");
        } else if(presenter == null) {
            throw new IllegalArgumentException("Received null presenter.");
        } else {
            this.mPresenter = presenter;
            LayoutInflater inflater = LayoutInflater.from(drawer.getContext());
            View content = inflater.inflate(layout.navigation_drawer_view, drawer, false);
            this.mNavigationPager = (ViewPager)content.findViewById(id.wearable_support_navigation_drawer_view_pager);
            this.mPageIndicatorView = (PageIndicatorView)content.findViewById(id.wearable_support_navigation_drawer_page_indicator);
            drawer.setDrawerContent(content);
        }
    }

    public void setNavigationPagerAdapter(WearableNavigationDrawerAdapter adapter) {
        if(this.mNavigationPager != null && this.mPageIndicatorView != null) {
            MultiPageUi.NavigationPagerAdapter navigationPagerAdapter = new MultiPageUi.NavigationPagerAdapter(adapter);
            this.mNavigationPager.setAdapter(navigationPagerAdapter);
            this.mNavigationPager.clearOnPageChangeListeners();
            this.mNavigationPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
                public void onPageSelected(int position) {
                    MultiPageUi.this.mPresenter.onSelected(position);
                }
            });
            this.mPageIndicatorView.setPager(this.mNavigationPager);
        } else {
            Log.w("MultiPageUi", "setNavigationPagerAdapter was called before initialize.");
        }
    }

    public void notifyPageIndicatorDataChanged() {
        if(this.mPageIndicatorView != null) {
            this.mPageIndicatorView.notifyDataSetChanged();
        }

    }

    public void notifyNavigationPagerAdapterDataChanged() {
        if(this.mNavigationPager != null) {
            PagerAdapter adapter = this.mNavigationPager.getAdapter();
            if(adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

    }

    public void setNavigationPagerSelectedItem(int index, boolean smoothScrollTo) {
        if(this.mNavigationPager != null) {
            this.mNavigationPager.setCurrentItem(index, smoothScrollTo);
        }

    }

    private static final class NavigationPagerAdapter extends PagerAdapter {
        private final WearableNavigationDrawerAdapter mAdapter;

        NavigationPagerAdapter(WearableNavigationDrawerAdapter adapter) {
            this.mAdapter = adapter;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(layout.navigation_drawer_item_view, container, false);
            container.addView(view);
            ImageView iconView = (ImageView)view.findViewById(id.wearable_support_navigation_drawer_item_icon);
            TextView textView = (TextView)view.findViewById(id.wearable_support_navigation_drawer_item_text);
            iconView.setImageDrawable(this.mAdapter.getItemDrawable(position));
            textView.setText(this.mAdapter.getItemText(position));
            return view;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        public int getCount() {
            return this.mAdapter.getCount();
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
