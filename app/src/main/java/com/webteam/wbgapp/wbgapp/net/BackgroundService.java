package com.webteam.wbgapp.wbgapp.net;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.BaseActivity;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

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
        if (!Listeners.contains( listener))
            Listeners.add(listener);
    }

    static SubstitutePlan _today, _tomorrow;
    public static EventListAdapter _eventList;
    public static NewsListAdapter _newsList;
    public static JSONObject _accData;//needs better solution

    private static final Handler updateHandler = new Handler();

    public BackgroundService()
    {
        super("BackgroundUpdater - DefaultConstruction");
    }

    public BackgroundService(String name) {
        super(name);
    }

    public void releaseMemory()
    {
        try {
            if (_today != null)
            {
                saveSubPlan();
                _today = null;
            }
            if (_tomorrow != null)
            {
                saveNextSubPlan();
                _tomorrow  = null;
            }

            if (_eventList != null && !_eventList.isEmpty())
            {
                saveEvents();
                updateHandler.post(new Runnable() {
                    @Override
                    public void run() { _eventList.clear(); }
                });
            }

            if (_newsList != null && !_newsList.isEmpty())
            {
                saveNews();
                updateHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        _newsList.clear();
                    }
                });
            }


            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (_newsList == null)
            _newsList = new NewsListAdapter( getApplicationContext(), R.layout.display_news_element, R.id.article_element_title, new ArrayList<News>());
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

                case Constants.INTENT_SAVE_SUB_PLAN:
                    saveSubPlan();
                    break;
                case Constants.INTENT_SAVE_NEXT_SUB_PLAN:
                    saveNextSubPlan();
                    break;
                case Constants.INTENT_SAVE_EVENTS:
                    saveEvents();
                    break;
                case Constants.INTENT_SAVE_NEWS:
                    saveNews();
                    break;

                case Constants.INTENT_CHECK_LOGIN:
                    verifyLogin();
                    break;

                case Constants.INTENT_RELEASE_MEMORY:
                    releaseMemory();
                    break;
            }
        } catch (IOException | JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private String pullData(String req) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        String link;
        if (req.equalsIgnoreCase("vertretungsplan") || req.equalsIgnoreCase("login"))
        {
            String login = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE).getString("login", "");
            if (login.length() > 0)
                link = Constants.SERVER_URL + "?task=" + req + login;
            else
                return "false";
        }
        else
        link = Constants.SERVER_URL + "?task=" + req;
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

    private void verifyLogin() throws IOException, JSONException
    {
        _accData = new JSONObject(pullData("login"));
        if (_accData.getBoolean("login"))
            update(Constants.INTENT_CHECK_LOGIN);
        else
        {
            getSharedPreferences("Settings", MODE_PRIVATE).edit().putString("login", null).apply();
            getSharedPreferences("Settings", MODE_PRIVATE).edit().putString("user", null).apply();
            getSharedPreferences("Settings", MODE_PRIVATE).edit().putString("pw", null).apply();
        }
    }

    private void saveSubPlan() throws IOException {
        FileOutputStream file = openFileOutput(Constants.FILE_SUB_PLAN, MODE_PRIVATE);
        file.write(_today.toString().getBytes());
    }

    private void loadSubPlan() throws JSONException, ParseException, IOException {

        try {
            FileInputStream file = openFileInput(Constants.FILE_SUB_PLAN);
            byte[] buffer = new byte[65535];

            StringBuilder sb = new StringBuilder();
            while (-1 != file.read(buffer)) {
                sb.append(new String(buffer, "UTF-8"));
            }
            String str = sb.toString();
            str.trim();
            _today = new SubstitutePlan(new JSONObject(str));
        } catch (FileNotFoundException e)
        {
            String data = pullData("vertretungsplan");
            if (data.equalsIgnoreCase("false"))
                return;

            _today =  new SubstitutePlan(new JSONObject(data));//errors
        }
        update(Constants.INTENT_GET_SUB_PLAN);
    }

    private void saveNextSubPlan() throws IOException {
        FileOutputStream file = openFileOutput("NextSubPlanCache.bin", MODE_PRIVATE);
        file.write(_tomorrow.toString().getBytes());
    }

    private void loadNextSubPlan() throws JSONException, ParseException
    {
        _tomorrow =  new SubstitutePlan(new JSONObject());
        update(Constants.INTENT_GET_NEXT_SUB_PLAN);
    }

    private void saveEvents() throws IOException {
        FileOutputStream file = openFileOutput("EventCache.bin", MODE_PRIVATE);
        JSONArray arr = new JSONArray();
        if (!_eventList.isEmpty())
            for (int i = 0; i < _eventList.getCount(); i++)
                arr.put(_eventList.getItem(i).toString());
        file.write(arr.toString().getBytes());
    }

    private void loadEvents(boolean append) throws IOException, JSONException {
        String str = null;

        //clearing _eventList if Necessary load File
        if (_eventList.isEmpty()) {
            try {
                FileInputStream file = openFileInput(Constants.FILE_EVENT);
                byte[] buffer = new byte[65535];

                StringBuilder sb = new StringBuilder();
                while (-1 != file.read(buffer)) {
                    sb.append(new String(buffer, "UTF-8"));
                }
                str = sb.toString();
                str.trim();
            } catch (FileNotFoundException expected)
            {
                Log.i("EventLoader", "couldn't open Cache File will try to download.");
            }
            } else if (!_eventList.isEmpty() && ! append)updateHandler.post(new Runnable() {
            @Override
            public void run() {
                _eventList.clear();
                _eventList.notifyDataSetChanged();
            }
        });

        //pulling from Server if no File Loaded
        if (str == null || str.charAt(1) == ']')
        {
            str = pullData("events&tstamp=" + Long.toString(_eventList.isEmpty() ? Util.getTStampFromDate(Calendar.getInstance().getTime()) : _eventList.getItem(_eventList.getCount() - 1).getTime()));
        }

        //Reading JSON
        if (!str.equals("")) {
            JSONArray arr = new JSONArray(str);

            for (int i = arr.length() - 1; i >= 0; i--) {
                final Event data = new Event( new JSONObject(arr.getString(i)));
                updateHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        _eventList.add(data);
                        _eventList.notifyDataSetChanged();
                    }
                });
            }
            update(Constants.INTENT_GET_NEXT_EVENT);
        }
    }

    private void saveNews() throws IOException {
        FileOutputStream file = openFileOutput("NewsCache.bin", MODE_PRIVATE);
        JSONArray arr = new JSONArray();
        if ( _newsList != null && !_newsList.isEmpty())
            for (int i = 0; i < _newsList.getCount(); i++)
                arr.put(_newsList.getItem(i).toString());
        file.write(arr.toString().getBytes());
    }

    private void loadNews(boolean append) throws IOException, JSONException {

        String str = null;
        //clearing _newsList if Necessary load File
        if (_newsList.isEmpty()) {
            try {
                FileInputStream file = openFileInput(Constants.FILE_NEWS);
                byte[] buffer = new byte[65535];

                StringBuilder sb = new StringBuilder();
                while (-1 != file.read(buffer)) {
                    sb.append(new String(buffer, "UTF-8"));
                }
                str = sb.toString();
            } catch (FileNotFoundException expected)
            {
                Log.i("NewsLoader", "couldn't open Cache File will try to download.");
            }
        } else if (!_newsList.isEmpty() && !append)updateHandler.post(new Runnable() {
                @Override
                public void run() {
                    _newsList.clear();
                    _eventList.notifyDataSetChanged();
                }
            });

        //pulling from Server if no File Loaded
        if (str == null)
        {
            str = pullData("news&tstamp=" + Long.toString(_newsList.isEmpty() ? Util.getTStampFromDate(Calendar.getInstance().getTime()) : _newsList.getItem(_newsList.getCount() - 1).getTime()));
        }

        //Reading JSON
        if (!str.equals("")) {
            JSONArray arr = new JSONArray(str);
            for (int i = 0; i < arr.length(); i++) {
                final News data = new News( new JSONObject(arr.getString(i)));
                updateHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        _newsList.add(data);
                        _eventList.notifyDataSetChanged();
                    }
                });
            }
            update(Constants.INTENT_GET_NEXT_NEWS);
        }
    }

    private void loadEventData(int id) throws IOException, JSONException {
        if (id == -1)
            throw new IllegalArgumentException("News ID Data couldn't be resolved");

        String data = pullData("eventcontent&id=" + id + "&images=false");
        if (!data.equals("")) {
            _eventList.get(id).setExtData(data);
            update(Constants.INTENT_GET_EVENT_CONTENT);
        }
    }

    private void loadNewsData(int id) throws IOException, JSONException {
        if (id == -1)
            throw new IllegalArgumentException("News ID Data couldn't be resolved");

        String data = pullData("newscontent&id=" + id + "&images=false");
        StringBuilder build = new StringBuilder();
        JSONArray contentData = new JSONArray(data);

        for (int i = 0; i < contentData.length(); i++)
            build.append(contentData.getJSONObject(i).getString("text"));

        if (build.length() > 0) {
            _newsList.get(id).setContent(build.toString());
            update(Constants.INTENT_GET_NEWS_CONTENT);
        }
    }

    private void update(final String type)
    {
        for (final UpdateListener listener: Listeners) {
            if (listener.getUpdateType().equals(type))
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
