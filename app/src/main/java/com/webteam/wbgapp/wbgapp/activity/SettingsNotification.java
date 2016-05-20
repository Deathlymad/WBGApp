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
        temp = ((Switch) findViewById(R.id.notification_general_on_off));
            temp.setChecked(settings.getBoolean("notificationGeneral", true));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.notification_news_on_off));
            temp.setChecked(settings.getBoolean("notificationNews", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.notification_place_lockscreen_on_off));
            temp.setChecked(settings.getBoolean("notificationLockscreen", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.notification_place_statusbar_on_off));
            temp.setChecked(settings.getBoolean("notificationStatusbar", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.notification_schedules_all_on_off));
            temp.setChecked(settings.getBoolean("notificationSchedules", false));
            temp.setOnCheckedChangeListener(this);      //TODO: Schedules need to be implemented
        temp = ((Switch) findViewById(R.id.notification_schedules_selected_on_off));
            temp.setChecked(settings.getBoolean("notificationSchedulesSelect", false));
            temp.setOnCheckedChangeListener(this);
        temp = ((Switch) findViewById(R.id.notification_substitute_plan_on_off));
            temp.setChecked(settings.getBoolean("notificationSubstitution", false));
            temp.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor settings = getSettings().edit();
        switch (buttonView.getId()) {
            case R.id.notification_general_on_off:
                settings.putBoolean("notificationGeneral", isChecked);
                break;
            case R.id.notification_news_on_off:
                settings.putBoolean("notificationNews", isChecked);
                break;
            case R.id.notification_place_lockscreen_on_off:
                settings.putBoolean("notificationLockscreen", isChecked);
                break;
            case R.id.notification_place_statusbar_on_off:
                settings.putBoolean("notificationStatusbar", isChecked);
                break;
            case R.id.notification_schedules_all_on_off:
                settings.putBoolean("notificationSchedules", isChecked);
                break;
            case R.id.notification_schedules_selected_on_off:
                settings.putBoolean("notificationSchedulesSelect", isChecked);
                break;
            case R.id.notification_substitute_plan_on_off:
                settings.putBoolean("notificationSubstitution", isChecked);
                break;
        }
        settings.apply();
    }
}
