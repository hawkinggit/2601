//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.view.drawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import com.mstarc.wearablelauncher.R.dimen;
import com.mstarc.wearablelauncher.R.drawable;
import com.mstarc.wearablelauncher.R.id;
import com.mstarc.wearablelauncher.R.layout;
import com.mstarc.wearablelauncher.R.styleable;
import android.support.wearable.view.drawer.WearableDrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TargetApi(23)
public class WearableDrawerView extends FrameLayout {
    private final ViewGroup mPeekContainer;
    private final ImageView mPeekIcon;
    private View mContent;
    private WearableDrawerLayout mParent;
    private float mOpenedPercent;
    private boolean mIsLocked;
    private boolean mCanAutoPeek;
    private boolean mShouldLockWhenNotOpenOrPeeking;
    private boolean mOnlyOpenWhenAtTop;
    private boolean mShouldPeekOnScrollDown;
    private boolean mIsPeeking;
    @WearableDrawerView.DrawerState
    private int mDrawerState;
    @IdRes
    private int mPeekResId;
    @IdRes
    private int mContentResId;
    private static final int[] COLOR_ATTRS = new int[]{16844002};
    private static final int BACKGROUND_COLOR_INDEX = 0;

    public WearableDrawerView(Context context) {
        this(context, (AttributeSet)null);
    }

