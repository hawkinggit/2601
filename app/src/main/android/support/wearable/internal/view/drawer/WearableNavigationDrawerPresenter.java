//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.internal.view.drawer;

import android.support.annotation.MainThread;
import android.support.wearable.view.drawer.WearableNavigationDrawer.WearableNavigationDrawerAdapter;

public interface WearableNavigationDrawerPresenter {
    @MainThread
    void onDataSetChanged();

    @MainThread
    void onNewAdapter(WearableNavigationDrawerAdapter var1);

    @MainThread
    void onSelected(int var1);

    @MainThread
    void onSetCurrentItemRequested(int var1, boolean var2);

    @MainThread
    boolean onDrawerTapped();
}
