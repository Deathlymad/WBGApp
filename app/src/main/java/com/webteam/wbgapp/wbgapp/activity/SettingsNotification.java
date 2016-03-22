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
    protected void save(FileOutputStream file) throws IOException {

    }

    @Override
    protected void load(FileInputStream file) throws IOException {

    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_settings_notification);

        ((Switch) findViewById(R.id.notification_general_on_off)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.notification_news_on_off)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.notification_place_lockscreen_on_off)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.notification_place_statusbar_on_off)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.notification_schedules_all_on_off)).setOnCheckedChangeListener(this);      //TODO: Schedules need to be implemented
        ((Switch) findViewById(R.id.notification_schedules_selected_on_off)).setOnCheckedChangeListener(this); //TODO: needs to be fully implemented
        ((Switch) findViewById(R.id.notification_substitute_plan_on_off)).setOnCheckedChangeListener(this);
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
    }
}
