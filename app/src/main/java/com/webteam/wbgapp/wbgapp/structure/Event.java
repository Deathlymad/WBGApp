package com.webteam.wbgapp.wbgapp.structure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.NewsArticle;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.util.Constants;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Deathlymad on 12.03.2016 .
 */
public class Event implements IRequest, View.OnClickListener {
    private int _id;
    private Date _startTime, _endTime;
    private String _title;

    private int _author;
    private String _teaser;
    private String _location;

    private Context _context;

    public Event(Context applicationContext, JSONObject data) throws JSONException {
        _id = data.getInt("id");
        _title = Util.unescUnicode(data.getString("title"));

        _context = applicationContext;

        try {
            _startTime = Util.getDateFromTStamp(data.getLong("startTime"));
            _endTime = Util.getDateFromTStamp(data.getLong("endTime"));
            _author = data.getInt("author");
            _teaser = Util.unescUnicode(data.getString("teaser"));
            _location = Util.unescUnicode(data.getString("location"));
        } catch(JSONException e) //for some reason JSON couldn't be read data needs to be pulled
        {
            Intent i = new Intent( _context, BackgroundService.class); // move to NewsArticle
            i.setAction(Constants.INTENT_GET_EVENT_CONTENT);
            i.putExtra("id", _id);
            _context.startService(i);
        }
    }

    @Override
    public String[] getRequest() {
        return new String[]{"eventcontent&id=" + _id + "&images=false"};
    }

    @Override
    public void handleResults(String... result) {
        String res = result[0];
    }

    @Override
    public String toString()
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", _id);
            if (_startTime != null)
                obj.put("startTime", Util.getTStampFromDate(_startTime));
            if (_endTime != null)
                obj.put("endTime", Util.getTStampFromDate(_endTime));
            if (_title != null)
                obj.put("title", Util.escUnicode(_title));
            obj.put("author", _author);
            if (_teaser != null)
                obj.put("teaser", Util.escUnicode(_teaser));
            if (_location != null)
                obj.put("location", Util.escUnicode(_location));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public String getTitle()
    {
        return _title;
    }

    public long getTime() {
        return Util.getTStampFromDate(_startTime);
    }

    public String getDateString() {
        return Util.getStringFromDate(_startTime);
    }

    public int getID() {
        return _id;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent( _context, NewsArticle.class);
        i.putExtra(Constants.NEWS_ARTICLE_DATA, toString());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public void setExtData(String s) {
        try {
            JSONObject extData = new JSONObject(s);
            _author = extData.getInt("author");
            _teaser = Util.unescUnicode(extData.getString("teaser"));
            _location = Util.unescUnicode(extData.getString("location"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
