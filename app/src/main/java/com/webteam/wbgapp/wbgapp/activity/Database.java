package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.view.View;

import com.webteam.wbgapp.wbgapp.R;

/**
 * Created by malte on 31.01.2016.
 */
public class Database extends BaseActivity {

    @Override
    protected String getName() {
        return "Database Test";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

    }

    public void submit(View view) {
    }
}
