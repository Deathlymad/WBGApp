package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.webteam.wbgapp.wbgapp.R;

/**
 * Created by malte on 21.01.2016.
 */
public class SettingsDatausage extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @Override
    protected String getName() {
        return getString(R.string.settings_datausage_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_settings_datausage);
        ((Switch) findViewById(R.id.data_images_on_off)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.data_videos_on_off)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.data_images_on_off:
                //settings.setPreloadImages(isChecked);
            case R.id.data_videos_on_off:
                //SettingManager.instance.setPreloadVideos(isChecked);
        }
    }
}
