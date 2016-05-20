package com.webteam.wbgapp.wbgapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.webteam.wbgapp.wbgapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by malte on 21.01.2016.
 */
public class SettingsDatausage extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    @Override
    protected String getName() {
        return getString(R.string.settings_datausage_title);
    }


    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_settings_datausage);

        SharedPreferences settings = getSettings();
        Switch temp = ((Switch) findViewById(R.id.data_images_on_off));
            temp.setChecked(settings.getBoolean("loadImages", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.data_videos_on_off));
            temp.setChecked(settings.getBoolean("loadVideos", false));
            temp.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor settings = getSettings().edit();
        switch (buttonView.getId()) {
            case R.id.data_images_on_off:
                settings.putBoolean("loadImages", isChecked);
                break;
            case R.id.data_videos_on_off:
                settings.putBoolean("loadVideos", isChecked);
                break;
        }
        settings.apply();
    }
}
