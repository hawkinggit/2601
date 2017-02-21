//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.view.drawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.mstarc.wearablelauncher.R.string;
import com.mstarc.wearablelauncher.R.styleable;
import android.support.wearable.internal.view.drawer.MultiPagePresenter;
import android.support.wearable.internal.view.drawer.MultiPageUi;
import android.support.wearable.internal.view.drawer.SinglePagePresenter;
import android.support.wearable.internal.view.drawer.SinglePageUi;
import android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter;
import android.support.wearable.view.drawer.WearableDrawerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.accessibility.AccessibilityManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

@TargetApi(23)
public class WearableNavigationDrawer extends WearableDrawerView {
    private static final String TAG = "WearableNavDrawer";
    private static final int DEFAULT_STYLE = 1;
    private static final long AUTO_CLOSE_DRAWER_DELAY_MS;
    private final boolean mIsAccessibilityEnabled;
    private final Handler mMainThreadHandler;
    private final Runnable mCloseDrawerRunnable;
    @Nullable
    private final GestureDetector mGestureDetector;
    private final WearableNavigationDrawerPresenter mPresenter;
    private final SimpleOnGestureListener mOnGestureListener;

    public WearableNavigationDrawer(Context context) {
        this(context, (AttributeSet)null);
    }

    public WearableNavigationDrawer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableNavigationDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mCloseDrawerRunnable = new Runnable() {
            public void run() {
                WearableNavigationDrawer.this.closeDrawer();
            }
        };
        this.mOnGestureListener = new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return WearableNavigationDrawer.this.mPresenter.onDrawerTapped();
            }
        };
        this.mGestureDetector = new GestureDetector(this.getContext(), this.mOnGestureListener);
        boolean singlePage = false;
        if(attrs != null) {
            TypedArray accessibilityManager = context.obtainStyledAttributes(attrs, styleable.WearableNavigationDrawer, defStyleAttr, 0);

            try {
                int navigationStyle = accessibilityManager.getInt(styleable.WearableNavigationDrawer_navigation_style, 1);
                singlePage = navigationStyle == 0;
            } finally {
                accessibilityManager.recycle();
            }
        }

        AccessibilityManager accessibilityManager1 = (AccessibilityManager)context.getSystemService("accessibility");
        this.mIsAccessibilityEnabled = accessibilityManager1.isEnabled();
        this.mPresenter = (WearableNavigationDrawerPresenter)(singlePage?new SinglePagePresenter(new SinglePageUi(this), this.mIsAccessibilityEnabled):new MultiPagePresenter(this, new MultiPageUi(), this.mIsAccessibilityEnabled));
        this.getPeekContainer().setContentDescription(context.getString(string.navigation_drawer_content_description));
        this.setShouldOnlyOpenWhenAtTop(true);
    }

    @VisibleForTesting
    public WearableNavigationDrawer(Context context, WearableNavigationDrawerPresenter presenter, GestureDetector gestureDetector) {
        super(context);
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mCloseDrawerRunnable = new Runnable() {
            public void run() {
                WearableNavigationDrawer.this.closeDrawer();
            }
        };
        this.mOnGestureListener = new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return WearableNavigationDrawer.this.mPresenter.onDrawerTapped();
            }
        };
        this.mPresenter = presenter;
        this.mGestureDetector = gestureDetector;
        this.mIsAccessibilityEnabled = false;
    }

    public void setAdapter(WearableNavigationDrawer.WearableNavigationDrawerAdapter adapter) {
        this.mPresenter.onNewAdapter(adapter);
    }

    public void setCurrentItem(int index, boolean smoothScrollTo) {
        this.mPresenter.onSetCurrentItemRequested(index, smoothScrollTo);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.autoCloseDrawerAfterDelay();
        return this.mGestureDetector != null && this.mGestureDetector.onTouchEvent(ev);
    }

    public boolean canScrollHorizontally(int direction) {
        return this.isOpened();
    }

    public void onDrawerOpened() {
        this.autoCloseDrawerAfterDelay();
    }

    public void onDrawerClosed() {
        this.mMainThreadHandler.removeCallbacks(this.mCloseDrawerRunnable);
    }

    private void autoCloseDrawerAfterDelay() {
        if(!this.mIsAccessibilityEnabled) {
            this.mMainThreadHandler.removeCallbacks(this.mCloseDrawerRunnable);
            this.mMainThreadHandler.postDelayed(this.mCloseDrawerRunnable, AUTO_CLOSE_DRAWER_DELAY_MS);
        }

    }

    int preferGravity() {
        return 48;
    }

    static {
        AUTO_CLOSE_DRAWER_DELAY_MS = TimeUnit.SECONDS.toMillis(5L);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NavigationStyle {
        int SINGLE_PAGE = 0;
        int MULTI_PAGE = 1;
    }

    public abstract static class WearableNavigationDrawerAdapter {
        @Nullable
        private WearableNavigationDrawerPresenter mPresenter;

        public WearableNavigationDrawerAdapter() {
        }

        public abstract String getItemText(int var1);

        public abstract Drawable getItemDrawable(int var1);

        public abstract void onItemSelected(int var1);

        public abstract int getCount();

        public void notifyDataSetChanged() {
            if(this.mPresenter != null) {
                this.mPresenter.onDataSetChanged();
            } else {
                Log.w("WearableNavDrawer", "adapter.notifyDataSetChanged called before drawer.setAdapter; ignoring.");
            }

        }

        public void setPresenter(WearableNavigationDrawerPresenter presenter) {
            this.mPresenter = presenter;
        }
    }
}
