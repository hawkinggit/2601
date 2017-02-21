//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.internal.view.drawer;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter;
import android.support.wearable.view.drawer.WearableNavigationDrawer.WearableNavigationDrawerAdapter;

public class SinglePagePresenter implements WearableNavigationDrawerPresenter {
    private static final long DRAWER_CLOSE_DELAY_MS = 500L;
    private final SinglePagePresenter.Ui mUi;
    private final boolean mIsAccessibilityEnabled;
    @Nullable
    private WearableNavigationDrawerAdapter mAdapter;
    private int mCount = 0;
    private int mSelected = 0;

    public SinglePagePresenter(SinglePagePresenter.Ui ui, boolean isAccessibilityEnabled) {
        if(ui == null) {
            throw new IllegalArgumentException("Received null ui.");
        } else {
            this.mIsAccessibilityEnabled = isAccessibilityEnabled;
            this.mUi = ui;
            this.mUi.setPresenter(this);
            this.onDataSetChanged();
        }
    }

    public void onDataSetChanged() {
        if(this.mAdapter != null) {
            int count = this.mAdapter.getCount();
            if(this.mCount != count) {
                this.mCount = count;
                this.mSelected = Math.min(this.mSelected, count - 1);
                this.mUi.initialize(count);
            }

            for(int i = 0; i < count; ++i) {
                this.mUi.setIcon(i, this.mAdapter.getItemDrawable(i), this.mAdapter.getItemText(i));
            }

            this.mUi.setText(this.mAdapter.getItemText(this.mSelected), false);
            this.mUi.selectItem(this.mSelected);
        }
    }

    public void onNewAdapter(WearableNavigationDrawerAdapter adapter) {
        if(adapter == null) {
            throw new IllegalArgumentException("Received null adapter.");
        } else {
            this.mAdapter = adapter;
            this.mAdapter.setPresenter(this);
            this.onDataSetChanged();
        }
    }

    public void onSelected(int index) {
        this.mUi.deselectItem(this.mSelected);
        this.mUi.selectItem(index);
        this.mSelected = index;
        if(this.mIsAccessibilityEnabled) {
            this.mUi.peekDrawer();
        } else {
            this.mUi.closeDrawerDelayed(500L);
        }

        if(this.mAdapter != null) {
            this.mUi.setText(this.mAdapter.getItemText(index), true);
            this.mAdapter.onItemSelected(index);
        }

    }

    public void onSetCurrentItemRequested(int index, boolean smoothScrollTo) {
        this.mUi.deselectItem(this.mSelected);
        this.mUi.selectItem(index);
        this.mSelected = index;
        if(this.mAdapter != null) {
            this.mUi.setText(this.mAdapter.getItemText(index), false);
            this.mAdapter.onItemSelected(index);
        }

    }

    public boolean onDrawerTapped() {
        return false;
    }

    public interface Ui {
        void setPresenter(WearableNavigationDrawerPresenter var1);

        void initialize(int var1);

        void setIcon(int var1, Drawable var2, String var3);

        void setText(String var1, boolean var2);

        void selectItem(int var1);

        void deselectItem(int var1);

        void closeDrawerDelayed(long var1);

        void peekDrawer();
    }
}
