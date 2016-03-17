package com.webteam.wbgapp.wbgapp.structure;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.IRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Deathlymad on 16.01.2016.
 */
public class News implements IRequest{
    private int _id;
    private Date _date;
    private String _title;

    private String _content;

    private TextView _teaser;

    public News(JSONObject data) throws JSONException {
        _id = data.getInt("id");
        _date = Util.getDateFromTStamp(data.getLong("date"));
        _title = Util.unescUnicode(data.getString("headline"));
    }

    @Override
    public String[] getRequest() {
        return new String[]{"articlecontent&id=" + _id + "&images=false"};
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
            obj.put("date", _date.getTime());
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

    public <T extends View.OnClickListener> void addView(T view) {
        LinearLayout container = (LinearLayout) ((Activity)view).findViewById(R.id.news_container);
        TextView _teaser = new TextView(container.getContext());
        _teaser.append(_title);
        _teaser.setOnClickListener(view);
        container.addView(_teaser);
    }
}
