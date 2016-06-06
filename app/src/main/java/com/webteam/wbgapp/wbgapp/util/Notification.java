package com.webteam.wbgapp.wbgapp.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.activity.BaseActivity;

//might be redundant
@Deprecated
public class Notification {
    public Notification(String _text, Activity sender, Class< ? extends Activity> activityClass)
    {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(sender)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle("WBGApp Notification")
            .setContentText(_text);

        Intent resultIntent = new Intent(sender, activityClass);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(sender);

        stackBuilder.addParentStack(sender);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) sender.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }
}
