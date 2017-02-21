package com.mstarc.wearablelauncher.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.drawer.WearableDrawerLayout;
import android.support.wearable.view.drawer.WearableDrawerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mstarc.wearablelauncher.R;

/**
 * Created by wangxinzhi on 17-2-19.
 */

public class ClockDrawerFragment extends Fragment {
    private static final String TAG = ClockDrawerFragment.class.getSimpleName();
    private WearableDrawerLayout mWearableDrawerLayout;
    private RecyclerView mTopDrawerContent;
    private int mOpenedDrawerNumber = 0;

    public boolean canParentScrolling() {
        return mOpenedDrawerNumber <= 0;
    }

    public ClockDrawerFragment() {
    }

    class ClockDrawerStateCallback extends WearableDrawerLayout.DrawerStateCallback {

        @Override
        public void onDrawerOpened(View var1) {
            mOpenedDrawerNumber++;
            Log.d(TAG, "onDrawerOpened" + mOpenedDrawerNumber);

        }

        @Override
        public void onDrawerClosed(View var1) {
            mOpenedDrawerNumber--;
            Log.d(TAG, "onDrawerClosed" + mOpenedDrawerNumber);
        }

        @Override
        public void onDrawerStateChanged(@WearableDrawerView.DrawerState int var1) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.example_clock, container, false);
        mWearableDrawerLayout = (WearableDrawerLayout) root.findViewById(R.id.drawer_layout);
        mWearableDrawerLayout.peekDrawer(Gravity.TOP);
        mWearableDrawerLayout.peekDrawer(Gravity.BOTTOM);
        mWearableDrawerLayout.setDrawerStateCallback(new ClockDrawerStateCallback());
        mTopDrawerContent = (RecyclerView) root.findViewById(R.id.drawer_content);
        LinearLayoutManager lm = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTopDrawerContent.setLayoutManager(lm);
        mTopDrawerContent.setAdapter(new SimplerItemAdapter());
        return root;
    }

    protected final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        TextView mText;

        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.textView2);
        }
    }

    protected final static class SimplerItemAdapter extends RecyclerView.Adapter<SimpleItemViewHolder> {

        @Override
        public SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_top_drawer_item, parent, false);

            return new SimpleItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SimpleItemViewHolder holder, int position) {
            holder.mText.setText("Item" + position);


        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
