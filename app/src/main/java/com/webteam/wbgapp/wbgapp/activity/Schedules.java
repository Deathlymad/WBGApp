package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;

import com.webteam.wbgapp.wbgapp.R;

/**
 * Created by malte on 21.01.2016.
 */
public class Schedules extends BaseActivity {
    @Override
    protected String getName() {
        return getString(R.string.schedule_title);
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_timetable);
    }
}