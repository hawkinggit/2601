//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.view.drawer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.wearable.view.drawer.FlingWatcher;
import android.support.wearable.view.drawer.ViewDragHelper;
import android.support.wearable.view.drawer.WearableDrawerView;
import android.support.wearable.view.drawer.FlingWatcher.FlingListener;
import android.support.wearable.view.drawer.ViewDragHelper.Callback;
import android.support.wearable.view.drawer.WearableDrawerView.DrawerState;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;

public class WearableDrawerLayout extends FrameLayout implements OnLayoutChangeListener, NestedScrollingParent, FlingListener {
    private static final String TAG = "WearableDrawerLayout";
    private static final int GRAVITY_UNDEFINED = -1;
    private static final int PEEK_FADE_DURATION_MS = 150;
    private static final int PEEK_AUTO_CLOSE_DELAY_MS = 1000;
    private static final int DOWN = 1;
    private static final int UP = -1;
    private static final float OPENED_PERCENT_THRESHOLD = 0.5F;
    private static final int NESTED_SCROLL_SLOP_DP = 5;
    private final int mNestedScrollSlopPx;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final ViewDragHelper mTopDrawerDragger;
    private final ViewDragHelper mBottomDrawerDragger;
    @Nullable
    private WearableDrawerView mTopDrawerView;
    @Nullable
    private WearableDrawerView mBottomDrawerView;
    @Nullable
    private View mScrollingContentView;
    private WearableDrawerLayout.DrawerStateCallback mDrawerStateCallback;
    private int mSystemWindowInsetBottom;
    private int mCurrentNestedScrollSlopTracker;
    private boolean mShouldOpenTopDrawerAfterLayout;
    private boolean mShouldOpenBottomDrawerAfterLayout;
    private boolean mShouldPeekTopDrawerAfterLayout;
    private boolean mShouldPeekBottomDrawerAfterLayout;
    private boolean mCanTopDrawerBeClosed;
    private boolean mCanBottomDrawerBeClosed;
    private boolean mLastScrollWasFling;
    private MotionEvent mDrawerOpenLastInterceptedTouchEvent;
    private final boolean mIsAccessibilityEnabled;
    private final FlingWatcher mFlingWatcher;
    private final Handler mMainThreadHandler;
    private final WearableDrawerLayout.ClosePeekRunnable mCloseTopPeekRunnable;
    private final WearableDrawerLayout.ClosePeekRunnable mCloseBottomPeekRunnable;
    @VisibleForTesting
    final Callback mTopDrawerDraggerCallback;
    @VisibleForTesting
    final Callback mBottomDrawerDraggerCallback;

