package com.mstarc.wearablelauncher;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * Created by wangxinzhi on 17-2-16.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
