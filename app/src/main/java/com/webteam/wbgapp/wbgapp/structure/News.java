package com.webteam.wbgapp.wbgapp.structure;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.IRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Deathlymad on 16.01.2016.
 */
public class News implements IRequest{
    private int _id;
    private Date _date;
    private String _title;

    private String _content;

    public News(JSONObject data) throws JSONException {
        _id = data.getInt("id");
        _date = Util.getDateFromTStamp(data.getLong("date"));
        _title = Util.convUnicode(data.getString("headline"));
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
            obj.put("date", _date);
            obj.put("title", _title);
            obj.put("content", _content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public String getTitle()
    {
        return _title;
    }

    public void save(FileOutputStream file) throws IOException
    {
        file.write(this.toString().getBytes());
    }

    public TextView addView(LinearLayout view) {
        TextView Text = new TextView(view.getContext());
        Text.append(_title);
        return Text;
    }
}
