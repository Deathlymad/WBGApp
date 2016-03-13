package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;

import com.webteam.wbgapp.wbgapp.R;

public class SubstitutePlan extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_substitute_plan);
    }

    @Override
    protected String getName() {
        return getString(R.string.substitute_plan_title);
    }
}
