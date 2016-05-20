package com.webteam.wbgapp.wbgapp.activity;

import android.app.Application;
import android.content.Intent;

import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.util.Constants;

/**
 * Created by Deathlymad on 20.05.2016.
 */
public class WBGApp extends Application {

    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_RELEASE_MEMORY);
        startService(i);
    }

    @Override
    public void onTerminate()
    {
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_RELEASE_MEMORY);
        startService(i);
        super.onTerminate();
    }
}
