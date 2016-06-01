package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.structure.Event;
import com.webteam.wbgapp.wbgapp.util.Constants;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by Deathlymad on 24.03.2016 .
 */

public class EventArticle extends BaseActivity implements BackgroundService.UpdateListener{

    private int _id = 0;
    private String _title = "Lädt...";
    private String _location = "";
    protected String _startTime, _endTime;
    @Override
    protected String getName() {
        return _title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String extra = getIntent().getStringExtra(Constants.EVENT_ARTICLE_DATA);
        JSONObject _extra;
        BackgroundService.registerUpdate(this);
        try {
            _extra = new JSONObject(extra);
            _id = _extra.getInt("id");
            _title = Util.unescUnicode(_extra.getString("title"));
            try {
                _location = Util.unescUnicode(_extra.getString("location"));
                if (_location == "")
                    _location = "Weinberg Gymnasium Kleinmachnow";
                ((TextView)findViewById(R.id.show_event_title)).setText(_location);
                _startTime = Util.getDateString(Util.getDateFromString(_extra.getString("startTime")));
                ((TextView)findViewById(R.id.show_event_from)).setText(_startTime);
                _endTime = Util.getDateString(Util.getDateFromString(_extra.getString("endTime")));
                ((TextView)findViewById(R.id.show_event_to)).setText(_endTime);
            } catch (JSONException ignored) {
                Intent i = new Intent(this, BackgroundService.class); // move to NewsArticle
                i.setAction(Constants.INTENT_GET_EVENT_CONTENT);
                i.putExtra("id", _id);
                this.startService(i);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_show_event);

        ((TextView)findViewById(R.id.show_event_title)).setText(_title);
    }

    @Override
    public void onUpdate(String Type) {
        Event temp = BackgroundService._eventList.get(_id);
        if (temp != null)
        {
            _title = temp.getTitle();
            ((TextView)findViewById(R.id.show_event_title)).setText(_title);
            setTitle(_title);
            try {
                JSONObject _data = new JSONObject(temp.toString());
                _location = _data.getString("location");
                if (_location == "")
                    _location = "Weinberg Gymnasium Kleinmachnow";
                ((TextView)findViewById(R.id.show_event_location)).setText(_location);
                _startTime = Util.getDateString(Util.getDateFromTStamp(_data.getLong("startTime")));
                ((TextView)findViewById(R.id.show_event_from)).setText(_startTime);
                _endTime = Util.getDateString(Util.getDateFromTStamp(_data.getLong("endTime")));
                ((TextView)findViewById(R.id.show_event_to)).setText(_endTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_GET_EVENT_CONTENT;
    }
}
