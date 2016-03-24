package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Deathlymad on 15.03.2016.
 */
public class NewsArticle extends BaseActivity {

    private String _title = "";

    @Override
    protected String getName() {
        return _title;
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {

    }

    @Override
    protected void load(FileInputStream file) throws IOException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String extra = getIntent().getStringExtra(WBGApp.requestTitle);
        JSONObject _extra = null;
        try {
            _extra = new JSONObject(extra);
            _title = Util.unescUnicode(_extra.getString("headline"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_show_article);

        ((TextView)findViewById(R.id.show_article_title)).setText(_title);

        try {
            ((TextView)findViewById(R.id.show_article_date_infos)).setText("Geschrieben am " + Util.getStringFromTStamp(Long.parseLong(_extra.getString("date"))));
            ((TextView)findViewById(R.id.show_article_text)).setText(_extra.getString("content")); //TODO no value //TODO Webservice vervollständigen (Malte)
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
