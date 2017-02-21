//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.view.drawer;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.support.wearable.R.style;
//import android.support.wearable.R.styleable;
import com.mstarc.wearablelauncher.R.styleable;
import com.mstarc.wearablelauncher.R.style;
import android.support.wearable.view.SimpleAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import java.util.concurrent.TimeUnit;

public class PageIndicatorView extends View implements OnPageChangeListener {
    private static final String TAG = "Dots";
    private int mDotSpacing;
    private float mDotRadius;
    private float mDotRadiusSelected;
    private int mDotColor;
    private int mDotColorSelected;
    private boolean mDotFadeWhenIdle;
    private int mDotFadeOutDelay;
    private int mDotFadeOutDuration;
    private int mDotFadeInDuration;
    private float mDotShadowDx;
    private float mDotShadowDy;
    private float mDotShadowRadius;
    private int mDotShadowColor;
    private PagerAdapter mAdapter;
    private int mNumberOfPositions;
    private int mSelectedPosition;
    private int mCurrentViewPagerState;
    private final Paint mDotPaint;
    private final Paint mDotPaintShadow;
    private final Paint mDotPaintSelected;
    private final Paint mDotPaintShadowSelected;
    private boolean mVisible;

    public PageIndicatorView(Context context) {
        this(context, (AttributeSet)null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, styleable.PageIndicatorView, defStyleAttr, style.PageIndicatorViewStyle);
        this.mDotSpacing = a.getDimensionPixelOffset(styleable.PageIndicatorView_pageIndicatorDotSpacing, 0);
        this.mDotRadius = a.getDimension(styleable.PageIndicatorView_pageIndicatorDotRadius, 0.0F);
        this.mDotRadiusSelected = a.getDimension(styleable.PageIndicatorView_pageIndicatorDotRadiusSelected, 0.0F);
        this.mDotColor = a.getColor(styleable.PageIndicatorView_pageIndicatorDotColor, 0);
        this.mDotColorSelected = a.getColor(styleable.PageIndicatorView_pageIndicatorDotColorSelected, 0);
        this.mDotFadeOutDelay = a.getInt(styleable.PageIndicatorView_pageIndicatorDotFadeOutDelay, 0);
        this.mDotFadeOutDuration = a.getInt(styleable.PageIndicatorView_pageIndicatorDotFadeOutDuration, 0);
        this.mDotFadeInDuration = a.getInt(styleable.PageIndicatorView_pageIndicatorDotFadeInDuration, 0);
        this.mDotFadeWhenIdle = a.getBoolean(styleable.PageIndicatorView_pageIndicatorDotFadeWhenIdle, false);
        this.mDotShadowDx = a.getDimension(styleable.PageIndicatorView_pageIndicatorDotShadowDx, 0.0F);
        this.mDotShadowDy = a.getDimension(styleable.PageIndicatorView_pageIndicatorDotShadowDy, 0.0F);
        this.mDotShadowRadius = a.getDimension(styleable.PageIndicatorView_pageIndicatorDotShadowRadius, 0.0F);
        this.mDotShadowColor = a.getColor(styleable.PageIndicatorView_pageIndicatorDotShadowColor, 0);
        a.recycle();
        this.mDotPaint = new Paint(1);
        this.mDotPaint.setColor(this.mDotColor);
        this.mDotPaint.setStyle(Style.FILL);
        this.mDotPaintSelected = new Paint(1);
        this.mDotPaintSelected.setColor(this.mDotColorSelected);
        this.mDotPaintSelected.setStyle(Style.FILL);
        this.mDotPaintShadow = new Paint(1);
        this.mDotPaintShadowSelected = new Paint(1);
        this.mCurrentViewPagerState = 0;
        if(this.isInEditMode()) {
            this.mNumberOfPositions = 5;
            this.mSelectedPosition = 2;
            this.mDotFadeWhenIdle = false;
        }

        if(this.mDotFadeWhenIdle) {
            this.mVisible = false;
            this.animate().alpha(0.0F).setStartDelay(2000L).setDuration((long)this.mDotFadeOutDuration).start();
        } else {
            this.animate().cancel();
            this.setAlpha(1.0F);
        }

        this.updateShadows();
    }

    private void updateShadows() {
        this.updateDotPaint(this.mDotPaint, this.mDotPaintShadow, this.mDotRadius, this.mDotShadowRadius, this.mDotColor, this.mDotShadowColor);
        this.updateDotPaint(this.mDotPaintSelected, this.mDotPaintShadowSelected, this.mDotRadiusSelected, this.mDotShadowRadius, this.mDotColorSelected, this.mDotShadowColor);
    }

