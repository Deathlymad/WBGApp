package com.webteam.wbgapp.wbgapp.net;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.fragment.EventListAdapter;
import com.webteam.wbgapp.wbgapp.activity.fragment.NewsListAdapter;
import com.webteam.wbgapp.wbgapp.structure.Event;
import com.webteam.wbgapp.wbgapp.structure.News;
import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;
import com.webteam.wbgapp.wbgapp.util.Constants;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

/**
 * Created by Deathlymad on 07.05.2016.
 */
public class BackgroundService extends IntentService //manages Data
{
    public static SubstitutePlan getSubPlan() {
        return _today;
    }

    public interface UpdateListener
    {
        void onUpdate(String Type);
        String getUpdateType();
    }

    static private ArrayList<UpdateListener> Listeners = new ArrayList<>();
    public static void registerUpdate( UpdateListener listener)
    {
        Listeners.add(listener);
    }

    static SubstitutePlan _today, _tomorrow;
    public static EventListAdapter _eventList;
    public static NewsListAdapter _newsList;

    private static final Handler updateHandler = new Handler();

    public BackgroundService()
    {
        super("BackgroundUpdarter - DefaultConstruction");
    }

    public BackgroundService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (_newsList == null)
            _newsList = new NewsListAdapter( this, R.layout.display_news_element, R.id.article_element_title, new ArrayList<News>());
        if (_eventList == null)
            _eventList = new EventListAdapter(this, R.layout.display_news_element, R.id.article_element_title, new ArrayList<Event>());
        try {
            switch(intent.getAction())
            {
                case Constants.INTENT_GET_SUB_PLAN:
                    loadSubPlan();
                    break;
                case Constants.INTENT_GET_NEXT_SUB_PLAN:
                    loadNextSubPlan();
                    break;
                case Constants.INTENT_GET_NEXT_EVENT:
                    loadEvents(intent.getBooleanExtra("append", true));
                    break;
                case Constants.INTENT_GET_NEXT_NEWS:
                    loadNews(intent.getBooleanExtra("append", true));
                    break;
                case Constants.INTENT_GET_NEWS_CONTENT:
                    loadNewsData(intent.getIntExtra("id", -1));
                    break;
                case Constants.INTENT_GET_EVENT_CONTENT:
                    loadEventData(intent.getIntExtra("id", -1));
                    break;
            }
            update(intent.getAction());
        } catch (IOException | JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private String pullData(String req) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        String link = Constants.SERVER_URL + "?task=" + req;
        HttpGet request = new HttpGet( link);
        HttpResponse response = client.execute(request);
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }


    private void loadSubPlan() throws JSONException, ParseException, IOException {
        String data = pullData("vertretungsplan");
        int split = data.indexOf("#####");

        _today =  new SubstitutePlan(new JSONObject(data.substring(0, split)));
        _tomorrow =  new SubstitutePlan(new JSONObject(data.substring(split + 5)));
    }

    private void loadNextSubPlan() throws JSONException, ParseException
    {
        _tomorrow =  new SubstitutePlan(new JSONObject());
    }

    private void loadEvents(boolean append) throws IOException, JSONException {
        String str = null;

        //clearing _eventList if Necessary load File
        if (_eventList.isEmpty() && !append &&  new File(getFilesDir().getPath() + Constants.FILE_EVENT).exists()) {
            FileInputStream file = new FileInputStream(getFilesDir().getPath() + Constants.FILE_EVENT);
            byte temp[] = new byte[65535];
            int bytes = file.read(temp);
            if (bytes <= 2)
                return;
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes; i++)
                if (temp[i]!= 0)
                    sb.append((char)temp[i]);
            str = sb.toString();
        } else if (!_eventList.isEmpty() && ! append)updateHandler.post(new Runnable() {
            @Override
            public void run() {
                _eventList.clear();
            }
        });

        //pulling from Server if no File Loaded
        if (str == null)
        {
            str = pullData("events&tstamp=" + Long.toString(_eventList.isEmpty() ? Util.getTStampFromDate(Calendar.getInstance().getTime()) : _eventList.getItem(_eventList.getCount() - 1).getTime()));
        }

        //Reading JSON
        if (!str.isEmpty()) {
            JSONArray arr = new JSONArray(str);
            for (int i = 0; i < 10; i++) {
                final Event data = new Event(getApplicationContext(), new JSONObject(arr.getString(i)));
                updateHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        _eventList.add(data);
                    }
                });
            }
        }
    }

    private void loadNews(boolean append) throws IOException, JSONException {

        String str = null;

        //clearing _newsList if Necessary load File
        if (_newsList.isEmpty() && !append &&  new File(getFilesDir().getPath() + Constants.FILE_NEWS).exists()) {
            FileInputStream file = new FileInputStream(getFilesDir().getPath() + "/" + Constants.FILE_NEWS);
            byte temp[] = new byte[65535];
            int bytes = file.read(temp);
            if (bytes > 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bytes; i++)
                    if (temp[i] != 0)
                        sb.append((char) temp[i]);
                str = sb.toString();
            }
        } else if (!_newsList.isEmpty() && !append)updateHandler.post(new Runnable() {
                @Override
                public void run() {
                    _newsList.clear();
                }
            });

        //pulling from Server if no File Loaded
        if (str == null)
        {
            str = pullData("news&tstamp=" + Long.toString(_newsList.isEmpty() ? Util.getTStampFromDate(Calendar.getInstance().getTime()) : _newsList.getItem(_newsList.getCount() - 1).getTime()));
        }

        //Reading JSON
        if (!str.isEmpty()) {
            JSONArray arr = new JSONArray(str);
            for (int i = 0; i < 10; i++) {
                final News data = new News(getApplicationContext(), new JSONObject(arr.getString(i)));
                updateHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        _newsList.add(data);
                    }
                });
            }
        }
    }

    private void loadEventData(int id) throws IOException, JSONException {
        if (id == -1)
            throw new IllegalArgumentException("News ID Data couldn't be resolved");

        String data = pullData("eventcontent&id=" + id + "&images=false");
        _eventList.get(id).setExtData(data);
    }

    private void loadNewsData(int id) throws IOException, JSONException {
        if (id == -1)
            throw new IllegalArgumentException("News ID Data couldn't be resolved");

        String data = pullData("newscontent&id=" + id + "&images=false");
        StringBuilder build = new StringBuilder();
        JSONArray contentData = new JSONArray(data);

        for (int i = 0; i < contentData.length(); i++)
            build.append(contentData.getJSONObject(i).getString("text"));

        _newsList.get(id).setContent(build.toString());
    }

    private void update(final String type)
    {
        for (final UpdateListener listener: Listeners) {
            if (listener.getUpdateType() == type)
            {
                updateHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onUpdate(type);
                    }
                });
            }
        }
    }
}
