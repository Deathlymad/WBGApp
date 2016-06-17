package com.webteam.wbgapp.wbgapp.activity.forum;

import android.os.Bundle;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.BaseActivity;

/**
 * Created by Deathlymad on 23.01.2016 .
 */
public class ForumActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle SaveInstantState) {

        super.onCreate(SaveInstantState);
        setContentView(R.layout.activity_forum_main);

    }

    @Override
    protected String getName() {
        return "Forum Main";
    }
}
