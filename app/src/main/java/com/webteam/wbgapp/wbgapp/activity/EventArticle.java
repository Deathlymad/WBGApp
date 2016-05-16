package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.fragment.EventListAdapter;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.structure.Event;
import com.webteam.wbgapp.wbgapp.util.Constants;
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

// TODO: Beim runterziehen Liste aktualisieren

public class EventArticle extends BaseActivity implements BackgroundService.UpdateListener {

    private int _id = 0;
    private String _title = "Läd...";
    private String _teaser = "";
    @Override
    protected String getName() {
        return _title;
    }

    @Override
    protected boolean needsFile()
    { return false;}

    @Override
    protected void save(FileOutputStream file) throws IOException {

    }

    @Override
    protected void load(FileInputStream file) throws IOException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String extra = getIntent().getStringExtra(Constants.EVENT_ARTICLE_DATA);
        JSONObject _extra;
        BackgroundService.registerUpdate(this);
        try {
            _extra = new JSONObject(extra);
            _id = _extra.getInt("id");
            _title = _extra.getString("title");
            try {
                _teaser = _extra.getString("teaser");
                if (_teaser == "")
                    throw new JSONException("");
            } catch (JSONException ignored) {
                Intent i = new Intent(this, BackgroundService.class); // move to NewsArticle
                i.setAction(Constants.INTENT_GET_EVENT_CONTENT);
                i.putExtra("id", _id);
                this.startService(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_show_event);

        ((TextView)findViewById(R.id.event_show_title)).setText(_title);
        ((TextView)findViewById(R.id.event_show_title)).setText(_teaser);
    }

    @Override
    public void onUpdate(String Type) {
        Event temp = BackgroundService._eventList.get(_id);
        if (temp != null)
        {
            _title = temp.getTitle();
            _teaser = temp.getTitle(); //TODO write Text

            ((TextView)findViewById(R.id.event_show_title)).setText(_title);
            ((TextView)findViewById(R.id.event_show_title)).setText(_teaser);
            setTitle(_title);
        }
    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_GET_EVENT_CONTENT;
    }
}
