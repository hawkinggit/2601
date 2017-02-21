//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.view;

import android.content.Context;
import android.support.annotation.FractionRes;

public final class ResourcesUtil {
    public static int getScreenWidthPx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightPx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getFractionOfScreenPx(Context context, int screenPx, @FractionRes int resId) {
        float marginPercent = context.getResources().getFraction(resId, 1, 1);
        return (int)(marginPercent * (float)screenPx);
    }

    private ResourcesUtil() {
    }
}
