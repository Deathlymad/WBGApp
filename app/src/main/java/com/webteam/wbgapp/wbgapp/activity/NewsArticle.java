package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.structure.Event;
import com.webteam.wbgapp.wbgapp.structure.News;
import com.webteam.wbgapp.wbgapp.util.Constants;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Deathlymad on 15.03.2016.
 */
public class NewsArticle extends BaseActivity implements BackgroundService.UpdateListener {

    private int _id = 0;
    private String _title = "";
    private String _date= "";
    private String _content = "";

    @Override
    protected String getName() {
        return _title;
    }

    @Override
    public void onUpdate(String Type) {
        News temp = BackgroundService._newsList.get(_id);
        if (temp != null)
        {
            _date = temp.getDateString();
            _content = temp.getContent();



            ((TextView)findViewById(R.id.show_article_date_infos)).setText("Geschrieben am " + _date);
            ((TextView)findViewById(R.id.show_article_text)).setText(Util.unescUnicode(_content));
        }
    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_GET_NEWS_CONTENT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //save data in News
        String extra = getIntent().getStringExtra(Constants.NEWS_ARTICLE_DATA);
        JSONObject _extra;
        try {
            _extra = new JSONObject(extra);
            _id = _extra.getInt("id");
            _title = Util.unescUnicode(_extra.getString("headline"));
            try {
                _date = Util.getStringFromTStamp(_extra.getLong("date"));
                _content = _extra.getString("content");
            } catch (JSONException ignored) {
                BackgroundService.registerUpdate(this);
                Intent i = new Intent(this, BackgroundService.class);
                i.setAction(Constants.INTENT_GET_NEWS_CONTENT);
                i.putExtra("id", _id);
                this.startService(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_show_article);

        ((TextView)findViewById(R.id.show_article_title)).setText(_title);

        ((TextView)findViewById(R.id.show_article_date_infos)).setText("Geschrieben am " + _date);
        ((TextView)findViewById(R.id.show_article_text)).setText(Util.unescUnicode(_content));
    }
}