    public WearableDrawerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WearableDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mIsLocked = false;
        this.mCanAutoPeek = true;
        this.mShouldLockWhenNotOpenOrPeeking = false;
        this.mOnlyOpenWhenAtTop = false;
        this.mShouldPeekOnScrollDown = false;
        this.mPeekResId = 0;
        this.mContentResId = 0;
        LayoutInflater.from(context).inflate(layout.wearable_drawer_view, this, true);
        this.setClickable(true);
        this.setElevation(context.getResources().getDimension(dimen.wearable_drawer_view_elevation));
        this.mPeekContainer = (ViewGroup)this.findViewById(id.wearable_support_drawer_view_peek_container);
        this.mPeekIcon = (ImageView)this.findViewById(id.wearable_support_drawer_view_peek_icon);
        this.mPeekContainer.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WearableDrawerView.this.onPeekContainerClicked(v);
            }
        });
        this.parseAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPeekContainer.bringToFront();
    }

    public void onPeekContainerClicked(View v) {
        this.openDrawer();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LayoutParams peekParams = (LayoutParams)this.mPeekContainer.getLayoutParams();
        if(!Gravity.isVertical(peekParams.gravity)) {
            boolean isTopDrawer = (((LayoutParams)this.getLayoutParams()).gravity & 112) == 48;
            if(isTopDrawer) {
                peekParams.gravity = 80;
                this.mPeekIcon.setImageResource(drawable.ic_more_horiz_24dp_wht);
            } else {
                peekParams.gravity = 48;
                this.mPeekIcon.setImageResource(drawable.ic_more_vert_24dp_wht);
            }

            this.mPeekContainer.setLayoutParams(peekParams);
        }

    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        int childId = child.getId();
        if(childId != 0) {
            if(childId == this.mPeekResId) {
                this.setPeekContent(child, index, params);
                return;
            }

            if(childId == this.mContentResId && !this.setDrawerContentWithoutAdding(child)) {
                return;
            }
        }

        super.addView(child, index, params);
    }

    int preferGravity() {
        return 0;
    }

    ViewGroup getPeekContainer() {
        return this.mPeekContainer;
    }

    public void setDrawerContent(@Nullable View content) {
        if(this.setDrawerContentWithoutAdding(content)) {
            this.addView(content);
        }

    }

    public boolean hasDrawerContent() {
        return this.mContent != null;
    }

    @Nullable
    public View getDrawerContent() {
        return this.mContent;
    }

    public void setPeekContent(View content) {
        android.view.ViewGroup.LayoutParams layoutParams = content.getLayoutParams();
        this.setPeekContent(content, -1, (android.view.ViewGroup.LayoutParams)(layoutParams != null?layoutParams:this.generateDefaultLayoutParams()));
    }

    public void openDrawer() {
        this.getWearableDrawerLayout().openDrawer(this);
    }

    public void closeDrawer() {
        this.getWearableDrawerLayout().closeDrawer(this);
    }

    public void peekDrawer() {
        this.getWearableDrawerLayout().peekDrawer(this);
    }

    public void onDrawerOpened() {
    }

    public void onDrawerClosed() {
    }

    public void onDrawerStateChanged(@WearableDrawerView.DrawerState int state) {
    }

    public void setShouldOnlyOpenWhenAtTop(boolean onlyOpenWhenAtTop) {
        this.mOnlyOpenWhenAtTop = onlyOpenWhenAtTop;
    }

    public boolean shouldOnlyOpenWhenAtTop() {
        return this.mOnlyOpenWhenAtTop;
    }

    public void setShouldPeekOnScrollDown(boolean shouldPeekOnScrollDown) {
        this.mShouldPeekOnScrollDown = shouldPeekOnScrollDown;
    }

    public boolean shouldPeekOnScrollDown() {
        return this.mShouldPeekOnScrollDown;
    }

    public void setShouldLockWhenNotOpenOrPeeking(boolean shouldLockWhenNotOpenOrPeeking) {
        this.mShouldLockWhenNotOpenOrPeeking = shouldLockWhenNotOpenOrPeeking;
    }

    public boolean shouldLockWhenNotOpenOrPeeking() {
        return this.mShouldLockWhenNotOpenOrPeeking;
    }

    @WearableDrawerView.DrawerState
    public int getDrawerState() {
        return this.mDrawerState;
    }

    public boolean isPeeking() {
        return this.mIsPeeking;
    }

    public boolean canAutoPeek() {
        return this.mCanAutoPeek && !this.mIsLocked;
    }

    public void setCanAutoPeek(boolean canAutoPeek) {
        this.mCanAutoPeek = canAutoPeek;
    }

    public boolean isLocked() {
        return this.mIsLocked || this.shouldLockWhenNotOpenOrPeeking() && this.mOpenedPercent <= 0.0F;
    }

    public void lockDrawerClosed() {
        this.closeDrawer();
        this.mIsLocked = true;
    }

    public void lockDrawerOpened() {
        this.openDrawer();
        this.mIsLocked = true;
    }

    public void unlockDrawer() {
        this.mIsLocked = false;
    }

    public boolean isOpened() {
        return this.mOpenedPercent == 1.0F;
    }

    public boolean isClosed() {
        return this.mOpenedPercent == 0.0F;
    }

    void setDrawerState(@WearableDrawerView.DrawerState int drawerState) {
        this.mDrawerState = drawerState;
    }

    void setIsPeeking(boolean isPeeking) {
        this.mIsPeeking = isPeeking;
    }

    float getOpenedPercent() {
        return this.mOpenedPercent;
    }

    void setOpenedPercent(float openedPercent) {
        this.mOpenedPercent = openedPercent;
    }

    private void parseAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.setDefaultBackgroundIfNonePresent(context);
        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, styleable.WearableDrawerView, defStyleAttr, defStyleRes);
            this.mContentResId = typedArray.getResourceId(styleable.WearableDrawerView_drawer_content, 0);
            this.mPeekResId = typedArray.getResourceId(styleable.WearableDrawerView_peek_view, 0);
            typedArray.recycle();
        }
    }

    private void setDefaultBackgroundIfNonePresent(Context context) {
        boolean noBackgroundSetByUser = this.getBackground() == null;
        if(noBackgroundSetByUser) {
            this.setBackgroundColor(this.getDefaultBackgroundColor(context));
        }

    }

    private int getDefaultBackgroundColor(Context context) {
        return context.obtainStyledAttributes(COLOR_ATTRS).getColor(0, 0);
    }

    private void setPeekContent(View content, int index, android.view.ViewGroup.LayoutParams params) {
        if(content != null) {
            if(this.mPeekContainer.getChildCount() > 0) {
                this.mPeekContainer.removeAllViews();
            }

            this.mPeekContainer.addView(content, index, params);
        }
    }

    private boolean setDrawerContentWithoutAdding(View content) {
        if(content == this.mContent) {
            return false;
        } else {
            if(this.mContent != null) {
                this.removeView(this.mContent);
            }

            this.mContent = content;
            return this.mContent != null;
        }
    }

    private WearableDrawerLayout getWearableDrawerLayout() {
        if(this.mParent == null) {
            this.mParent = (WearableDrawerLayout)this.getParent();
        }

        return this.mParent;
    }

    @Retention(RetentionPolicy.CLASS)
    public @interface DrawerState {
        int STATE_IDLE = 0;
        int STATE_DRAGGING = 1;
        int STATE_SETTLING = 2;
    }
}
