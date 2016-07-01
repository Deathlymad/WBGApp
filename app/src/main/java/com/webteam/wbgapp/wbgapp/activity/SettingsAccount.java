package com.webteam.wbgapp.wbgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.structure.Account;
import com.webteam.wbgapp.wbgapp.util.Constants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by malte on 21.01.2016.
 */
public class SettingsAccount extends BaseActivity implements View.OnClickListener {

    private static Account currAcc = null;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_settings_account);
        Object btn = findViewById(R.id.settings_registration_confirm);
        if (btn != null)
            ((Button)btn).setOnClickListener(this);
    }

    @Override
    protected String getName() {
        return getString(R.string.settings_account_title);
    }

    @Override
    public void onClick(View v) {
        String name = ((TextView) findViewById(R.id.settings_registration_user)).getText().toString();
        String pw = ((TextView) findViewById(R.id.settings_registration_password)).getText().toString();
        if (name.contains("@"))
            name = name.substring(0, name.indexOf("@"));
        currAcc = new Account(name, pw, getSettings());
        Intent i = new Intent( this, BackgroundService.class); // move to NewsArticle
        i.setAction(Constants.INTENT_CHECK_LOGIN);
        startService(i);
    }

    public static boolean isLoggedIn()
    {
        return currAcc != null;
    }

    public static Account getCurrentAccount()
    {
        return currAcc;
    }
}

