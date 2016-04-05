package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.fragment.SubPlanAdapter;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//needs to be Fragment Activity
public class SubstitutePlanActivity extends BaseActivity implements IRequest {

    private SubstitutePlan plan = null;
    private DatabaseHandler handler = null;
    private PlanPuller puller = null;
    private int  state = 0;

    private class PlanPuller extends Thread
    {
        SubstitutePlanActivity _owner = null;
        String _url = null;
        public PlanPuller(SubstitutePlanActivity owner, String url)
        {
            _owner = owner;
            _url = url;
        }

        @Override
        public void run()
        {
            try{
                plan = new SubstitutePlan(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL(_url).openStream()));
            } catch (SAXException | IOException | ParserConfigurationException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        state = 0;
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_substitute_plan);
        try {
            if (handler != null)
                handler.get(10000, TimeUnit.MILLISECONDS);
            if (puller != null)
                    puller.join();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        setupFragment();
    }

    @Override
    protected String getName() {
        return getString(R.string.substitute_plan_title);
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
        if (plan != null)
            plan.save(file);
    }

    @Override
    protected void load(FileInputStream file) throws IOException {/*
        try{
            plan = new SubstitutePlan(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file));
        } catch (IOException e)
        {
            throw e;
        } catch (ParserConfigurationException | SAXException | ParseException e)
        {
            e.printStackTrace();
        }*/
        if (plan == null) {
            handler = new DatabaseHandler();
            handler.execute(this);
        }
    }

    @Override
    public String[] getRequest() {
        return new String[]{"substituteplan"};
    }

    @Override
    public void handleResults(String... result) {
        try {
            JSONObject json = new JSONObject(result[0]);
            puller = new PlanPuller(this, "http://wbgym.de/files/vertretungsplan/public/2016-04-05.xml");
            puller.start();//json.getString("today")
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupFragment()
    {
        if (plan != null)
        {
            ((ListView)findViewById(R.id.sub_list)).setAdapter(new SubPlanAdapter(this, R.layout.display_sub_plan_list_layout, R.id.sub_plan_layout_text, plan.pack()));

        }
    }
}
