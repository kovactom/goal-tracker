package com.vsb.tamz.goaltracker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.vsb.tamz.goaltracker.persistence.AppDatabase;
import com.vsb.tamz.goaltracker.persistence.model.Goal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class OverviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppDatabase db;
    private AlarmManager alarmManager;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
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
        navigationView = findViewById(R.id.nav_view);
        cardListView = findViewById(R.id.cardListView);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);

        navigationView.setNavigationItemSelectedListener(this);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "goals").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        List<Goal> goals = db.goalDao().findAll();
        final String[] repeats = getResources().getStringArray(R.array.cg_sp_repeat);
        List<GoalCard> data = new ArrayList<>();
        for(Goal val : goals) {
            long doneGoalSteps = db.goalProgressDao().getCountByGoalId(val.getId());
            long duration = doneGoalSteps * val.getDuration();
            long unitsBetween = getUnitsBetweenDates(val.getFrom(), val.getTo(), (int) val.getRepeatId());
            long totalDuration = unitsBetween * val.getDuration();
            boolean active = isTodayGoal(val.getFrom(), val.getTo(), (int) val.getRepeatId());
            data.add(new GoalCard(val.getName(), repeats[(int) val.getRepeatId()], val.getDuration() + " minutes", duration + "/" + totalDuration + " minutes", active));
        }
//        List<GoalCard> data = new ArrayList<>();
//        data.add(new GoalCard("Learn programming", "Every day", "30 minutes", "120 / 240 minutes"));
//        data.add(new GoalCard("Learn foreign language", "Every week", "60 minutes", "60 / 480 minutes"));
//        data.add(new GoalCard("Train ping-pong", "Every month", "15 minutes", "45 / 520 minutes"));
//        data.add(new GoalCard("Train cooking", "Every day", "30 minutes", "90 / 900 minutes"));

        GoalCardListViewAdapter adapter = new GoalCardListViewAdapter(getApplicationContext(), R.layout.goal_card_list_view_adapter, data);
        cardListView.setAdapter(adapter);
    }

    private long getUnitsBetweenDates(Date from, Date to, int repeatId) {
        long diff = to.getTime() - from.getTime();
        switch (repeatId) {
            case 0: {
                return diff / (1000L * 60 * 60 * 24 );
            }
            case 1: {
                return diff / (1000L * 60 * 60 * 24 * 7);
            }
            case 2: {
                return diff / (1000L * 60 * 60 * 24 * 30);
            }
        }
        return 0;
    }

    private boolean isTodayGoal(Date from, Date to, int repeatId) {
        GregorianCalendar targetDate = new GregorianCalendar();
        GregorianCalendar currentDate = new GregorianCalendar();
        targetDate.setTime(from);
        switch (repeatId) {
            case 0: {
                return currentDate.getTimeInMillis() >= from.getTime() && currentDate.getTimeInMillis() <= to.getTime();
            }
            case 1: {
                return targetDate.get(Calendar.DAY_OF_WEEK) == currentDate.get(Calendar.DAY_OF_WEEK);
            }
            case 2: {
                return targetDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH);
            }
        }

        return false;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }break;

        }
        return true;
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
