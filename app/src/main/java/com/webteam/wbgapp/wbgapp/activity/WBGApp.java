package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.structure.News;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class WBGApp extends BaseActivity implements IRequest, SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<News> _newsStack;
    private static final String requestTitle = "NewsFeed";

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
            for (int i = 0; i < 10; i++)
                _newsStack.add(new News( newsList.getJSONObject(i)));
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
}
