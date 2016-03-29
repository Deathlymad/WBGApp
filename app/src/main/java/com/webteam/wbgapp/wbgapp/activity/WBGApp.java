package com.webteam.wbgapp.wbgapp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.structure.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@TargetApi(Build.VERSION_CODES.M)
public class WBGApp extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ArrayList<News> _newsStack;
    public static final String requestTitle = "com.webteam.wbgapp.wbgapp.NEWS";

    private class NewsRequest implements IRequest
    {
        long _tstamp;
        WBGApp ref;

        public NewsRequest(WBGApp r)
        {
            ref = r;
            _tstamp = Calendar.getInstance().getTimeInMillis();
            DatabaseHandler hand = new DatabaseHandler();
            hand.execute(this);
        }

        public NewsRequest(WBGApp r, long tStamp)
        {
            ref = r;
            _tstamp = tStamp;
            DatabaseHandler hand = new DatabaseHandler();
            hand.execute(this);
        }

        @Override
        public String[] getRequest() {
            return new String[]{"news&tstamp=" + Long.toString(_tstamp)};
        }

        @Override
        public void handleResults(String... result) {
            String res = result[0];
            try {
                JSONArray newsList = new JSONArray(res);
                for (int i = 0; i < 10; i++) {
                    ref.addNewsToStack(new News(newsList.getJSONObject(i)));
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        _newsStack = new ArrayList<>();
        setContentView(R.layout.activity_wbgapp);
        super.onCreate(savedInstanceState);
        View layout = findViewById(R.id.swipe_container);


        Button bt = (Button)layout.findViewById(R.id.news_button_reload);
        bt.setOnClickListener(this);

        ((SwipeRefreshLayout)layout).setOnRefreshListener(this);
    }

    @Override
    protected String getName() {
        return "News";
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
        JSONArray arr = new JSONArray();
        if (!_newsStack.isEmpty())
            for (News n : _newsStack)
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
                    addNewsToStack(new News(new JSONObject(arr.getString(i))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void onRefresh(){
        try {
            ((SwipeRefreshLayout) findViewById(R.id.swipe_container)).setRefreshing(true);
            if (!((SwipeRefreshLayout) findViewById(R.id.swipe_container)).canChildScrollUp()) {
                LinearLayout list = (LinearLayout)findViewById(R.id.news_container);
                for (News n : _newsStack)
                    list.removeViewInLayout(n.getView());
                _newsStack.clear();
                new NewsRequest(this);
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
        {   if (_newsStack.size() > 0)
                new NewsRequest(this, _newsStack.get(_newsStack.size() - 1).getTime());
            else
                new NewsRequest(this);
        }
        else {
            TextView entry = (TextView) v;
            News article = null;
            for (News n : _newsStack) //probably needs faster approach
            {
                String a = n.getTitle();
                String b = entry.getText().toString();
                if (a.equals(b))
                    article = n;
            }
            if (article != null) {
                Intent i = new Intent(this, NewsArticle.class);
                i.putExtra(requestTitle, article.toString());
                startActivity(i);
            }
        }
    }

    private void addNewsToStack(News n)
    {
        _newsStack.add(n);
        n.addView(this);
    }
}
