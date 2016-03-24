package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Deathlymad on 24.03.2016 .
 */
public class EventArticle extends BaseActivity {
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
        String extra = getIntent().getStringExtra(EventSchedule.requestTitle);
        JSONObject _extra = null;
        try {
            _extra = new JSONObject(extra);
            _title = _extra.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_show_event);

        ((TextView)findViewById(R.id.event_show_title)).setText(_title);

        try {
            ((TextView)findViewById(R.id.event_show_title)).setText(_extra.getString("teaser")); //TODO no value
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
