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

/**
 * Created by Deathlymad on 22.03.2016 .
 */
public class Notification {
    public Notification(String _text, Activity sender, Class<BaseActivity> activityClass)
    {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(sender)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(sender, activityClass);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(sender);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(activityClass);
        // Adds the Intent that starts the Activity to the top of the stack
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
