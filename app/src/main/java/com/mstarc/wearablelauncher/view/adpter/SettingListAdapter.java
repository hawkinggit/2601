package com.mstarc.wearablelauncher.view.adpter;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mstarc.wearablelauncher.R;
import com.mstarc.wearablelauncher.view.settings.SettingFragment;

/**
 * Created by wangxinzhi on 17-2-12.
 */

public class SettingListAdapter extends WearableListView.Adapter {

    private static final String TAG = SettingFragment.class.getSimpleName();

    public enum SettingItemEnum {
        PHONE, SPORT, WEBXIN, TIMER, VOICESEARCH, ZHIFUBAO, HEARTBEATS, HEALTH, MAP, PAY, CAR, WEATHER, MESSAGE, SETTINGS, CALENDAR
    }

    private final LayoutInflater mInflater;
    private Context mContext;
    private String[] mSettingItemNameArray;

    public SettingListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mSettingItemNameArray = mContext.getResources().getStringArray(R.array.settinglist);
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.settingitem, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;
        TextView view = (TextView) holder.itemView.findViewById(R.id.textView);
        view.setText(mContext.getResources().getStringArray(R.array.settinglist)[i]);
        holder.setmItemId(SettingItemEnum.values()[i]);
    }

    @Override
    public int getItemCount() {
        return mSettingItemNameArray.length;
    }

    public static class ViewHolder extends WearableListView.ViewHolder{
        public SettingItemEnum getmItemId() {
            return mItemId;
        }

        public void setmItemId(SettingItemEnum mItemId) {
            this.mItemId = mItemId;
        }

        private SettingItemEnum mItemId;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}