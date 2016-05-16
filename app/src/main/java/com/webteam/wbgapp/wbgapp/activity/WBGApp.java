package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.fragment.NewsListAdapter;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.util.Constants;

import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//TODO mark non clickable News

public class WBGApp
        extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener,
            BackgroundService.UpdateListener
        {

    private class NewsScrollHandler implements AbsListView.OnScrollListener
    {

        private WBGApp ref = null;

        public NewsScrollHandler(WBGApp reference)
        {
            ref = reference;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount <= firstVisibleItem + visibleItemCount)
                ref.appendList();
        }
    }

    @Override
    public void onUpdate(String Type) {
        ListView list = (ListView) findViewById(android.R.id.list);
        if (list != null && list.getAdapter() == null)
            list.setAdapter(BackgroundService._newsList);
    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_GET_NEXT_NEWS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_wbgapp);
        super.onCreate(savedInstanceState);
        ((SwipeRefreshLayout)findViewById(R.id.swipe_container)).setOnRefreshListener(this);

        ListView list = (ListView)findViewById(android.R.id.list);
        list.setOnScrollListener(new NewsScrollHandler(this));
    }

    @Override
    protected String getName() {
        return "News";
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
        JSONArray arr = new JSONArray();
        NewsListAdapter _newsStack = (NewsListAdapter) ((ListView)findViewById(android.R.id.list)).getAdapter();
        if ( _newsStack != null && !_newsStack.isEmpty())
            for (int i = 0; i < _newsStack.getCount(); i++)
                arr.put(_newsStack.getItem(i).toString());
        file.write(arr.toString().getBytes());
    }

    @Override
    protected void load(FileInputStream file) throws IOException {
        regenerateList();
    }

    @Override
    public void onRefresh(){
        SwipeRefreshLayout layout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        if ( layout == null)
            throw new NullPointerException("No RefreshLayout");
        layout.setRefreshing(true);
        if (!layout.canChildScrollUp())
            regenerateList();
        layout.setRefreshing(false);
    }

    private void regenerateList()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_GET_NEXT_NEWS);
        i.putExtra("append", false);
        startService(i);
        ListView list = (ListView)findViewById(android.R.id.list);
        if (BackgroundService._newsList != null)
            list.setAdapter(BackgroundService._newsList);
    }
    private void appendList()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_GET_NEXT_NEWS);
        i.putExtra("append", true);
        startService(i);
        ListView list = (ListView)findViewById(android.R.id.list);
        if (BackgroundService._newsList != null)
            list.setAdapter(BackgroundService._newsList);
    }
}
