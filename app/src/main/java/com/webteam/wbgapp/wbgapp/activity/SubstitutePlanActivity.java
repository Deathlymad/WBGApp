package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.fragment.SubPagerAdapter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//TODO: Auf Umstellung des Webservices achten! Es wird jetzt der richtige Link zurückgegeben (WBGYM) (Philip)
//TODO: Design anpassen - Feld "Klasse" jetzt nicht mehr nötig (Malte)
//TODO: Vertretungsplan für den nächsten Tag ins Menü einbinden + Algorhytmus

public class SubstitutePlanActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_substitute_plan);
        ViewPager pager = (ViewPager)findViewById(R.id.sub_plan_swipe_view);
        if (pager != null)
            pager.setAdapter(new SubPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    protected String getName() {
        return getString(R.string.substitute_plan_title);
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {
    }

    @Override
    protected void load(FileInputStream file) throws IOException {

    }
}