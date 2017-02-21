//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.view.drawer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ActionProvider;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import java.util.ArrayList;
import java.util.List;

class WearableActionDrawerMenu implements Menu {
    private final Context mContext;
    private final List<WearableActionDrawerMenu.WearableActionDrawerMenuItem> mItems = new ArrayList();
    private final WearableActionDrawerMenu.WearableActionDrawerMenuListener mListener;
    private final WearableActionDrawerMenu.WearableActionDrawerMenuItem.MenuItemChangedListener mItemChangedListener = new WearableActionDrawerMenu.WearableActionDrawerMenuItem.MenuItemChangedListener() {
        public void itemChanged(WearableActionDrawerMenu.WearableActionDrawerMenuItem item) {
            for(int i = 0; i < WearableActionDrawerMenu.this.mItems.size(); ++i) {
                if(WearableActionDrawerMenu.this.mItems.get(i) == item) {
                    WearableActionDrawerMenu.this.mListener.menuItemChanged(i);
                }
            }

        }
    };

    public WearableActionDrawerMenu(Context context, WearableActionDrawerMenu.WearableActionDrawerMenuListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public MenuItem add(CharSequence title) {
        return this.add(0, 0, 0, title);
    }

    public MenuItem add(int titleRes) {
        return this.add(0, 0, 0, titleRes);
    }

    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return this.add(groupId, itemId, order, this.mContext.getResources().getString(titleRes));
    }

    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        WearableActionDrawerMenu.WearableActionDrawerMenuItem item = new WearableActionDrawerMenu.WearableActionDrawerMenuItem(this.mContext, itemId, title, this.mItemChangedListener);
        this.mItems.add(item);
        this.mListener.menuItemAdded(this.mItems.size() - 1);
        return item;
    }

    public void clear() {
        this.mItems.clear();
        this.mListener.menuChanged();
    }

    public void removeItem(int id) {
        int index = this.findItemIndex(id);
        if(index >= 0 && index < this.mItems.size()) {
            this.mItems.remove(index);
            this.mListener.menuItemRemoved(index);
        }
    }

    public MenuItem findItem(int id) {
        int index = this.findItemIndex(id);
        return index >= 0 && index < this.mItems.size()?(MenuItem)this.mItems.get(index):null;
    }

    public int size() {
        return this.mItems.size();
    }

    @Nullable
    public MenuItem getItem(int index) {
        return index >= 0 && index < this.mItems.size()?(MenuItem)this.mItems.get(index):null;
    }

    private int findItemIndex(int id) {
        List items = this.mItems;
        int itemCount = items.size();

        for(int i = 0; i < itemCount; ++i) {
            if(((WearableActionDrawerMenu.WearableActionDrawerMenuItem)items.get(i)).getItemId() == id) {
                return i;
            }
        }

        return -1;
    }

    public void close() {
        throw new UnsupportedOperationException("close is not implemented");
    }

    public SubMenu addSubMenu(CharSequence title) {
        throw new UnsupportedOperationException("addSubMenu is not implemented");
    }

    public SubMenu addSubMenu(int titleRes) {
        throw new UnsupportedOperationException("addSubMenu is not implemented");
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        throw new UnsupportedOperationException("addSubMenu is not implemented");
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        throw new UnsupportedOperationException("addSubMenu is not implemented");
    }

    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        throw new UnsupportedOperationException("addIntentOptions is not implemented");
    }

    public void removeGroup(int groupId) {
    }

    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        throw new UnsupportedOperationException("setGroupCheckable is not implemented");
    }

    public void setGroupVisible(int group, boolean visible) {
        throw new UnsupportedOperationException("setGroupVisible is not implemented");
    }

    public void setGroupEnabled(int group, boolean enabled) {
        throw new UnsupportedOperationException("setGroupEnabled is not implemented");
    }

    public boolean hasVisibleItems() {
        return false;
    }

    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        throw new UnsupportedOperationException("performShortcut is not implemented");
    }

    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean performIdentifierAction(int id, int flags) {
        throw new UnsupportedOperationException("performIdentifierAction is not implemented");
    }

    public void setQwertyMode(boolean isQwerty) {
    }

    public static final class WearableActionDrawerMenuItem implements MenuItem {
        private final int mId;
        private final Context mContext;
        private CharSequence mTitle;
        private Drawable mIconDrawable;
        private OnMenuItemClickListener mClickListener;
        private final WearableActionDrawerMenu.WearableActionDrawerMenuItem.MenuItemChangedListener mItemChangedListener;

        public WearableActionDrawerMenuItem(Context context, int id, CharSequence title, WearableActionDrawerMenu.WearableActionDrawerMenuItem.MenuItemChangedListener listener) {
            this.mContext = context;
            this.mId = id;
            this.mTitle = title;
            this.mItemChangedListener = listener;
        }

        public int getItemId() {
            return this.mId;
        }

        public MenuItem setTitle(CharSequence title) {
            this.mTitle = title;
            if(this.mItemChangedListener != null) {
                this.mItemChangedListener.itemChanged(this);
            }

            return this;
        }

        public MenuItem setTitle(int title) {
            return this.setTitle(this.mContext.getResources().getString(title));
        }

        public CharSequence getTitle() {
            return this.mTitle;
        }

        public MenuItem setIcon(Drawable icon) {
            this.mIconDrawable = icon;
            if(this.mItemChangedListener != null) {
                this.mItemChangedListener.itemChanged(this);
            }

            return this;
        }

        public MenuItem setIcon(int iconRes) {
            return this.setIcon(this.mContext.getResources().getDrawable(iconRes));
        }

        public Drawable getIcon() {
            return this.mIconDrawable;
        }

        public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
            this.mClickListener = menuItemClickListener;
            return this;
        }

        public int getGroupId() {
            return 0;
        }

        public int getOrder() {
            return 0;
        }

        public MenuItem setTitleCondensed(CharSequence title) {
            return this;
        }

        public CharSequence getTitleCondensed() {
            return null;
        }

        public MenuItem setIntent(Intent intent) {
            throw new UnsupportedOperationException("setIntent is not implemented");
        }

        public Intent getIntent() {
            return null;
        }

        public MenuItem setShortcut(char numericChar, char alphaChar) {
            throw new UnsupportedOperationException("setShortcut is not implemented");
        }

        public MenuItem setNumericShortcut(char numericChar) {
            return this;
        }

        public char getNumericShortcut() {
            return '\u0000';
        }

        public MenuItem setAlphabeticShortcut(char alphaChar) {
            return this;
        }

        public char getAlphabeticShortcut() {
            return '\u0000';
        }

        public MenuItem setCheckable(boolean checkable) {
            return this;
        }

        public boolean isCheckable() {
            return false;
        }

        public MenuItem setChecked(boolean checked) {
            return this;
        }

        public boolean isChecked() {
            return false;
        }

        public MenuItem setVisible(boolean visible) {
            return this;
        }

        public boolean isVisible() {
            return false;
        }

        public MenuItem setEnabled(boolean enabled) {
            return this;
        }

        public boolean isEnabled() {
            return false;
        }

        public boolean hasSubMenu() {
            return false;
        }

        public SubMenu getSubMenu() {
            return null;
        }

        public ContextMenuInfo getMenuInfo() {
            return null;
        }

        public void setShowAsAction(int actionEnum) {
            throw new UnsupportedOperationException("setShowAsAction is not implemented");
        }

        public MenuItem setShowAsActionFlags(int actionEnum) {
            throw new UnsupportedOperationException("setShowAsActionFlags is not implemented");
        }

        public MenuItem setActionView(View view) {
            throw new UnsupportedOperationException("setActionView is not implemented");
        }

        public MenuItem setActionView(int resId) {
            throw new UnsupportedOperationException("setActionView is not implemented");
        }

        public View getActionView() {
            return null;
        }

        public MenuItem setActionProvider(ActionProvider actionProvider) {
            throw new UnsupportedOperationException("setActionProvider is not implemented");
        }

        public ActionProvider getActionProvider() {
            return null;
        }

        public boolean expandActionView() {
            throw new UnsupportedOperationException("expandActionView is not implemented");
        }

        public boolean collapseActionView() {
            throw new UnsupportedOperationException("collapseActionView is not implemented");
        }

        public boolean isActionViewExpanded() {
            throw new UnsupportedOperationException("isActionViewExpanded is not implemented");
        }

        public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
            throw new UnsupportedOperationException("setOnActionExpandListener is not implemented");
        }

        boolean invoke() {
            return this.mClickListener != null && this.mClickListener.onMenuItemClick(this);
        }

        private interface MenuItemChangedListener {
            void itemChanged(WearableActionDrawerMenu.WearableActionDrawerMenuItem var1);
        }
    }

    interface WearableActionDrawerMenuListener {
        void menuItemChanged(int var1);

        void menuItemAdded(int var1);

        void menuItemRemoved(int var1);

        void menuChanged();
    }
}