    private void updateDotPaint(Paint dotPaint, Paint shadowPaint, float baseRadius, float shadowRadius, int color, int shadowColor) {
        float radius = baseRadius + shadowRadius;
        float shadowStart = baseRadius / radius;
        RadialGradient gradient = new RadialGradient(0.0F, 0.0F, radius, new int[]{shadowColor, shadowColor, 0}, new float[]{0.0F, shadowStart, 1.0F}, TileMode.CLAMP);
        shadowPaint.setShader(gradient);
        dotPaint.setColor(color);
        dotPaint.setStyle(Style.FILL);
    }

    public void setPager(ViewPager pager) {
        pager.addOnPageChangeListener(this);
        this.setPagerAdapter(pager.getAdapter());
        this.mAdapter = pager.getAdapter();
        if(this.mAdapter != null && this.mAdapter.getCount() > 0) {
            this.positionChanged(0);
        }

    }

    public float getDotSpacing() {
        return (float)this.mDotSpacing;
    }

    public void setDotSpacing(int spacing) {
        if(this.mDotSpacing != spacing) {
            this.mDotSpacing = spacing;
            this.requestLayout();
        }

    }

    public float getDotRadius() {
        return this.mDotRadius;
    }

    public void setDotRadius(int radius) {
        if(this.mDotRadius != (float)radius) {
            this.mDotRadius = (float)radius;
            this.updateShadows();
            this.invalidate();
        }

    }

    public float getDotRadiusSelected() {
        return this.mDotRadiusSelected;
    }

    public void setDotRadiusSelected(int radius) {
        if(this.mDotRadiusSelected != (float)radius) {
            this.mDotRadiusSelected = (float)radius;
            this.updateShadows();
            this.invalidate();
        }

    }

    public int getDotColor() {
        return this.mDotColor;
    }

    public void setDotColor(int color) {
        if(this.mDotColor != color) {
            this.mDotColor = color;
            this.invalidate();
        }

    }

    public int getDotColorSelected() {
        return this.mDotColorSelected;
    }

    public void setDotColorSelected(int color) {
        if(this.mDotColorSelected != color) {
            this.mDotColorSelected = color;
            this.invalidate();
        }

    }

    public boolean getDotFadeWhenIdle() {
        return this.mDotFadeWhenIdle;
    }

    public void setDotFadeWhenIdle(boolean fade) {
        this.mDotFadeWhenIdle = fade;
        if(!fade) {
            this.fadeIn();
        }

    }

    public int getDotFadeOutDuration() {
        return this.mDotFadeOutDuration;
    }

    public void setDotFadeOutDuration(int duration, TimeUnit unit) {
        this.mDotFadeOutDuration = (int)TimeUnit.MILLISECONDS.convert((long)duration, unit);
    }

    public int getDotFadeInDuration() {
        return this.mDotFadeInDuration;
    }

    public void setDotFadeInDuration(int duration, TimeUnit unit) {
        this.mDotFadeInDuration = (int)TimeUnit.MILLISECONDS.convert((long)duration, unit);
    }

    public int getDotFadeOutDelay() {
        return this.mDotFadeOutDelay;
    }

    public void setDotFadeOutDelay(int delay) {
        this.mDotFadeOutDelay = delay;
    }

    public float getDotShadowRadius() {
        return this.mDotShadowRadius;
    }

    public void setDotShadowRadius(float radius) {
        if(this.mDotShadowRadius != radius) {
            this.mDotShadowRadius = radius;
            this.updateShadows();
            this.invalidate();
        }

    }

    public float getDotShadowDx() {
        return this.mDotShadowDx;
    }

    public void setDotShadowDx(float dx) {
        this.mDotShadowDx = dx;
        this.invalidate();
    }

    public float getDotShadowDy() {
        return this.mDotShadowDy;
    }

    public void setDotShadowDy(float dy) {
        this.mDotShadowDy = dy;
        this.invalidate();
    }

    public int getDotShadowColor() {
        return this.mDotShadowColor;
    }

    public void setDotShadowColor(int color) {
        this.mDotShadowColor = color;
        this.updateShadows();
        this.invalidate();
    }

    private void positionChanged(int position) {
        this.mSelectedPosition = position;
        this.invalidate();
    }

