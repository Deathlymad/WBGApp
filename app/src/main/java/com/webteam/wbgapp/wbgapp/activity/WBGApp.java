package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.structure.News;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WBGApp extends BaseActivity implements IRequest, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ArrayList<News> _newsStack;
    public static final String requestTitle = "com.webteam.wbgapp.wbgapp.NEWS";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        try {
            setContentView(R.layout.activity_wbgapp);
            assert ((SwipeRefreshLayout) findViewById(R.id.swipe_container)) != null;
            ((SwipeRefreshLayout) findViewById(R.id.swipe_container)).setOnRefreshListener(this);

            _newsStack = new ArrayList<>();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_wbgapp);

            for (News n : _newsStack) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.news_container);
                TextView entry = n.addView(layout);
                entry.setOnClickListener(this);
                assert layout != null;
                layout.addView(entry);
            }
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getRequest() {
        return new String[]{"news"};
    }

    @Override
    public void handleResults(String... result) {
        String res = result[0];
        _newsStack.clear(); //rewrite NewsFeed
        try {
            JSONArray newsList = new JSONArray(res);
            for (int i = 0; i < 10; i++) {
                _newsStack.add(new News(newsList.getJSONObject(i)));
                LinearLayout layout = (LinearLayout) findViewById(R.id.news_container);
                TextView entry = _newsStack.get(i).addView(layout);
                entry.setOnClickListener(this);
                assert layout != null;
                layout.addView(entry);
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getName() {
        return "News";
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
        JSONArray arr = new JSONArray();
        if (!_newsStack.isEmpty())
            for (News n : _newsStack)
                arr.put(n.toString());
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
                    _newsStack.add(new News(arr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void onRefresh(){
        try {
            assert ((SwipeRefreshLayout) findViewById(R.id.swipe_container)) != null;
            ((SwipeRefreshLayout) findViewById(R.id.swipe_container)).setRefreshing(true);
            assert ((SwipeRefreshLayout) findViewById(R.id.swipe_container)) != null;
            if (!((SwipeRefreshLayout) findViewById(R.id.swipe_container)).canChildScrollUp()) {
                DatabaseHandler hand = new DatabaseHandler();
                hand.execute(this);
            }
            assert ((SwipeRefreshLayout) findViewById(R.id.swipe_container)) != null;
            ((SwipeRefreshLayout) findViewById(R.id.swipe_container)).setRefreshing(false);
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        TextView entry = (TextView) v;
        News article = null;
        for (News n : _newsStack) //probably needs faster approach
            {
                String a = n.getTitle();
                String b = entry.getText().toString();
                if ( a.equals(b))
                    article = n;
            }
        if (article != null)
        {
            Intent i = new Intent(this, NewsArticle.class);
            i.putExtra(requestTitle, article.toString());
            startActivity(i);
        }
    }
}
