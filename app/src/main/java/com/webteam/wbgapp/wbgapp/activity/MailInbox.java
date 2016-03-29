package com.webteam.wbgapp.wbgapp.activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by malte on 29.03.2016.
 */
public class MailInbox extends BaseActivity{
    @Override
    protected String getName() {
        return "@string/mail_inbox";
    }

    @Override
    protected void save(FileOutputStream file) throws IOException {

    }

    @Override
    protected void load(FileInputStream file) throws IOException {

    }
}