    private void updateNumberOfPositions() {
        int count = this.mAdapter.getCount();
        if(count != this.mNumberOfPositions) {
            this.mNumberOfPositions = count;
            this.requestLayout();
        }

    }

    private void fadeIn() {
        this.mVisible = true;
        this.animate().cancel();
        this.animate().alpha(1.0F).setStartDelay(0L).setDuration((long)this.mDotFadeInDuration).start();
    }

    private void fadeOut(long delayMillis) {
        this.mVisible = false;
        this.animate().cancel();
        this.animate().alpha(0.0F).setStartDelay(delayMillis).setDuration((long)this.mDotFadeOutDuration).start();
    }

    private void fadeInOut() {
        this.mVisible = true;
        this.animate().cancel();
        this.animate().alpha(1.0F).setStartDelay(0L).setDuration((long)this.mDotFadeInDuration).setListener(new SimpleAnimatorListener() {
            public void onAnimationComplete(Animator animator) {
                PageIndicatorView.this.mVisible = false;
                PageIndicatorView.this.animate().alpha(0.0F).setListener((AnimatorListener)null).setStartDelay((long)PageIndicatorView.this.mDotFadeOutDelay).setDuration((long)PageIndicatorView.this.mDotFadeOutDuration).start();
            }
        }).start();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(this.mDotFadeWhenIdle && this.mCurrentViewPagerState == 1) {
            if(positionOffset != 0.0F) {
                if(!this.mVisible) {
                    this.fadeIn();
                }
            } else if(this.mVisible) {
                this.fadeOut(0L);
            }
        }

    }

    public void onPageSelected(int position) {
        if(position != this.mSelectedPosition) {
            this.positionChanged(position);
        }

    }

    public void onPageScrollStateChanged(int state) {
        if(this.mCurrentViewPagerState != state) {
            this.mCurrentViewPagerState = state;
            if(this.mDotFadeWhenIdle && state == 0) {
                if(this.mVisible) {
                    this.fadeOut((long)this.mDotFadeOutDelay);
                } else {
                    this.fadeInOut();
                }
            }
        }

    }

    public void setPagerAdapter(PagerAdapter adapter) {
        this.mAdapter = adapter;
        if(this.mAdapter != null) {
            this.updateNumberOfPositions();
            if(this.mDotFadeWhenIdle) {
                this.fadeInOut();
            }
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth;
        int totalHeight;
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            totalHeight = this.mNumberOfPositions * this.mDotSpacing;
            totalWidth = totalHeight + this.getPaddingLeft() + this.getPaddingRight();
        }

        if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            totalHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            float maxRadius = Math.max(this.mDotRadius + this.mDotShadowRadius, this.mDotRadiusSelected + this.mDotShadowRadius);
            int contentHeight = (int)Math.ceil((double)(maxRadius * 2.0F));
            contentHeight = (int)((float)contentHeight + this.mDotShadowDy);
            totalHeight = contentHeight + this.getPaddingTop() + this.getPaddingBottom();
        }

        this.setMeasuredDimension(resolveSizeAndState(totalWidth, widthMeasureSpec, 0), resolveSizeAndState(totalHeight, heightMeasureSpec, 0));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.mNumberOfPositions > 1) {
            float dotCenterLeft = (float)this.getPaddingLeft() + (float)this.mDotSpacing / 2.0F;
            float dotCenterTop = (float)this.getHeight() / 2.0F;
            canvas.save();
            canvas.translate(dotCenterLeft, dotCenterTop);

            for(int i = 0; i < this.mNumberOfPositions; ++i) {
                float radius;
                if(i == this.mSelectedPosition) {
                    radius = this.mDotRadiusSelected + this.mDotShadowRadius;
                    canvas.drawCircle(this.mDotShadowDx, this.mDotShadowDy, radius, this.mDotPaintShadowSelected);
                    canvas.drawCircle(0.0F, 0.0F, this.mDotRadiusSelected, this.mDotPaintSelected);
                } else {
                    radius = this.mDotRadius + this.mDotShadowRadius;
                    canvas.drawCircle(this.mDotShadowDx, this.mDotShadowDy, radius, this.mDotPaintShadow);
                    canvas.drawCircle(0.0F, 0.0F, this.mDotRadius, this.mDotPaint);
                }

                canvas.translate((float)this.mDotSpacing, 0.0F);
            }

            canvas.restore();
        }

    }

    public void notifyDataSetChanged() {
        if(this.mAdapter != null && this.mAdapter.getCount() > 0) {
            this.updateNumberOfPositions();
        }

    }
}
