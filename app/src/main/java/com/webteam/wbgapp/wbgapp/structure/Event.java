package com.webteam.wbgapp.wbgapp.structure;

import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Deathlymad on 12.03.2016 .
 */
public class Event implements IRequest {
    private int _id;
    private int _addTime; //WHAT IS THIS
    private Date _startTime, _endTime;
    private String _title;

    private int _author;
    private String _teaser;
    private String _location;
    public Event(JSONObject data) throws JSONException {
        _id = data.getInt("id");
        _addTime = data.getInt("addTime");
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
            _teaser = extData.getString("teaser");
            _location = extData.getString("location");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
