package com.webteam.wbgapp.wbgapp.structure;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.webteam.wbgapp.wbgapp.activity.NewsArticle;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.util.Constants;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Deathlymad on 16.01.2016.
 */
public class News {
    private int _id;
    private Date _date;
    private String _title;
    private String _teaser;
    private String _content;

    public News( JSONObject data) throws JSONException {
        _id = data.getInt("id");
        _date = Util.getDateFromTStamp(data.getLong("date"));
        _title = Util.unescUnicode(data.getString("headline"));
        _teaser = Util.unescUnicode(data.getString("teaser"));
    }

    @Override
    public String toString()
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", _id);
            obj.put("date", Util.getTStampFromDate(_date));
            obj.put("headline", Util.escUnicode(_title));
            obj.put("teaser", Util.escUnicode(_teaser));
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

    public void setContent(String data)
    {
        _content = data;
    }

    public void onClick(View v) {
        Intent i = new Intent( v.getContext(), NewsArticle.class);
        i.putExtra(Constants.NEWS_ARTICLE_DATA, toString());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(i);
    }

    public int getID() {
        return _id;
    }

    public String getContent() {
        return _content;
    }
}
