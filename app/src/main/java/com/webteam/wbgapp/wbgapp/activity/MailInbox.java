package com.webteam.wbgapp.wbgapp.activity;

import android.os.Bundle;

import com.webteam.wbgapp.wbgapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by malte on 29.03.2016.
 */
public class MailInbox extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_inbox);
    }

    @Override
    protected String getName() {
        return getString(R.string.mail_inbox);
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {

    }

    @Override
    protected void load(FileInputStream file) throws IOException {

    }
}
