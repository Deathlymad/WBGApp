package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;

import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SubstitutePlanActivity extends BaseActivity {

    private SubstitutePlan plan;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_substitute_plan);
    }

    @Override
    protected String getName() {
        return getString(R.string.substitute_plan_title);
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
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
}
