package com.webteam.wbgapp.wbgapp.structure;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.NewsArticle;
import com.webteam.wbgapp.wbgapp.activity.WBGApp;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Deathlymad on 16.01.2016.
 */
public class News implements IRequest, View.OnClickListener {
    private int _id;
    private Date _date;
    private String _title;
    private Activity _activity;
    private String _content;

    public static final String requestTitle = "com.webteam.wbgapp.wbgapp.NEWS";

    public News(Activity ac, JSONObject data) throws JSONException {
        _activity = ac;
        _id = data.getInt("id");
        _date = Util.getDateFromTStamp(data.getLong("date"));
        _title = Util.unescUnicode(data.getString("headline"));
        new DatabaseHandler().execute(this);
    }

    @Override
    public String[] getRequest() {
        return new String[]{"newscontent&id=" + _id + "&images=false"};
    }

    @Override
    public void handleResults(String... result) {
        String res = result[0];
        try {
            JSONArray newsList = new JSONArray(res);
            _content = newsList.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString()
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", _id);
            obj.put("date", Util.getTStampFromDate(_date));
            obj.put("headline", Util.escUnicode(_title));
            obj.put("content", Util.escUnicode(_content));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public String getTitle()
    {
        return _title;
    }

    public String getDateString()
    {
        return Util.getStringFromDate(_date);
    }

    public long getTime()
    {
        return Util.getTStampFromDate(_date);
    }

    @Override
    public void onClick(View v) {
        ((WBGApp)_activity).createArticle(this);
    }
}
