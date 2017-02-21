package com.mstarc.wearablelauncher.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.drawer.WearableDrawerLayout;
import android.support.wearable.view.drawer.WearableDrawerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mstarc.wearablelauncher.R;

import java.util.ArrayList;

/**
 * Created by wangxinzhi on 17-2-19.
 */

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private WearableDrawerLayout mWearableDrawerLayout;
    private WearableDrawerView mWearableDrawerView;
    RecyclerView mTopDrawerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.example_main_layout);
        // Main Wearable Drawer Layout that wraps all content
        mWearableDrawerLayout = (WearableDrawerLayout) findViewById(R.id.drawer_layout);
        mWearableDrawerLayout.peekDrawer(Gravity.TOP);
        mWearableDrawerLayout.peekDrawer(Gravity.BOTTOM);
        mTopDrawerContent = (RecyclerView) findViewById(R.id.drawer_content);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTopDrawerContent.setLayoutManager(lm);
        mTopDrawerContent.setAdapter(new SimplerItemAdapter());
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
            holder.mText.setText("Item"+position);


        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
