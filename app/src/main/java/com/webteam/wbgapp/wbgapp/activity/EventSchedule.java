package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.fragment.EventListAdapter;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.structure.Event;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Deathlymad on 24.03.2016 .
 */
public class EventSchedule extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private EventListAdapter _eventStack;
    public static final String requestTitle = "com.webteam.wbgapp.wbgapp.EVENTS";

    private class EventRequest implements IRequest
    {
        long _tstamp;
        EventSchedule ref;

        public EventRequest(EventSchedule r)
        {
            ref = r;
            _tstamp = Calendar.getInstance().getTimeInMillis();
            DatabaseHandler hand = new DatabaseHandler();
            hand.execute(this);
        }

        public EventRequest(EventSchedule r, long tStamp)
        {
            ref = r;
            _tstamp = tStamp;
            DatabaseHandler hand = new DatabaseHandler();
            hand.execute(this);
        }

        @Override
        public String[] getRequest() {
            return new String[]{"events&tstamp=" + Long.toString(_tstamp)};
        }

        @Override
        public void handleResults(String... result) {
            String res = result[0];
            _eventStack.clear(); //rewrite eventStack
            try {
                JSONArray eventList = new JSONArray(new JSONObject(Util.unescUnicode(res)));
                for (int i = 0; i < 10; i++) {
                    addEventToStack(new Event(eventList.getJSONObject(i)));
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                Log.i("Event JSON Parser", Util.unescUnicode(res));
            }
        }

    }

    private class EventScrollHandler implements AbsListView.OnScrollListener
    {

        private EventSchedule ref = null;

        public EventScrollHandler(EventSchedule reference)
        {
            ref = reference;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount <= firstVisibleItem + visibleItemCount)
                ref.pullNextHeap();
        }
    }

    private void pullNextHeap() {
        if (_eventStack.getCount() == 0)
            new EventRequest(this);
        else
            new EventRequest(this, _eventStack.getItem(_eventStack.getCount() - 1).getTime());
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        _eventStack = new EventListAdapter(this, R.layout.display_news_element, R.id.article_element_title, new ArrayList<Event>());
        setContentView(R.layout.activity_wbgapp);
        super.onCreate(savedInstanceState);
        View layout = findViewById(R.id.swipe_container);
        if (layout != null)
            ((SwipeRefreshLayout)layout).setOnRefreshListener(this);


        ListView list = (ListView)findViewById(android.R.id.list);
        if (list == null)
            throw new NullPointerException("Event List couldn't be Resolved.");
        list.setAdapter(_eventStack);
        list.setOnScrollListener(new EventScrollHandler(this));
    }

    @Override
    protected String getName() {
        return "Events";
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
        JSONArray arr = new JSONArray();
        if (!_eventStack.isEmpty())
            for (int i = 0; i < _eventStack.getCount(); i++)
                arr.put(_eventStack.getItem(i).toString());
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
                    addEventToStack(new Event(new JSONObject(arr.getString(i))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onRefresh(){
        SwipeRefreshLayout layout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        if ( layout == null)
            throw new NullPointerException("No RefreshLayout");
        layout.setRefreshing(true);
        if (!layout.canChildScrollUp()) {
            _eventStack.clear();
            _eventStack.notifyDataSetChanged();
            new EventRequest(this);
        }
        layout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        TextView entry = (TextView) v;
        Event article = null;
        for (int i = 0; i < _eventStack.getCount(); i++) //probably needs faster approach
        {
            String a = _eventStack.getItem(i).getTitle();
            String b = entry.getText().toString();
            if ( a.equals(b))
                article = _eventStack.getItem(i);
        }
        if (article != null)
        {
            Intent i = new Intent(this, EventArticle.class);
            i.putExtra(requestTitle, article.toString());
            startActivity(i);
        }
    }

    private void addEventToStack(Event e)
    {
        _eventStack.add( e);
    }
}
