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

import java.util.ArrayList;

public class WBGApp extends BaseActivity implements IRequest, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ArrayList<News> _newsStack;
    public static final String requestTitle = "com.webteam.wbgapp.wbgapp.NEWS";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbgapp);
        ((SwipeRefreshLayout)findViewById(R.id.swipe_container)).setOnRefreshListener(this);

        _newsStack = new ArrayList<>();
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
                layout.addView(entry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getName() {
        return "News";
    }

    @Override
    public void onRefresh(){
        ((SwipeRefreshLayout)findViewById(R.id.swipe_container)).setRefreshing(true);
        if (!((SwipeRefreshLayout)findViewById(R.id.swipe_container)).canChildScrollUp())
        {
            DatabaseHandler hand = new DatabaseHandler();
            hand.execute(this);
        }
        ((SwipeRefreshLayout)findViewById(R.id.swipe_container)).setRefreshing(false);
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
