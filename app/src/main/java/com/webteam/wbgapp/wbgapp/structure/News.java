package com.webteam.wbgapp.wbgapp.structure;

import android.view.View;

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
    private View _teaser;

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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
