package com.webteam.wbgapp.wbgapp.structure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.NewsArticle;
import com.webteam.wbgapp.wbgapp.activity.WBGApp;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.util.Constants;
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
    private Context _context;
    private String _content;

    public News(Context context, JSONObject data) throws JSONException {
        _context = context;
        _id = data.getInt("id");
        _date = Util.getDateFromTStamp(data.getLong("date"));
        _title = Util.unescUnicode(data.getString("headline"));
        try {
            _content = Util.unescUnicode(data.getString("content"));
        } catch(JSONException e) //for some reason JSON couldn't be read data needs to be pulled
        {

            Intent i = new Intent( _context, BackgroundService.class); // move to NewsArticle
            i.setAction(Constants.INTENT_GET_NEWS_CONTENT);
            i.putExtra("id", _id);
            context.startService(i);
        }
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

    public void setContent(String data)
    {
        _content = data;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent( _context, NewsArticle.class);
        i.putExtra(Constants.NEWS_ARTICLE_DATA, toString());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public int getID() {
        return _id;
    }
}
