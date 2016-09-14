package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;

import com.webteam.wbgapp.wbgapp.R;

/**
 * Created by Deathlymad on 01.06.2016.
 */
public class HomeScreen extends BaseActivity {
    protected String getName(){
        return "WBGApp - Startseite";
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
    }

}
