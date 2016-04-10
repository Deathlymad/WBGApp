package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.fragment.NewsListAdapter;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.structure.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WBGApp extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private NewsListAdapter _newsStack;

    private class NewsRequest implements IRequest
    {
        long _tstamp;
        WBGApp ref;

        public NewsRequest(WBGApp r)
        {
            ref = r;
            _tstamp = Calendar.getInstance().getTimeInMillis();
            DatabaseHandler hand = new DatabaseHandler();
            hand.execute(this);
        }

        public NewsRequest(WBGApp r, long tStamp)
        {
            ref = r;
            _tstamp = tStamp;
            DatabaseHandler hand = new DatabaseHandler();
            hand.execute(this);
        }

        @Override
        public String[] getRequest() {
            return new String[]{"news&tstamp=" + Long.toString(_tstamp)};
        }

        @Override
        public void handleResults(String... result) {
            String res = result[0];
            try {
                JSONArray newsList = new JSONArray(res);
                for (int i = 0; i < 10; i++) {
                    JSONObject obj = newsList.getJSONObject(i);
                    News n = new News( ref, obj);
                    ref.addNews(n);
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        _newsStack = new NewsListAdapter( this, R.layout.display_news_element, R.id.article_element_title, new ArrayList<News>());
        setContentView(R.layout.activity_wbgapp);
        super.onCreate(savedInstanceState);
        ((SwipeRefreshLayout)findViewById(R.id.swipe_container)).setOnRefreshListener(this);
        ((ListView)findViewById(android.R.id.list)).setAdapter(_newsStack);
    }

    @Override
    protected String getName() {
        return "News";
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
        JSONArray arr = new JSONArray();
        if (!_newsStack.isEmpty())
            for (int i = 0; i < _newsStack.getCount(); i++)
                arr.put(_newsStack.getItem(i).toString());
        file.write(arr.toString().getBytes());
    }

    @Override
    protected void load(FileInputStream file) throws IOException {
        byte temp[] = new byte[65535];
        int bytes = file.read(temp);
        if (bytes <= 2)
            return;
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < 65535; i++)
            if (temp[i]!= 0)
                str.append((char)temp[i]);
        String s = str.toString();
        if (!s.isEmpty())
            try {
                JSONArray arr = new JSONArray(s);
                for (int i = 0; i < 10; i++)
                    addNews(new News( this, new JSONObject( arr.getString(i))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void onRefresh(){
        try {
            ((SwipeRefreshLayout) findViewById(R.id.swipe_container)).setRefreshing(true);
            if (!((SwipeRefreshLayout) findViewById(R.id.swipe_container)).canChildScrollUp()) {
                _newsStack.clear();
                _newsStack.notifyDataSetChanged();
                new NewsRequest(this);
            }
            ((SwipeRefreshLayout) findViewById(R.id.swipe_container)).setRefreshing(false);
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private void addNews(News n)
    {
        _newsStack.add(n);
        _newsStack.notifyDataSetChanged();
    }

    public void createArticle(News n)
    {
        Intent i = new Intent( this, NewsArticle.class);
        i.putExtra( News.requestTitle, toString());
        startActivity(i);
    }
}
