package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.structure.Account;

/**
 * Created by malte on 21.01.2016.
 */
public class SettingsAccount extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_settings_account);
    }

    @Override
    protected String getName() {
        return getString(R.string.settings_account_title);
    }

    public void login(View view) {
        String name = ((TextView) findViewById(R.id.settings_registration_user)).getText().toString();
        String pw = ((TextView) findViewById(R.id.settings_registration_password)).getText().toString();
        Account acc = new Account(name);
        acc.login(pw);
        settings.setCurrAccount(acc);
    }
}

