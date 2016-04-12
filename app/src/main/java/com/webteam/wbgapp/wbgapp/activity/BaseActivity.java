package com.webteam.wbgapp.wbgapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.webteam.wbgapp.wbgapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Deathlymad on 23.01.2016 .
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean _activityInvalidation = false;

    protected abstract String getName();
    protected abstract void save(FileOutputStream file) throws IOException;
    protected abstract void load(FileInputStream file) throws IOException;
    protected boolean needsFile(){ return true;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFile();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause()
    {
        saveFile();
        super.onPause();
    }

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_layout, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.display_container);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        setTitle(getName());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wbgapp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent i = new Intent(this, SettingsAccount.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent i;

        switch (id) {
            case R.id.nav_news:
                i = new Intent(this, WBGApp.class);
                break;
            case R.id.nav_substitute_plan:
                i = new Intent(this, SubstitutePlanActivity.class);
                break;
            /*
            case R.id.nav_schedule:
                i = new Intent(this, Schedules.class);
                break;
            */
            case R.id.nav_events:
                i = new Intent(this, EventSchedule.class);
                break;
            /*
            case R.id.mail_inbox:
                i = new Intent(this, MailInbox.class);
                break;
            case R.id.nav_send:
                i = new Intent(this, ForumNotification.class);
                break;
            case R.id.nav_messages:
                i = new Intent(this, ForumMessages.class);
                break;
             */
            /*
            case R.id.nav_settings_account:
                i = new Intent(this, SettingsAccount.class);
                break;
            case R.id.nav_settings_notifications:
                i = new Intent(this, SettingsNotification.class);
                break;
            case R.id.nav_settings_datausage:
                i = new Intent(this, SettingsDatausage.class);
                break;
            */
            default:
                i = new Intent();
                break;
        }

        startActivity(i);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void saveFile()
    {
        if (!needsFile())
            return;
        try {
            if (!_activityInvalidation) {
                FileOutputStream out = openFileOutput(getName(), Context.MODE_PRIVATE);
                save(out);
                out.flush();
                out.close();
            } else
                deleteFile(getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFile()
    {
        if (!needsFile())
            return;
        try {
            FileInputStream in = openFileInput(getName());
            load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void invalidateChaches()
    {
        _activityInvalidation = true;
    }

    protected SharedPreferences getSettings()
    {
        return getSharedPreferences("Settings", MODE_PRIVATE);
    }
}
