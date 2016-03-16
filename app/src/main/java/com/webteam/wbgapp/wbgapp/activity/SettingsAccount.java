package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.structure.Account;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    @Override
    protected void save(FileOutputStream file) throws IOException {

    }

    @Override
    protected void load(FileInputStream file) throws IOException {

    }

    public void login(View view) {
        try {
            assert ((TextView) findViewById(R.id.settings_registration_user)) != null;
            String name = ((TextView) findViewById(R.id.settings_registration_user)).getText().toString();
            assert ((TextView) findViewById(R.id.settings_registration_password)) != null;
            String pw = ((TextView) findViewById(R.id.settings_registration_password)).getText().toString();
            Account acc = new Account(name);
            acc.login(pw);
            settings.setCurrAccount(acc);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

