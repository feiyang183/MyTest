package com.example.dell.myscada.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.dell.myscada.R;

/**
 * Created by Dell on 2017/5/27.
 */

public class PreferenceFagment extends PreferenceFragment {
    SharedPreferences.OnSharedPreferenceChangeListener mChangeListener;
    Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if("genercal".equals(key) || "baojing1".equals(key) || "baojing2".equals(key)){
                    Toast.makeText(mActivity, key + ": Change to" + sharedPreferences.getBoolean(key, true), Toast.LENGTH_SHORT).show();
                }else if ("name".equals(key)){
                    findPreference("name").setSummary(sharedPreferences.getString(key, "GMY - 02:00"));
                }
            }
        };
        addPreferencesFromResource(R.xml.activity_setting);
    }

    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mChangeListener);
    }
}
