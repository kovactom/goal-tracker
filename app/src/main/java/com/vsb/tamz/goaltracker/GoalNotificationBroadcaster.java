package com.vsb.tamz.goaltracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class GoalNotificationBroadcaster extends BroadcastReceiver {

    public static final String CHANNEL_ID = "GOAL_TRACKER_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        String goalName = intent.getStringExtra("goalName");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_timeline_24px)
                .setContentTitle("Upcoming goal: " + goalName)
                .setContentText("Your daily goal is waiting for you!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(5444144, notificationBuilder.build());

        Toast.makeText(context, "Scheduled notification", Toast.LENGTH_LONG).show();
    }
}
