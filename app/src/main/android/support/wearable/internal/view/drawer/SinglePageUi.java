//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.internal.view.drawer;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.mstarc.wearablelauncher.R.id;
import com.mstarc.wearablelauncher.R.layout;
import android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter;
import android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePageUi implements Ui {
    @IdRes
    private static final int[] SINGLE_PAGE_BUTTON_IDS;
    @LayoutRes
    private static final int[] SINGLE_PAGE_LAYOUT_RES;
    private final WearableNavigationDrawer mDrawer;
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final Runnable mCloseDrawerRunnable = new Runnable() {
        public void run() {
            SinglePageUi.this.mDrawer.closeDrawer();
        }
    };
    private WearableNavigationDrawerPresenter mPresenter;
    private CircledImageView[] mSinglePageImageViews;
    @Nullable
    private TextView mTextView;

    public SinglePageUi(WearableNavigationDrawer navigationDrawer) {
        if(navigationDrawer == null) {
            throw new IllegalArgumentException("Received null navigationDrawer.");
        } else {
            this.mDrawer = navigationDrawer;
        }
    }

    public void setPresenter(WearableNavigationDrawerPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void initialize(int count) {
        if(count >= 0 && count < SINGLE_PAGE_LAYOUT_RES.length && SINGLE_PAGE_LAYOUT_RES[count] != 0) {
            int layoutRes = SINGLE_PAGE_LAYOUT_RES[count];
            LayoutInflater inflater = LayoutInflater.from(this.mDrawer.getContext());
            View content = inflater.inflate(layoutRes, this.mDrawer, false);
            View peek = inflater.inflate(layout.single_page_nav_drawer_peek_view, this.mDrawer, false);
            this.mTextView = (TextView)content.findViewById(id.wearable_support_nav_drawer_text);
            this.mSinglePageImageViews = new CircledImageView[count];

            for(int i = 0; i < count; ++i) {
                this.mSinglePageImageViews[i] = (CircledImageView)content.findViewById(SINGLE_PAGE_BUTTON_IDS[i]);
                this.mSinglePageImageViews[i].setOnClickListener(new SinglePageUi.OnSelectedClickHandler(i, this.mPresenter));
                this.mSinglePageImageViews[i].setCircleHidden(true);
            }

            this.mDrawer.setDrawerContent(content);
            this.mDrawer.setPeekContent(peek);
        } else {
            this.mDrawer.setDrawerContent((View)null);
        }
    }

    public void setIcon(int index, Drawable drawable, String contentDescription) {
        this.mSinglePageImageViews[index].setImageDrawable(drawable);
        this.mSinglePageImageViews[index].setContentDescription(contentDescription);
    }

    public void setText(String itemText, boolean showToastIfNoTextView) {
        if(this.mTextView != null) {
            this.mTextView.setText(itemText);
        } else if(showToastIfNoTextView) {
            Toast toast = Toast.makeText(this.mDrawer.getContext(), itemText, Toast.LENGTH_SHORT);
            toast.setGravity(17, 0, 0);
            toast.show();
        }

    }

    public void selectItem(int index) {
        this.mSinglePageImageViews[index].setCircleHidden(false);
    }

    public void deselectItem(int index) {
        this.mSinglePageImageViews[index].setCircleHidden(true);
    }

    public void closeDrawerDelayed(long delayMs) {
        this.mMainThreadHandler.removeCallbacks(this.mCloseDrawerRunnable);
        this.mMainThreadHandler.postDelayed(this.mCloseDrawerRunnable, delayMs);
    }

    public void peekDrawer() {
        this.mDrawer.peekDrawer();
    }

    static {
        SINGLE_PAGE_BUTTON_IDS = new int[]{id.wearable_support_nav_drawer_icon_0, id.wearable_support_nav_drawer_icon_1, id.wearable_support_nav_drawer_icon_2, id.wearable_support_nav_drawer_icon_3, id.wearable_support_nav_drawer_icon_4, id.wearable_support_nav_drawer_icon_5, id.wearable_support_nav_drawer_icon_6};
        SINGLE_PAGE_LAYOUT_RES = new int[]{0, layout.single_page_nav_drawer_1_item, layout.single_page_nav_drawer_2_item, layout.single_page_nav_drawer_3_item, layout.single_page_nav_drawer_4_item, layout.single_page_nav_drawer_5_item, layout.single_page_nav_drawer_6_item, layout.single_page_nav_drawer_7_item};
    }

    private static class OnSelectedClickHandler implements OnClickListener {
        private final int mIndex;
        private final WearableNavigationDrawerPresenter mPresenter;

        private OnSelectedClickHandler(int index, WearableNavigationDrawerPresenter presenter) {
            this.mIndex = index;
            this.mPresenter = presenter;
        }

        public void onClick(View v) {
            this.mPresenter.onSelected(this.mIndex);
        }
    }
}
