package com.vsb.tamz.goaltracker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView cardListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        this.createNotificationChannel();
        this.scheduleNotifications();

        toolbar = findViewById(R.id.overview_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        cardListView = findViewById(R.id.cardListView);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);


        List<GoalCard> data = new ArrayList<>();
        data.add(new GoalCard("Learn programming", "Every day", "30 minutes", "120 / 240 minutes"));
        data.add(new GoalCard("Learn foreign language", "Every week", "60 minutes", "60 / 480 minutes"));
        data.add(new GoalCard("Train ping-pong", "Every month", "15 minutes", "45 / 520 minutes"));
        data.add(new GoalCard("Train cooking", "Every day", "30 minutes", "90 / 900 minutes"));

        GoalCardListViewAdapter adapter = new GoalCardListViewAdapter(getApplicationContext(), R.layout.goal_card_list_view_adapter, data);
        cardListView.setAdapter(adapter);
    }

    private void scheduleNotifications() {
        Intent intent = new Intent(getApplicationContext(), GoalNotificationBroadcaster.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 52);
        calendar.set(Calendar.SECOND, 30);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_overview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNewGoal(View view) {
        Intent intent = new Intent(this, CreateGoalActivity.class);
        startActivity(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(GoalNotificationBroadcaster.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
