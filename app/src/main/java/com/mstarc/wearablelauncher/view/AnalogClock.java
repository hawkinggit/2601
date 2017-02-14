package com.mstarc.wearablelauncher.view;

/**
 * Created by wangxinzhi on 17-2-12.
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mstarc.wearablelauncher.R;

import java.lang.String;
import java.util.TimeZone;

/**
 * This widget display an analogic clock with two hands for hours and minutes.
 */
public class AnalogClock extends View {
    private Time mCalendar;

    private Drawable mHourHand;

    private Drawable mMinuteHand;

    private Drawable mDial;

    private Drawable mNail;

//    private Drawable mIndicator;

    private int mDialWidth;

    private int mDialHeight;

    private boolean mAttached;

    private static boolean sShowIndicator = false;

    private Context mContext;

    private final Handler mHandler = new Handler();

    private float mMinutes;

    private float mHour;

    private boolean mChanged;

    /**
     * Create a new instance.
     *
     * @param context The application environment.
     */
    public AnalogClock(Context context) {
        this(context, null);
    }

    /**
     * Create a new instance.
     *
     * @param context The application environment.
     * @param attrs A collection of attributes.
     */
    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Create a new instance.
     *
     * @param context The application environment.
     * @param attrs A collection of attributes.
     * @param defStyle The default style to apply to this view.
     */
    public AnalogClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        Resources r = mContext.getResources();

        Log.i(TAG, "Create a new instance.");
        mDial = r.getDrawable(R.drawable.clock_bg_black);
        mHourHand = r.getDrawable(R.drawable.clock_hour_black);
        mMinuteHand = r.getDrawable(R.drawable.clock_min_black);
        mNail = r.getDrawable(R.drawable.clock_nail_black);
//        mIndicator = r.getDrawable(R.drawable.idle_indicator);

        mCalendar = new Time();

        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();

        /// Set rtc alarm: declare.
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mAlarmIntent = new Intent("MYALARMRECEIVER");
        mPI = PendingIntent.getBroadcast(mContext, 0, mAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.i(TAG, "onAttachedToWindow()");

        if (!mAttached) {
            mAttached = true;

            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
//            filter.addAction(NotificationHelper.ACTION_NOTIFICATION_SHOW_INDICATOR);
//            filter.addAction(NotificationHelper.ACTION_NOTIFICATION_HIDE_INDICATOR);

            getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
        }

        // NOTE: It's safe to do these after registering the receiver since the
        // receiver always runs
        // in the main thread, therefore the receiver can't run before this
        // method returns.

        // The time zone may have changed while the receiver wasn't registered,
        // so update the Time
        mCalendar = new Time();

        //
        PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        updateAlarmTimer(!pm.isScreenOn());

        // Make sure we update to the current time
        onTimeChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        Log.i(TAG, "onDetachedFromWindow()");

        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        int availableWidth = getRight() - getLeft();
        int availableHeight = getBottom() - getTop();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w, (float) availableHeight
                    / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);

        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        int offset = hourHand.getIntrinsicHeight() / 5;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
//            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
            hourHand.setBounds(x - (w / 2), y - h + offset, x + (w / 2), y + offset);
        }
        hourHand.draw(canvas);
        canvas.restore();

        canvas.save();

        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - h + offset, x + (w / 2), y + offset);
        }
        minuteHand.draw(canvas);
        canvas.restore();

        final Drawable mNailDrawable = mNail;
        if (mNailDrawable != null) {
            canvas.save();
            if (changed) {
                w = mNailDrawable.getIntrinsicWidth();
                h = mNailDrawable.getIntrinsicHeight();
                mNailDrawable.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
            }
            mNailDrawable.draw(canvas);
            canvas.restore();
        }

        // Notification Indicator.
//        if (sShowIndicator) {
//            final Drawable mIndicatorDrawable = mIndicator;
//            canvas.save();
//            if (changed) {
//                w = mIndicatorDrawable.getIntrinsicWidth();
//                h = mIndicatorDrawable.getIntrinsicHeight();
//                mIndicatorDrawable.setBounds(0, 16, w, 16 + h);
//            }
//            mIndicatorDrawable.draw(canvas);
//            canvas.restore();
//        }

        if (scaled) {
            canvas.restore();
        }
    }

    private void onTimeChanged() {
        mCalendar.setToNow();

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = 0; //mCalendar.second;

        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;

        updateContentDescription(mCalendar);
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.i(TAG, "Intent received" + intent);

            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
//
//            if (action.equals(
//                    NotificationHelper.ACTION_NOTIFICATION_SHOW_INDICATOR)) {
//                sShowIndicator = true;
//            } else if (action.equals(
//                    NotificationHelper.ACTION_NOTIFICATION_HIDE_INDICATOR)) {
//                sShowIndicator = false;
//            } else
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Log.i(TAG, "Intent.ACTION_SCREEN_ON");
                updateAlarmTimer(false);
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i(TAG, "Intent.ACTION_SCREEN_OFF");
                updateAlarmTimer(true);
            } else if (action.equals("MYALARMRECEIVER")) {  // handle alarm event
                Log.i(TAG, "Alarm Update time");
            }

            onTimeChanged();

            invalidate();
        }
    };

    private void updateContentDescription(Time time) {
        final int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR;
        String contentDescription = DateUtils.formatDateTime(mContext, time.toMillis(false), flags);
        setContentDescription(contentDescription);
    }

    /// Set rtc alarm: variable definition.
    private static final int ALARM_SEC = 60;
    private static final String TAG = "AnalogClock";
    private static final String PROPERTY_STANDBY_DISPLAY = "persist.sys.standby.display";
    private AlarmManager mAlarmManager;
    private Intent mAlarmIntent;
    private PendingIntent mPI;
    private final IntentFilter filter = new IntentFilter("MYALARMRECEIVER");

    private void updateAlarmTimer(boolean isScreenOff) {
//        boolean isDispalyModeOn = SystemProperties.getBoolean(PROPERTY_STANDBY_DISPLAY, false);
        boolean enable = isScreenOff;

        Log.d(TAG, "updateAlarmTimer: " + (enable ? "enabled" : "disabled")
                + " screenOff= " + isScreenOff);

        if (enable) {
            if (mAlarmManager != null) {
                setAlarm();
            }
        } else {
            if (mAlarmManager != null) {
                mAlarmManager.cancel(mPI);
            }
        }
    }

    private void setAlarm() {
        long currTime = System.currentTimeMillis() / 1000 * 1000; // truncate milliseconds
        long secondSpan  = 60 - ((currTime / 1000) % 60); // span seconds to next minute
        long alarmTime = currTime + secondSpan * 1000;

        Log.i(TAG, "setAlarm, currentTime=" + currTime + " alarmTime=" + alarmTime);

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                alarmTime, ALARM_SEC * 1000, mPI);
    }
}