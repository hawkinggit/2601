
package android.support.wearable.view.drawer;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;
import android.view.View;

public class FlingWatcher {
    private static final int POLLING_DELAY_MS = 100;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final FlingWatcher.FlingListener mFlingListener;
    private boolean mIsRunning = false;
    private View mView;
    private int mLastScrollY;
    private final Runnable mCheckForChangeRunnable = new Runnable() {
        public void run() {
            FlingWatcher.this.checkForChange();
        }
    };

    public FlingWatcher(FlingWatcher.FlingListener flingListener) {
        this.mFlingListener = flingListener;
    }

    public void start(View view) {
        if(!this.mIsRunning) {
            this.mIsRunning = true;
            this.mView = view;
            this.mLastScrollY = view.getScrollY();
            this.scheduleNextCheckForChange();
        }
    }

    @VisibleForTesting
    void scheduleNextCheckForChange() {
        this.mHandler.postDelayed(this.mCheckForChangeRunnable, 100L);
    }

    @VisibleForTesting
    void checkForChange() {
        if(this.mIsRunning && this.mView != null) {
            int currentScrollY = this.mView.getScrollY();
            if(currentScrollY == this.mLastScrollY) {
                this.mIsRunning = false;
                this.mFlingListener.onFlingComplete(this.mView);
            } else {
                this.mLastScrollY = currentScrollY;
                this.scheduleNextCheckForChange();
            }

        }
    }

    public interface FlingListener {
        void onFlingComplete(View var1);
    }
}
