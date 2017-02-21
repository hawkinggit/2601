//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.wearable.view.drawer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.mstarc.wearablelauncher.R.dimen;
import com.mstarc.wearablelauncher.R.fraction;
import com.mstarc.wearablelauncher.R.id;
import com.mstarc.wearablelauncher.R.layout;
import com.mstarc.wearablelauncher.R.styleable;
import android.support.wearable.view.ResourcesUtil;
import android.support.wearable.view.drawer.WearableActionDrawerMenu;
import android.support.wearable.view.drawer.WearableDrawerView;
import android.support.wearable.view.drawer.WearableActionDrawerMenu.WearableActionDrawerMenuItem;
import android.support.wearable.view.drawer.WearableActionDrawerMenu.WearableActionDrawerMenuListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class WearableActionDrawer extends WearableDrawerView {
    private static final String TAG = "WearableActionDrawer";
    private final RecyclerView mActionList;
    private final int mTopPadding;
    private final int mBottomPadding;
    private final int mLeftPadding;
    private final int mRightPadding;
    private final int mFirstItemTopPadding;
    private final int mLastItemBottomPadding;
    private final int mIconRightMargin;
    private final boolean mShowOverflowInPeek;
    @Nullable
    private final ImageView mPeekActionIcon;
    @Nullable
    private final ImageView mPeekExpandIcon;
    private WearableActionDrawer.OnMenuItemClickListener mOnMenuItemClickListener;
    private final Adapter<WearableActionDrawer.ActionItemViewHolder> mActionListAdapter;
    private Menu mMenu;

    public WearableActionDrawer(Context context) {
        this(context, (AttributeSet)null);
    }

    public WearableActionDrawer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableActionDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, (ImageView)null, (ImageView)null);
    }

    @VisibleForTesting
    WearableActionDrawer(Context context, AttributeSet attrs, int defStyleAttr, @Nullable ImageView peekActionIcon, @Nullable ImageView peekExpandIcon) {
        super(context, attrs, defStyleAttr);
        this.setShouldLockWhenNotOpenOrPeeking(true);
        boolean showOverflowInPeek = false;
        int screenHeightPx;
        if(attrs != null) {
            TypedArray screenWidthPx = context.obtainStyledAttributes(attrs, styleable.WearableActionDrawer, defStyleAttr, 0);

            try {
                if(screenWidthPx.hasValue(styleable.WearableActionDrawer_show_overflow_in_peek)) {
                    showOverflowInPeek = screenWidthPx.getBoolean(styleable.WearableActionDrawer_show_overflow_in_peek, false);
                }

                if(screenWidthPx.hasValue(styleable.WearableActionDrawer_action_menu)) {
                    screenHeightPx = screenWidthPx.getResourceId(styleable.WearableActionDrawer_action_menu, 0);
                    MenuInflater res = new MenuInflater(context);
                    res.inflate(screenHeightPx, this.getMenu());
                }
            } finally {
                screenWidthPx.recycle();
            }
        }

        this.mShowOverflowInPeek = showOverflowInPeek;
        if(peekActionIcon != null) {
            Log.w("WearableActionDrawer", "Using injected peek and action icons. Should only occur in tests.");
            this.mPeekActionIcon = peekActionIcon;
            this.mPeekExpandIcon = peekExpandIcon;
        } else if(!this.mShowOverflowInPeek) {
            LayoutInflater screenWidthPx1 = LayoutInflater.from(context);
            View screenHeightPx1 = screenWidthPx1.inflate(layout.action_drawer_peek_view, this.getPeekContainer(), false);
            this.setPeekContent(screenHeightPx1);
            this.mPeekActionIcon = (ImageView)screenHeightPx1.findViewById(id.wearable_support_action_drawer_peek_action_icon);
            this.mPeekExpandIcon = (ImageView)screenHeightPx1.findViewById(id.wearable_support_action_drawer_expand_icon);
        } else {
            this.mPeekActionIcon = null;
            this.mPeekExpandIcon = null;
        }

        int screenWidthPx2 = ResourcesUtil.getScreenWidthPx(context);
        screenHeightPx = ResourcesUtil.getScreenHeightPx(context);
        Resources res1 = this.getResources();
        this.mTopPadding = res1.getDimensionPixelOffset(dimen.action_drawer_item_top_padding);
        this.mBottomPadding = res1.getDimensionPixelOffset(dimen.action_drawer_item_bottom_padding);
        this.mLeftPadding = ResourcesUtil.getFractionOfScreenPx(context, screenWidthPx2, fraction.action_drawer_item_left_padding);
        this.mRightPadding = ResourcesUtil.getFractionOfScreenPx(context, screenWidthPx2, fraction.action_drawer_item_right_padding);
        this.mFirstItemTopPadding = ResourcesUtil.getFractionOfScreenPx(context, screenHeightPx, fraction.action_drawer_item_first_item_top_padding);
        this.mLastItemBottomPadding = ResourcesUtil.getFractionOfScreenPx(context, screenHeightPx, fraction.action_drawer_item_last_item_bottom_padding);
        this.mIconRightMargin = res1.getDimensionPixelOffset(dimen.action_drawer_item_icon_right_margin);
        this.mActionList = new RecyclerView(context);
        this.mActionList.setLayoutManager(new LinearLayoutManager(context));
        this.mActionListAdapter = new WearableActionDrawer.ActionListAdapter(this.getMenu());
        this.mActionList.setAdapter(this.mActionListAdapter);
        this.setDrawerContent(this.mActionList);
    }

    public boolean canScrollHorizontally(int direction) {
        return this.isOpened();
    }

    public void onPeekContainerClicked(View v) {
        if(this.mShowOverflowInPeek) {
            super.onPeekContainerClicked(v);
        } else {
            this.onMenuItemClicked(0);
        }

    }

    int preferGravity() {
        return 80;
    }

    public void setOnMenuItemClickListener(WearableActionDrawer.OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    private void onMenuItemClicked(int position) {
        if(position >= 0 && position < this.getMenu().size()) {
            WearableActionDrawerMenuItem menuItem = (WearableActionDrawerMenuItem)this.getMenu().getItem(position);
            if(menuItem.invoke()) {
                return;
            }

            if(this.mOnMenuItemClickListener != null) {
                this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
            }
        }

    }

    private void updatePeekIcons() {
        if(this.mPeekActionIcon != null && this.mPeekExpandIcon != null) {
            Menu menu = this.getMenu();
            int numberOfActions = menu.size();
            if(numberOfActions > 1) {
                this.setDrawerContent(this.mActionList);
                this.mPeekExpandIcon.setVisibility(VISIBLE);
            } else {
                this.setDrawerContent((View)null);
                this.mPeekExpandIcon.setVisibility(GONE);
            }

            if(numberOfActions >= 1) {
                Drawable firstActionDrawable = menu.getItem(0).getIcon();
                if(firstActionDrawable != null) {
                    firstActionDrawable = firstActionDrawable.getConstantState().newDrawable().mutate();
                    firstActionDrawable.clearColorFilter();
                }

                this.mPeekActionIcon.setImageDrawable(firstActionDrawable);
                this.mPeekActionIcon.setContentDescription(menu.getItem(0).getTitle());
            }

        }
    }

    public Menu getMenu() {
        if(this.mMenu == null) {
            this.mMenu = new WearableActionDrawerMenu(this.getContext(), new WearableActionDrawerMenuListener() {
                public void menuItemChanged(int position) {
                    if(WearableActionDrawer.this.mActionListAdapter != null) {
                        WearableActionDrawer.this.mActionListAdapter.notifyItemChanged(position);
                    }

                    if(position == 0) {
                        WearableActionDrawer.this.updatePeekIcons();
                    }

                }

                public void menuItemAdded(int position) {
                    if(WearableActionDrawer.this.mActionListAdapter != null) {
                        WearableActionDrawer.this.mActionListAdapter.notifyItemChanged(position);
                    }

                    if(position <= 1) {
                        WearableActionDrawer.this.updatePeekIcons();
                    }

                }

                public void menuItemRemoved(int position) {
                    if(WearableActionDrawer.this.mActionListAdapter != null) {
                        WearableActionDrawer.this.mActionListAdapter.notifyItemChanged(position);
                    }

                    if(position <= 1) {
                        WearableActionDrawer.this.updatePeekIcons();
                    }

                }

                public void menuChanged() {
                    if(WearableActionDrawer.this.mActionListAdapter != null) {
                        WearableActionDrawer.this.mActionListAdapter.notifyDataSetChanged();
                    }

                    WearableActionDrawer.this.updatePeekIcons();
                }
            });
        }

        return this.mMenu;
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem var1);
    }

    private final class ActionItemViewHolder extends ViewHolder {
        public final View mView;
        public final ImageView mIconView;
        public final TextView mTextView;

        public ActionItemViewHolder(View view) {
            super(view);
            this.mView = view;
            this.mIconView = (ImageView)view.findViewById(id.wearable_support_action_drawer_item_icon);
            ((LayoutParams)this.mIconView.getLayoutParams()).setMarginEnd(WearableActionDrawer.this.mIconRightMargin);
            this.mTextView = (TextView)view.findViewById(id.wearable_support_action_drawer_item_text);
        }
    }

    private final class ActionListAdapter extends Adapter<WearableActionDrawer.ActionItemViewHolder> {
        private final Menu mActionMenu = WearableActionDrawer.this.getMenu();
        private final OnClickListener mItemClickListener = new OnClickListener() {
            public void onClick(View v) {
                int childPos = WearableActionDrawer.this.mActionList.getChildAdapterPosition(v);
                if(childPos == -1) {
                    Log.w("WearableActionDrawer", "invalid child position");
                } else {
                    WearableActionDrawer.this.onMenuItemClicked(childPos);
                }
            }
        };

        public ActionListAdapter(Menu menu) {
        }

        public int getItemCount() {
            return this.mActionMenu.size();
        }

        public void onBindViewHolder(WearableActionDrawer.ActionItemViewHolder holder, int position) {
            holder.mView.setPadding(WearableActionDrawer.this.mLeftPadding, position == 0?WearableActionDrawer.this.mFirstItemTopPadding:WearableActionDrawer.this.mTopPadding, WearableActionDrawer.this.mRightPadding, position == this.getItemCount() - 1?WearableActionDrawer.this.mLastItemBottomPadding:WearableActionDrawer.this.mBottomPadding);
            Drawable icon = this.mActionMenu.getItem(position).getIcon();
            if(icon != null) {
                icon = icon.getConstantState().newDrawable().mutate();
            }

            CharSequence title = this.mActionMenu.getItem(position).getTitle();
            holder.mTextView.setText(title);
            holder.mTextView.setContentDescription(title);
            holder.mIconView.setContentDescription(title);
            holder.mIconView.setImageDrawable(icon);
        }

        public WearableActionDrawer.ActionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layout.action_drawer_item_view, parent, false);
            view.setOnClickListener(this.mItemClickListener);
            return WearableActionDrawer.this.new ActionItemViewHolder(view);
        }
    }
}
