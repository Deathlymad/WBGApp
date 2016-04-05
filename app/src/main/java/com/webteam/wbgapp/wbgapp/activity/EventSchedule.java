package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.structure.Event;

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

    //TODO: Bugfix: Bei "Weitere Events laden" -> Accounteinstellungen || News ????

    private ArrayList<Event> _eventStack;
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
                JSONArray eventList = new JSONArray(res);
                for (int i = 0; i < 10; i++) {
                    addEventToStack(new Event(eventList.getJSONObject(i)));
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        _eventStack = new ArrayList<>();
        setContentView(R.layout.activity_wbgapp);
        Button reloader = ((Button)findViewById(R.id.news_button_reload));
        reloader.setText(R.string.reload_button_events);
        reloader.setOnClickListener(this);
        super.onCreate(savedInstanceState);
        View layout = findViewById(R.id.swipe_container);
        if (layout != null)
            ((SwipeRefreshLayout)layout).setOnRefreshListener(this);

    }

    @Override
    protected String getName() {
        return "Events";
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
        JSONArray arr = new JSONArray();
        if (!_eventStack.isEmpty())
            for (Event n : _eventStack)
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
                    addEventToStack(new Event(new JSONObject(arr.getString(i))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onRefresh(){
        try {
            ((SwipeRefreshLayout) findViewById(R.id.swipe_container)).setRefreshing(true);
            if (!((SwipeRefreshLayout) findViewById(R.id.swipe_container)).canChildScrollUp()) {
                new  EventRequest(this);
            }
            ((SwipeRefreshLayout) findViewById(R.id.swipe_container)).setRefreshing(false);
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.news_button_reload)
        {
            if (_eventStack.size() > 0)
                new EventRequest(this, _eventStack.get(0).getTime());
            else
                new EventRequest(this);
        } else
        {
            TextView entry = (TextView) v;
            Event article = null;
            for (Event n : _eventStack) //probably needs faster approach
            {
                String a = n.getTitle();
                String b = entry.getText().toString();
                if ( a.equals(b))
                    article = n;
            }
            if (article != null)
            {
                Intent i = new Intent(this, EventArticle.class);
                i.putExtra(requestTitle, article.toString());
                startActivity(i);
            }
        }
    }

    private void addEventToStack(Event e)
    {
        _eventStack.add( 0, e);
        e.addView(this);
    }
}
