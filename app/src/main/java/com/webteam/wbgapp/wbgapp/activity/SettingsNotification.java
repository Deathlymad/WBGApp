package com.webteam.wbgapp.wbgapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.webteam.wbgapp.wbgapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by malte on 21.01.2016.
 */
public class SettingsNotification extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @Override
    protected String getName() {
        return getString(R.string.settings_notifications_title);
    }


    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_settings_notification);

        Switch temp;
        SharedPreferences settings = getSettings();
        temp = ((Switch) findViewById(R.id.n_general));
            temp.setChecked(settings.getBoolean("notificationGeneral", true));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.n_news));
            temp.setChecked(settings.getBoolean("notificationNews", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.n_events));
            temp.setChecked(settings.getBoolean("notificationEvents", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.n_messages));
            temp.setChecked(settings.getBoolean("notificationMessages", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.n_mail));
            temp.setChecked(settings.getBoolean("notificationMail", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.n_supstitue));
            temp.setChecked(settings.getBoolean("notificationSubstitution", false));
            temp.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor settings = getSettings().edit();
        switch (buttonView.getId()) {
            case R.id.n_general:
                settings.putBoolean("notificationGeneral", isChecked);
                break;
            case R.id.n_news:
                settings.putBoolean("notificationNews", isChecked);
                break;
            case R.id.n_events:
                settings.putBoolean("notificationEvents", isChecked);
                break;
            case R.id.n_messages:
                settings.putBoolean("notificationMessages", isChecked);
                break;
            case R.id.n_mail:
                settings.putBoolean("notificationMail", isChecked);
                break;
            case R.id.n_supstitue:
                settings.putBoolean("notificationSubstitution", isChecked);
                break;
        }
        settings.apply();
    }
}
