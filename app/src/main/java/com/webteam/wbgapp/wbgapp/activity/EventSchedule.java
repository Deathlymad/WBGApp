package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.structure.Event;
import com.webteam.wbgapp.wbgapp.structure.News;
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

    private class EventClickHandler implements ListView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Event n = (Event)parent.getItemAtPosition(position);
            n.onClick(view);
        }
    }


    @Override
    public void onUpdate(String Type) {
        ListView list = (ListView) findViewById(android.R.id.list);
        if (list != null && BackgroundService._eventList != null && list.getAdapter() == null) {
            list.setAdapter(BackgroundService._eventList);
        }
    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_GET_NEXT_EVENT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        BackgroundService.registerUpdate(this);
        setContentView(R.layout.activity_wbgapp);
        super.onCreate(savedInstanceState);

        ListView list = (ListView) findViewById(android.R.id.list);
        if (list == null)
            throw new NullPointerException("Event List couldn't be Resolved.");
        list.setOnScrollListener(new EventScrollHandler(this));
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(new EventClickHandler());
    }

    @Override
    protected String getName() {
        return "Events";
    }

    private void regenerateList()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_GET_NEXT_EVENT);
        i.putExtra("append", false);
        startService(i);
    }
    private void appendList()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_GET_NEXT_EVENT);
        i.putExtra("append", true);
        startService(i);
    }
}