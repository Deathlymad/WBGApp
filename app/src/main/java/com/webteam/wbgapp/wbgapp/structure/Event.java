package com.webteam.wbgapp.wbgapp.structure;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Deathlymad on 12.03.2016 .
 */
public class Event implements IRequest {
    private int _id;
    private Date _addTime; //WHAT IS THIS
    private Date _startTime, _endTime;
    private String _title;

    private int _author;
    private String _teaser;
    private String _location;

    View _event;


    public Event(JSONObject data) throws JSONException {
        _id = data.getInt("id");
        _addTime = Util.getDateFromTStamp(data.getInt("addTime"));
        _startTime = Util.getDateFromTStamp(data.getLong("startTime"));
        _endTime = Util.getDateFromTStamp(data.getLong("endTime"));
        _title = Util.unescUnicode(data.getString("title"));
        new DatabaseHandler().execute(this);
    }

    @Override
    public String[] getRequest() {
        return new String[]{"eventcontent&id=" + _id + "&images=false"};
    }

    @Override
    public void handleResults(String... result) {
        String res = result[0];
        try {
            JSONObject extData = new JSONObject(res);
            _author = extData.getInt("author");
            _teaser = Util.unescUnicode(extData.getString("teaser"));
            _location = Util.unescUnicode(extData.getString("location"));
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
            obj.put("addTime", Util.getTStampFromDate(_addTime));
            obj.put("startTime", Util.getTStampFromDate(_startTime));
            obj.put("endTime", Util.getTStampFromDate(_endTime));
            obj.put("title", Util.escUnicode(_title));
            obj.put("author", _author);
            obj.put("teaser", Util.escUnicode(_teaser));
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

    public <T extends View.OnClickListener> void addView(T view) {
        LinearLayout container = (LinearLayout)((Activity)view).findViewById(R.id.news_container);
        _event = ((Activity)view).getLayoutInflater().inflate(R.layout.activity_news_element, null);

        TextView _teaser = (TextView)_event.findViewById(R.id.article_element_title);
        _teaser.setText(_title);
        _teaser.setOnClickListener(view);

        TextView _teaserDate = (TextView)_event.findViewById(R.id.article_element_date);//TODO: needs to Change
        String date = new SimpleDateFormat("dd.MM.yyyy").format(_startTime.getTime());
        _teaserDate.setText(date);

        container.addView(_event);
    }
}
