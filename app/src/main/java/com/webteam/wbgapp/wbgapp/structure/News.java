package com.webteam.wbgapp.wbgapp.structure;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.util.Util;

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

    private View _news;

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

    public long getTime()
    {
        return Util.getTStampFromDate(_date);
    }

    public View getView()
    {
        return _news;
    }

    public <T extends View.OnClickListener> void addView(T view) {
        _news = ((Activity)view).getLayoutInflater().inflate(R.layout.display_news_element, null);

        TextView _teaser = (TextView)_news.findViewById(R.id.article_element_title);
        _teaser.setText(_title);
        _teaser.setOnClickListener(view);

        TextView _teaserDate = (TextView)_news.findViewById(R.id.article_element_date);
        _teaserDate.setText(Util.getStringFromDate(_date));

        LinearLayout list = ((LinearLayout)((Activity)view).findViewById(R.id.news_container));
        list.addView(_news, list.getChildCount() - 1);
    }
}
