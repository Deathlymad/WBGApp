package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;

import com.webteam.wbgapp.wbgapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Deathlymad on 15.03.2016.
 */
public class NewsArticle extends BaseActivity {

    private String _title = "";

    @Override
    protected String getName() {
        return _title;
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {

    }

    @Override
    protected void load(FileInputStream file) throws IOException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String extra = getIntent().getStringExtra(WBGApp.requestTitle);

        try {
            _title = (new JSONObject(extra)).getString("headline");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbgapp);
    }
}
