//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.internal.view.drawer;

import android.support.annotation.Nullable;
import android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.support.wearable.view.drawer.WearableNavigationDrawer.WearableNavigationDrawerAdapter;

public class MultiPagePresenter implements WearableNavigationDrawerPresenter {
    private final MultiPagePresenter.Ui mUi;
    private final WearableNavigationDrawer mDrawer;
    private final boolean mIsAccessibilityEnabled;
    @Nullable
    private WearableNavigationDrawerAdapter mAdapter;

    public MultiPagePresenter(WearableNavigationDrawer drawer, MultiPagePresenter.Ui ui, boolean isAccessibilityEnabled) {
        if(drawer == null) {
            throw new IllegalArgumentException("Received null drawer.");
        } else if(ui == null) {
            throw new IllegalArgumentException("Received null ui.");
        } else {
            this.mDrawer = drawer;
            this.mUi = ui;
            this.mUi.initialize(drawer, this);
            this.mIsAccessibilityEnabled = isAccessibilityEnabled;
        }
    }

    public void onDataSetChanged() {
        this.mUi.notifyNavigationPagerAdapterDataChanged();
        this.mUi.notifyPageIndicatorDataChanged();
    }

    public void onNewAdapter(WearableNavigationDrawerAdapter adapter) {
        if(adapter == null) {
            throw new IllegalArgumentException("Received null adapter.");
        } else {
            this.mAdapter = adapter;
            this.mAdapter.setPresenter(this);
            this.mUi.setNavigationPagerAdapter(adapter);
        }
    }

    public void onSelected(int index) {
        if(this.mAdapter != null) {
            this.mAdapter.onItemSelected(index);
        }

    }

    public void onSetCurrentItemRequested(int index, boolean smoothScrollTo) {
        this.mUi.setNavigationPagerSelectedItem(index, smoothScrollTo);
    }

    public boolean onDrawerTapped() {
        if(this.mDrawer.isOpened()) {
            if(this.mIsAccessibilityEnabled) {
                this.mDrawer.peekDrawer();
            } else {
                this.mDrawer.closeDrawer();
            }

            return true;
        } else {
            return false;
        }
    }

    public interface Ui {
        void initialize(WearableNavigationDrawer var1, WearableNavigationDrawerPresenter var2);

        void notifyNavigationPagerAdapterDataChanged();

        void notifyPageIndicatorDataChanged();

        void setNavigationPagerAdapter(WearableNavigationDrawerAdapter var1);

        void setNavigationPagerSelectedItem(int var1, boolean var2);
    }
}
