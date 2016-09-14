package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.structure.News;
import com.webteam.wbgapp.wbgapp.util.Constants;

public class NewsSchedule
        extends BaseActivity
        implements BackgroundService.UpdateListener
        {

    private class NewsScrollHandler implements AbsListView.OnScrollListener
    {

        private NewsSchedule ref = null;

        public NewsScrollHandler(NewsSchedule reference)
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

    private class NewsClickHandler implements ListView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            News n = (News)parent.getItemAtPosition(position);
            n.onClick(view);
        }
    }

    @Override
    public void onUpdate(String Type) {
        ListView list = (ListView) findViewById(android.R.id.list);
        if (list != null && BackgroundService._newsList != null && list.getAdapter() == null) {
            list.setAdapter(BackgroundService._newsList);
        }
    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_GET_NEXT_NEWS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        BackgroundService.registerUpdate(this);
        setContentView(R.layout.activity_wbgapp);
        super.onCreate(savedInstanceState);

        ListView list = (ListView)findViewById(android.R.id.list);
        if (list == null)
            throw new NullPointerException("couldn't fine News ListView");
        list.setOnScrollListener(new NewsScrollHandler(this));
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(new NewsClickHandler());
    }

    @Override
    protected String getName() {
        return "Neuigkeiten";
    }

    private void regenerateList()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_GET_NEXT_NEWS);
        i.putExtra("append", false);
        startService(i);
    }
    private void appendList()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_GET_NEXT_NEWS);
        i.putExtra("append", true);
        startService(i);
    }
}
