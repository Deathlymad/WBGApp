package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.DatabaseHandler;
import com.webteam.wbgapp.wbgapp.net.IRequest;
import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SubstitutePlanActivity extends BaseActivity implements IRequest{

    private SubstitutePlan plan = null;

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
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_substitute_plan);
        new DatabaseHandler().execute(this);
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
    protected void load(FileInputStream file) throws IOException {
        try{
            plan = new SubstitutePlan(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file));
        } catch (IOException e)
        {
            throw e;
        } catch (ParserConfigurationException | SAXException | ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getRequest() {
        return new String[]{"substituteplan"};
    }

    @Override
    public void handleResults(String... result) {
        JSONObject json = null;
        try {
            json = new JSONObject(result[0]);
            new PlanPuller(this, json.getString("today")).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