    public WearableDrawerLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public WearableDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WearableDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mCloseTopPeekRunnable = new WearableDrawerLayout.ClosePeekRunnable(48);
        this.mCloseBottomPeekRunnable = new WearableDrawerLayout.ClosePeekRunnable(80);
        this.mFlingWatcher = new FlingWatcher(this);
        this.mTopDrawerDraggerCallback = new WearableDrawerLayout.TopDrawerDraggerCallback();
        this.mTopDrawerDragger = ViewDragHelper.create(this, 1.0F, this.mTopDrawerDraggerCallback);
        this.mTopDrawerDragger.setEdgeTrackingEnabled(4);
        this.mBottomDrawerDraggerCallback = new WearableDrawerLayout.BottomDrawerDraggerCallback();
        this.mBottomDrawerDragger = ViewDragHelper.create(this, 1.0F, this.mBottomDrawerDraggerCallback);
        this.mBottomDrawerDragger.setEdgeTrackingEnabled(8);
        WindowManager windowManager = (WindowManager)context.getSystemService("window");
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        this.mNestedScrollSlopPx = Math.round(metrics.density * 5.0F);
        AccessibilityManager accessibilityManager = (AccessibilityManager)context.getSystemService("accessibility");
        this.mIsAccessibilityEnabled = accessibilityManager.isEnabled();
    }

    @VisibleForTesting
    WearableDrawerLayout(Context context, FlingWatcher flingWatcher, @Nullable WearableDrawerView topDrawerView, @Nullable WearableDrawerView bottomDrawerView, ViewDragHelper topDrawerDragger, ViewDragHelper bottomDrawerDragger, boolean isAccessibilityEnabled) {
        super(context);
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mCloseTopPeekRunnable = new WearableDrawerLayout.ClosePeekRunnable(48);
        this.mCloseBottomPeekRunnable = new WearableDrawerLayout.ClosePeekRunnable(80);
        this.mFlingWatcher = flingWatcher;
        this.mTopDrawerDragger = topDrawerDragger;
        this.mBottomDrawerDragger = bottomDrawerDragger;
        this.mTopDrawerView = topDrawerView;
        this.mBottomDrawerView = bottomDrawerView;
        this.mTopDrawerDraggerCallback = new WearableDrawerLayout.TopDrawerDraggerCallback();
        this.mBottomDrawerDraggerCallback = new WearableDrawerLayout.BottomDrawerDraggerCallback();
        this.mNestedScrollSlopPx = 5;
        this.mIsAccessibilityEnabled = isAccessibilityEnabled;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mTopDrawerDragger.refreshEdgeSize();
        this.mBottomDrawerDragger.refreshEdgeSize();
    }

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        this.mSystemWindowInsetBottom = insets.getSystemWindowInsetBottom();
        if(this.mSystemWindowInsetBottom != 0) {
            MarginLayoutParams layoutParams = (MarginLayoutParams)this.getLayoutParams();
            layoutParams.bottomMargin = this.mSystemWindowInsetBottom;
            this.setLayoutParams(layoutParams);
        }

        return super.onApplyWindowInsets(insets);
    }

    @VisibleForTesting
    void closeDrawerDelayed(int gravity, long delayMs) {
        switch(gravity) {
            case 48:
                this.mMainThreadHandler.removeCallbacks(this.mCloseTopPeekRunnable);
                this.mMainThreadHandler.postDelayed(this.mCloseTopPeekRunnable, delayMs);
                break;
            case 80:
                this.mMainThreadHandler.removeCallbacks(this.mCloseBottomPeekRunnable);
                this.mMainThreadHandler.postDelayed(this.mCloseBottomPeekRunnable, delayMs);
                break;
            default:
                Log.w("WearableDrawerLayout", (new StringBuilder(67)).append("Invoked a delayed drawer close with an invalid gravity: ").append(gravity).toString());
        }

    }

    public void closeDrawer(int gravity) {
        this.closeDrawer(this.findDrawerWithGravity(gravity));
    }

    public void closeDrawer(View drawer) {
        if(drawer != null) {
            if(drawer == this.mTopDrawerView) {
                this.mTopDrawerDragger.smoothSlideViewTo(this.mTopDrawerView, 0, -this.mTopDrawerView.getHeight());
                this.invalidate();
            } else if(drawer == this.mBottomDrawerView) {
                this.mBottomDrawerDragger.smoothSlideViewTo(this.mBottomDrawerView, 0, this.getHeight());
                this.invalidate();
            } else {
                Log.w("WearableDrawerLayout", "closeDrawer(View) should be passed in the top or bottom drawer");
            }
        }
    }

    public void openDrawer(int gravity) {
        if(!this.isLaidOut()) {
            switch(gravity) {
                case 48:
                    this.mShouldOpenTopDrawerAfterLayout = true;
                    break;
                case 80:
                    this.mShouldOpenBottomDrawerAfterLayout = true;
            }

        } else {
            this.openDrawer(this.findDrawerWithGravity(gravity));
        }
    }

    public void openDrawer(View drawer) {
        if(drawer != null) {
            if(!this.isLaidOut()) {
                if(drawer == this.mTopDrawerView) {
                    this.mShouldOpenTopDrawerAfterLayout = true;
                } else if(drawer == this.mBottomDrawerView) {
                    this.mShouldOpenBottomDrawerAfterLayout = true;
                }

            } else {
                if(drawer == this.mTopDrawerView) {
                    this.mTopDrawerDragger.smoothSlideViewTo(this.mTopDrawerView, 0, 0);
                    showDrawerContentMaybeAnimate(this.mTopDrawerView);
                    this.invalidate();
                } else if(drawer == this.mBottomDrawerView) {
                    this.mBottomDrawerDragger.smoothSlideViewTo(this.mBottomDrawerView, 0, this.getHeight() - this.mBottomDrawerView.getHeight());
                    showDrawerContentMaybeAnimate(this.mBottomDrawerView);
                    this.invalidate();
                } else {
                    Log.w("WearableDrawerLayout", "openDrawer(View) should be passed in the top or bottom drawer");
                }

            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.mBottomDrawerView != null && this.mBottomDrawerView.isOpened() && !this.mCanBottomDrawerBeClosed || this.mTopDrawerView != null && this.mTopDrawerView.isOpened() && !this.mCanTopDrawerBeClosed) {
            this.mDrawerOpenLastInterceptedTouchEvent = ev;
            return false;
        } else {
            boolean shouldInterceptTop = this.mTopDrawerDragger.shouldInterceptTouchEvent(ev);
            boolean shouldInterceptBottom = this.mBottomDrawerDragger.shouldInterceptTouchEvent(ev);
            return shouldInterceptTop || shouldInterceptBottom;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if(ev == null) {
            Log.w("WearableDrawerLayout", "null MotionEvent passed to onTouchEvent");
            return false;
        } else {
            this.mTopDrawerDragger.processTouchEvent(ev);
            this.mBottomDrawerDragger.processTouchEvent(ev);
            return true;
        }
    }

    public void computeScroll() {
        boolean topSettling = this.mTopDrawerDragger.continueSettling(true);
        boolean bottomSettling = this.mBottomDrawerDragger.continueSettling(true);
        if(topSettling || bottomSettling) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        if(child instanceof WearableDrawerView) {
            WearableDrawerView drawerChild = (WearableDrawerView)child;
            int childGravity = ((android.widget.FrameLayout.LayoutParams)params).gravity;
            if(childGravity == 0 || childGravity == -1) {
                ((android.widget.FrameLayout.LayoutParams)params).gravity = drawerChild.preferGravity();
                childGravity = drawerChild.preferGravity();
                drawerChild.setLayoutParams(params);
            }

            WearableDrawerView drawerView;
            if(childGravity == 48) {
                this.mTopDrawerView = drawerChild;
                drawerView = this.mTopDrawerView;
            } else if(childGravity == 80) {
                this.mBottomDrawerView = drawerChild;
                drawerView = this.mBottomDrawerView;
            } else {
                drawerView = null;
            }

            if(drawerView != null) {
                drawerView.addOnLayoutChangeListener(this);
            }

        }
    }

    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        float openedPercent;
        int height;
        int childTop;
        if(v == this.mTopDrawerView) {
            openedPercent = this.mTopDrawerView.getOpenedPercent();
            height = v.getHeight();
            childTop = -height + (int)((float)height * openedPercent);
            v.layout(v.getLeft(), childTop, v.getRight(), childTop + height);
        } else if(v == this.mBottomDrawerView) {
            openedPercent = this.mBottomDrawerView.getOpenedPercent();
            height = v.getHeight();
            childTop = (int)((float)this.getHeight() - (float)height * openedPercent);
            v.layout(v.getLeft(), childTop, v.getRight(), childTop + height);
        }

    }

    public void setDrawerStateCallback(WearableDrawerLayout.DrawerStateCallback callback) {
        this.mDrawerStateCallback = callback;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(this.mShouldPeekBottomDrawerAfterLayout || this.mShouldPeekTopDrawerAfterLayout || this.mShouldOpenTopDrawerAfterLayout || this.mShouldOpenBottomDrawerAfterLayout) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    WearableDrawerLayout.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if(WearableDrawerLayout.this.mShouldOpenBottomDrawerAfterLayout) {
                        WearableDrawerLayout.this.openDrawerWithoutAnimation(WearableDrawerLayout.this.mBottomDrawerView);
                        WearableDrawerLayout.this.mShouldOpenBottomDrawerAfterLayout = false;
                    } else if(WearableDrawerLayout.this.mShouldPeekBottomDrawerAfterLayout) {
                        WearableDrawerLayout.this.peekDrawer(80);
                        WearableDrawerLayout.this.mShouldPeekBottomDrawerAfterLayout = false;
                    }

                    if(WearableDrawerLayout.this.mShouldOpenTopDrawerAfterLayout) {
                        WearableDrawerLayout.this.openDrawerWithoutAnimation(WearableDrawerLayout.this.mTopDrawerView);
                        WearableDrawerLayout.this.mShouldOpenTopDrawerAfterLayout = false;
                    } else if(WearableDrawerLayout.this.mShouldPeekTopDrawerAfterLayout) {
                        WearableDrawerLayout.this.peekDrawer(48);
                        WearableDrawerLayout.this.mShouldPeekTopDrawerAfterLayout = false;
                    }

                }
            });
        }

    }

    public void peekDrawer(int gravity) {
        if(!this.isLaidOut()) {
                Log.d("WearableDrawerLayout", "WearableDrawerLayout not laid out yet. Postponing peek.");

            switch(gravity) {
                case 48:
                    this.mShouldPeekTopDrawerAfterLayout = true;
                    break;
                case 80:
                    this.mShouldPeekBottomDrawerAfterLayout = true;
            }

        } else {
            WearableDrawerView drawerView = this.findDrawerWithGravity(gravity);
            this.maybePeekDrawer(drawerView);
        }
    }

    public void peekDrawer(View drawer) {
        if(!this.isLaidOut()) {
                Log.d("WearableDrawerLayout", "WearableDrawerLayout not laid out yet. Postponing peek.");

            if(drawer == this.mTopDrawerView) {
                this.mShouldPeekTopDrawerAfterLayout = true;
            } else if(drawer == this.mBottomDrawerView) {
                this.mShouldPeekBottomDrawerAfterLayout = true;
            } else {
                Log.w("WearableDrawerLayout", "peekDrawer(View) should be passed in the top or bottom drawer");
            }

        } else {
            this.maybePeekDrawer((WearableDrawerView)drawer);
        }
    }

    public void onFlingComplete(View view) {
        boolean canTopPeek = this.mTopDrawerView != null && this.mTopDrawerView.canAutoPeek();
        boolean canBottomPeek = this.mBottomDrawerView != null && this.mBottomDrawerView.canAutoPeek();
        boolean canScrollUp = view.canScrollVertically(-1);
        boolean canScrollDown = view.canScrollVertically(1);
        if(canTopPeek && !canScrollUp && !this.mTopDrawerView.isPeeking()) {
            this.peekDrawer(48);
        }

        if(canBottomPeek && (!canScrollUp || !canScrollDown) && !this.mBottomDrawerView.isPeeking()) {
            this.peekDrawer(80);
        }

    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        this.maybeUpdateScrollingContentView(target);
        this.mLastScrollWasFling = true;
        if(target == this.mScrollingContentView) {
            this.mFlingWatcher.start(this.mScrollingContentView);
        }

        return false;
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        this.maybeUpdateScrollingContentView(target);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        boolean scrolledUp = dyConsumed < 0;
        boolean scrolledDown = dyConsumed > 0;
        boolean overScrolledUp = dyUnconsumed < 0;
        boolean overScrolledDown = dyUnconsumed > 0;
        if(this.mTopDrawerView != null && this.mTopDrawerView.isOpened()) {
            this.mCanTopDrawerBeClosed = overScrolledDown || !this.mTopDrawerView.getDrawerContent().canScrollVertically(1);
            if(this.mCanTopDrawerBeClosed && this.mLastScrollWasFling) {
                this.onTouchEvent(this.mDrawerOpenLastInterceptedTouchEvent);
            }

            this.mLastScrollWasFling = false;
        } else if(this.mBottomDrawerView != null && this.mBottomDrawerView.isOpened()) {
            this.mCanBottomDrawerBeClosed = overScrolledUp;
            if(this.mCanBottomDrawerBeClosed && this.mLastScrollWasFling) {
                this.onTouchEvent(this.mDrawerOpenLastInterceptedTouchEvent);
            }

            this.mLastScrollWasFling = false;
        } else {
            this.mLastScrollWasFling = false;
            boolean canTopAutoPeek = this.mTopDrawerView != null && this.mTopDrawerView.canAutoPeek();
            boolean canBottomAutoPeek = this.mBottomDrawerView != null && this.mBottomDrawerView.canAutoPeek();
            boolean isTopDrawerPeeking = this.mTopDrawerView != null && this.mTopDrawerView.isPeeking();
            boolean isBottomDrawerPeeking = this.mBottomDrawerView != null && this.mBottomDrawerView.isPeeking();
            boolean scrolledDownPastSlop = false;
            boolean shouldPeekOnScrollDown = this.mBottomDrawerView != null && this.mBottomDrawerView.shouldPeekOnScrollDown();
            if(scrolledDown) {
                this.mCurrentNestedScrollSlopTracker += dyConsumed;
                scrolledDownPastSlop = this.mCurrentNestedScrollSlopTracker > this.mNestedScrollSlopPx;
            }

            if(canTopAutoPeek) {
                if(overScrolledUp && !isTopDrawerPeeking) {
                    this.peekDrawer(48);
                } else if(scrolledDown && isTopDrawerPeeking && !this.isClosingPeek(this.mTopDrawerView)) {
                    this.closeDrawer(48);
                }
            }

            if(canBottomAutoPeek) {
                if((overScrolledDown || overScrolledUp) && !isBottomDrawerPeeking) {
                    this.peekDrawer(80);
                } else if(shouldPeekOnScrollDown && scrolledDownPastSlop && !isBottomDrawerPeeking) {
                    this.peekDrawer(80);
                } else if((scrolledUp || !shouldPeekOnScrollDown && scrolledDown) && isBottomDrawerPeeking && !this.isClosingPeek(this.mBottomDrawerView)) {
                    this.closeDrawer(this.mBottomDrawerView);
                }
            }

        }
    }

    private void maybePeekDrawer(WearableDrawerView drawerView) {
        if(drawerView != null) {
            ViewGroup peekView = drawerView.getPeekContainer();
            if(peekView != null) {
                View drawerContent = drawerView.getDrawerContent();
                int layoutGravity = ((android.widget.FrameLayout.LayoutParams)drawerView.getLayoutParams()).gravity;
                int gravity = layoutGravity == 0?drawerView.preferGravity():layoutGravity;
                drawerView.setIsPeeking(true);
                peekView.setAlpha(1.0F);
                peekView.setScaleX(1.0F);
                peekView.setScaleY(1.0F);
                peekView.setVisibility(VISIBLE);
                if(drawerContent != null) {
                    drawerContent.setAlpha(0.0F);
                }

                if(gravity == 80) {
                    this.mBottomDrawerDragger.smoothSlideViewTo(drawerView, 0, this.getHeight() - peekView.getHeight());
                } else if(gravity == 48) {
                    this.mTopDrawerDragger.smoothSlideViewTo(drawerView, 0, -(drawerView.getHeight() - peekView.getHeight()));
                    if(!this.mIsAccessibilityEnabled) {
                        this.closeDrawerDelayed(gravity, 1000L);
                    }
                }

                this.invalidate();
            }
        }
    }

    private void openDrawerWithoutAnimation(View drawer) {
        if(drawer != null) {
            WearableDrawerView view;
            int offset;
            if(drawer == this.mTopDrawerView) {
                view = this.mTopDrawerView;
                offset = this.mTopDrawerView.getHeight();
            } else {
                if(drawer != this.mBottomDrawerView) {
                    Log.w("WearableDrawerLayout", "openDrawer(View) should be passed in the top or bottom drawer");
                    return;
                }

                view = this.mBottomDrawerView;
                offset = -this.mBottomDrawerView.getHeight();
            }

            view.offsetTopAndBottom(offset);
            view.setOpenedPercent(1.0F);
            view.onDrawerOpened();
            if(this.mDrawerStateCallback != null) {
                this.mDrawerStateCallback.onDrawerOpened(view);
            }

            showDrawerContentMaybeAnimate(this.mTopDrawerView);
            this.invalidate();
        }
    }

    private void animatePeekVisible(WearableDrawerView drawer) {
        View content = drawer.getDrawerContent();
        if(content != null) {
            content.animate().setDuration(150L).alpha(0.0F).start();
        }

        ViewGroup peek = drawer.getPeekContainer();
        peek.setVisibility(VISIBLE);
        peek.animate().setStartDelay(150L).setDuration(150L).alpha(1.0F).scaleX(1.0F).scaleY(1.0F).start();
        drawer.setIsPeeking(true);
    }

    @Nullable
    private WearableDrawerView findDrawerWithGravity(int gravity) {
        switch(gravity) {
            case 48:
                return this.mTopDrawerView;
            case 80:
                return this.mBottomDrawerView;
            default:
                Log.w("WearableDrawerLayout", (new StringBuilder(35)).append("Invalid drawer gravity: ").append(gravity).toString());
                return null;
        }
    }

    private void maybeUpdateScrollingContentView(View view) {
        if(view != this.mScrollingContentView && !this.isDrawerOrChildOfDrawer(view)) {
            this.mScrollingContentView = view;
        }

    }

    private boolean isDrawerOrChildOfDrawer(View view) {
        while(view != null && view != this) {
            if(view instanceof WearableDrawerView) {
                return true;
            }

            view = (View)view.getParent();
        }

        return false;
    }

    private boolean isClosingPeek(WearableDrawerView drawerView) {
        return drawerView != null && drawerView.getDrawerState() == 2;
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        this.mCurrentNestedScrollSlopTracker = 0;
        return true;
    }

    public void onStopNestedScroll(View target) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(target);
    }

    private boolean canDrawerContentScrollVertically(@Nullable WearableDrawerView drawerView, int direction) {
        if(drawerView == null) {
            return false;
        } else {
            View drawerContent = drawerView.getDrawerContent();
            return drawerContent == null?false:drawerContent.canScrollVertically(direction);
        }
    }

    private static void showDrawerContentMaybeAnimate(WearableDrawerView drawerView) {
        drawerView.bringToFront();
        View contentView = drawerView.getDrawerContent();
        if(drawerView.isPeeking()) {
            ViewGroup peekView = drawerView.getPeekContainer();
            peekView.animate().alpha(0.0F).scaleX(0.0F).scaleY(0.0F).setDuration(150L).start();
            if(contentView != null) {
                contentView.setAlpha(0.0F);
                contentView.animate().setStartDelay(150L).alpha(1.0F).setDuration(150L).start();
            }
        } else {
            drawerView.getPeekContainer().setAlpha(0.0F);
            if(contentView != null) {
                contentView.setAlpha(1.0F);
            }
        }

    }

    public abstract static class DrawerStateCallback {
        public DrawerStateCallback() {
        }

        public abstract void onDrawerOpened(View var1);

        public abstract void onDrawerClosed(View var1);

        public abstract void onDrawerStateChanged(@DrawerState int var1);
    }

    private class ClosePeekRunnable implements Runnable {
        private final int gravity;

        private ClosePeekRunnable(int gravity) {
            this.gravity = gravity;
        }

        public void run() {
            WearableDrawerView drawer = WearableDrawerLayout.this.findDrawerWithGravity(this.gravity);
            if(drawer != null && !drawer.isOpened() && drawer.getDrawerState() == 0) {
                WearableDrawerLayout.this.closeDrawer(this.gravity);
            }

        }
    }

    private class BottomDrawerDraggerCallback extends WearableDrawerLayout.DrawerDraggerCallback {
        private BottomDrawerDraggerCallback() {
            super();
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            if(WearableDrawerLayout.this.mBottomDrawerView == child) {
                int parentHeight = WearableDrawerLayout.this.getHeight();
                int peekHeight = WearableDrawerLayout.this.mBottomDrawerView.getPeekContainer().getHeight();
                return Math.max(parentHeight - child.getHeight(), Math.min(top, parentHeight - peekHeight));
            } else {
                return 0;
            }
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            if(WearableDrawerLayout.this.mBottomDrawerView != null && edgeFlags == 8 && !WearableDrawerLayout.this.mBottomDrawerView.isLocked() && (WearableDrawerLayout.this.mTopDrawerView == null || !WearableDrawerLayout.this.mTopDrawerView.isOpened()) && WearableDrawerLayout.this.mBottomDrawerView.hasDrawerContent()) {
                WearableDrawerLayout.this.mBottomDrawerDragger.captureChildView(WearableDrawerLayout.this.mBottomDrawerView, pointerId);
            }

        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if(releasedChild == WearableDrawerLayout.this.mBottomDrawerView) {
                int parentHeight = WearableDrawerLayout.this.getHeight();
                float openedPercent = WearableDrawerLayout.this.mBottomDrawerView.getOpenedPercent();
                int finalTop;
                if(yvel >= 0.0F && (yvel != 0.0F || openedPercent <= 0.5F)) {
                    WearableDrawerLayout.this.animatePeekVisible(WearableDrawerLayout.this.mBottomDrawerView);
                    finalTop = WearableDrawerLayout.this.getHeight() - WearableDrawerLayout.this.mBottomDrawerView.getPeekContainer().getHeight();
                } else {
                    finalTop = parentHeight - releasedChild.getHeight();
                }

                WearableDrawerLayout.this.mBottomDrawerDragger.settleCapturedViewAt(0, finalTop);
                WearableDrawerLayout.this.invalidate();
            }

        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if(changedView == WearableDrawerLayout.this.mBottomDrawerView) {
                int height = changedView.getHeight();
                int parentHeight = WearableDrawerLayout.this.getHeight();
                WearableDrawerLayout.this.mBottomDrawerView.setOpenedPercent((float)(parentHeight - top) / (float)height);
                WearableDrawerLayout.this.invalidate();
            }

        }

        public WearableDrawerView getDrawerView() {
            return WearableDrawerLayout.this.mBottomDrawerView;
        }
    }

    private class TopDrawerDraggerCallback extends WearableDrawerLayout.DrawerDraggerCallback {
        private TopDrawerDraggerCallback() {
            super();
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            if(WearableDrawerLayout.this.mTopDrawerView == child) {
                int peekHeight = WearableDrawerLayout.this.mTopDrawerView.getPeekContainer().getHeight();
                return Math.max(peekHeight - child.getHeight(), Math.min(top, 0));
            } else {
                return 0;
            }
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            if(WearableDrawerLayout.this.mTopDrawerView != null && edgeFlags == 4 && !WearableDrawerLayout.this.mTopDrawerView.isLocked() && (WearableDrawerLayout.this.mBottomDrawerView == null || !WearableDrawerLayout.this.mBottomDrawerView.isOpened()) && WearableDrawerLayout.this.mTopDrawerView.hasDrawerContent()) {
                boolean atTop = WearableDrawerLayout.this.mScrollingContentView == null || !WearableDrawerLayout.this.mScrollingContentView.canScrollVertically(-1);
                if(!WearableDrawerLayout.this.mTopDrawerView.shouldOnlyOpenWhenAtTop() || atTop) {
                    WearableDrawerLayout.this.mTopDrawerDragger.captureChildView(WearableDrawerLayout.this.mTopDrawerView, pointerId);
                }
            }

        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if(releasedChild == WearableDrawerLayout.this.mTopDrawerView) {
                float openedPercent = WearableDrawerLayout.this.mTopDrawerView.getOpenedPercent();
                int finalTop;
                if(yvel <= 0.0F && (yvel != 0.0F || openedPercent <= 0.5F)) {
                    WearableDrawerLayout.this.animatePeekVisible(WearableDrawerLayout.this.mTopDrawerView);
                    finalTop = WearableDrawerLayout.this.mTopDrawerView.getPeekContainer().getHeight() - releasedChild.getHeight();
                } else {
                    finalTop = 0;
                }

                WearableDrawerLayout.this.mTopDrawerDragger.settleCapturedViewAt(0, finalTop);
                WearableDrawerLayout.this.invalidate();
            }

        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if(changedView == WearableDrawerLayout.this.mTopDrawerView) {
                int height = changedView.getHeight();
                WearableDrawerLayout.this.mTopDrawerView.setOpenedPercent((float)(top + height) / (float)height);
                WearableDrawerLayout.this.invalidate();
            }

        }

        public WearableDrawerView getDrawerView() {
            return WearableDrawerLayout.this.mTopDrawerView;
        }
    }

    private abstract class DrawerDraggerCallback extends Callback {
        private DrawerDraggerCallback() {
        }

        public abstract WearableDrawerView getDrawerView();

        public boolean tryCaptureView(View child, int pointerId) {
            WearableDrawerView drawerView = this.getDrawerView();
            return child == drawerView && !drawerView.isLocked() && drawerView.hasDrawerContent();
        }

        public int getViewVerticalDragRange(View child) {
            return child == this.getDrawerView()?child.getHeight():0;
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
            WearableDrawerLayout.showDrawerContentMaybeAnimate((WearableDrawerView)capturedChild);
        }

        public void onViewDragStateChanged(int state) {
            WearableDrawerView drawerView = this.getDrawerView();
            switch(state) {
                case 0:
                    boolean openedOrClosed = false;
                    if(drawerView.isOpened()) {
                        openedOrClosed = true;
                        drawerView.onDrawerOpened();
                        if(WearableDrawerLayout.this.mDrawerStateCallback != null) {
                            WearableDrawerLayout.this.mDrawerStateCallback.onDrawerOpened(drawerView);
                        }

                        WearableDrawerLayout.this.mCanTopDrawerBeClosed = !WearableDrawerLayout.this.canDrawerContentScrollVertically(WearableDrawerLayout.this.mTopDrawerView, 1);
                        WearableDrawerLayout.this.mCanBottomDrawerBeClosed = !WearableDrawerLayout.this.canDrawerContentScrollVertically(WearableDrawerLayout.this.mBottomDrawerView, -1);
                    } else if(drawerView.isClosed()) {
                        openedOrClosed = true;
                        drawerView.onDrawerClosed();
                        if(WearableDrawerLayout.this.mDrawerStateCallback != null) {
                            WearableDrawerLayout.this.mDrawerStateCallback.onDrawerClosed(drawerView);
                        }
                    }

                    if(openedOrClosed && drawerView.isPeeking()) {
                        drawerView.setIsPeeking(false);
                        drawerView.getPeekContainer().setVisibility(INVISIBLE);
                    }
                default:
                    if(drawerView.getDrawerState() != state) {
                        drawerView.setDrawerState(state);
                        drawerView.onDrawerStateChanged(state);
                        if(WearableDrawerLayout.this.mDrawerStateCallback != null) {
                            WearableDrawerLayout.this.mDrawerStateCallback.onDrawerStateChanged(state);
                        }
                    }

            }
        }
    }
}
