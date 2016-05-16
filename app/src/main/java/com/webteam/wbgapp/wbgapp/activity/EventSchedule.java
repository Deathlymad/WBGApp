package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.structure.Event;
import com.webteam.wbgapp.wbgapp.util.Constants;

import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Deathlymad on 24.03.2016 .
 */
public class EventSchedule
        extends BaseActivity
        implements
            SwipeRefreshLayout.OnRefreshListener,
            View.OnClickListener,
            BackgroundService.UpdateListener {

    private class EventScrollHandler implements AbsListView.OnScrollListener {

        private EventSchedule ref = null;

        public EventScrollHandler(EventSchedule reference) {
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
            list.setAdapter(BackgroundService._eventList);
    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_GET_NEXT_EVENT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wbgapp);
        super.onCreate(savedInstanceState);
        View layout = findViewById(R.id.swipe_container);
        if (layout != null)
            ((SwipeRefreshLayout) layout).setOnRefreshListener(this);


        ListView list = (ListView) findViewById(android.R.id.list);
        if (list == null)
            throw new NullPointerException("Event List couldn't be Resolved.");
        BackgroundService.registerUpdate(this);
        list.setOnScrollListener(new EventScrollHandler(this));
    }

    @Override
    protected String getName() {
        return "Events";
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
        JSONArray arr = new JSONArray();
        if (!BackgroundService._eventList.isEmpty())
            for (int i = 0; i < BackgroundService._eventList.getCount(); i++)
                arr.put(BackgroundService._eventList.getItem(i).toString());
        file.write(arr.toString().getBytes());
    }

    @Override
    protected void load(FileInputStream file) throws IOException {
        regenerateList();
    }

    @Override
    public void onRefresh() {
        SwipeRefreshLayout layout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        if (layout == null)
            throw new NullPointerException("No RefreshLayout");
        layout.setRefreshing(true);
        if (!layout.canChildScrollUp()) {
            regenerateList();
        }
        layout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        TextView entry = (TextView) v;
        Event article = null;
        for (int i = 0; i < BackgroundService._eventList.getCount(); i++) //probably needs faster approach
        {
            String a = BackgroundService._eventList.getItem(i).getTitle();
            String b = entry.getText().toString();
            if (a.equals(b))
                article = BackgroundService._eventList.getItem(i);
        }
        if (article != null) {
            Intent i = new Intent(this, EventArticle.class);
            i.putExtra(Constants.EVENT_ARTICLE_DATA, article.toString());
            startActivity(i);
        }
    }


    private void regenerateList()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_GET_NEXT_EVENT);
        i.putExtra("append", false);
        startService(i);
        ListView list = (ListView)findViewById(android.R.id.list);
        if (BackgroundService._eventList != null)
            list.setAdapter(BackgroundService._eventList);
    }
    private void appendList()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_GET_NEXT_EVENT);
        i.putExtra("append", true);
        startService(i);
        ListView list = (ListView)findViewById(android.R.id.list);
        if (BackgroundService._eventList != null)
            list.setAdapter(BackgroundService._eventList);
    }
}