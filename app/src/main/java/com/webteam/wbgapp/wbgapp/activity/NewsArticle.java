package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Deathlymad on 15.03.2016.
 */
public class NewsArticle extends BaseActivity {

    private String _title;

    @Override
    protected String getName() {
        return _title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String extra = getIntent().getStringExtra(WBGApp.requestTitle);

        try {
            _title = (new JSONObject(extra)).getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbgapp);
    }
}
