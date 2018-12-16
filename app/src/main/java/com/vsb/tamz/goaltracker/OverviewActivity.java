package com.vsb.tamz.goaltracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.vsb.tamz.goaltracker.persistence.model.GoalProgress;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OverviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int GOAL_DETAIL_REQUEST_CODE = 1;
    public static final int CREATE_GOAL_REQUEST_CODE = 2;

    private static final int FILTER_ALL = 1;
    private static final int FILTER_TODAY = 2;

    private AppDatabase db;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ListView cardListView;
    private int currentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        this.createNotificationChannel();

        toolbar = findViewById(R.id.overview_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        cardListView = findViewById(R.id.cardListView);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);

        navigationView.setNavigationItemSelectedListener(this);
        db = AppDatabase.getDatabase(this);

        loadGoalsIntoCards();
    }

    private boolean containsTodayProgress(List<GoalProgress> goalProgresses) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        String currentDate = dateFormat.format(new Date());
        for(GoalProgress val : goalProgresses) {
            if (dateFormat.format(val.getDate()).equals(currentDate)) return true;
        }
        return false;
    }

    private void loadGoalsIntoCards() {
        List<Goal> goals = db.goalDao().findAll();
        List<GoalCard> data = new ArrayList<>();
        final String[] repeats = getResources().getStringArray(R.array.cg_sp_repeat);
        for(Goal val : goals) {
            if (currentFilter == FILTER_TODAY && !val.isTodayGoal()) continue;

            List<GoalProgress> goalProgresses = db.goalProgressDao().findAllByGoalId(val.getId());
            long doneUnits = db.goalProgressDao().getCountByGoalId(val.getId());
            boolean active = val.isTodayGoal() && !containsTodayProgress(goalProgresses);
            data.add(new GoalCard(val.getId(), val.getName(), repeats[(int) val.getRepeatId()], val.getDuration() + " minutes", val.getScore(doneUnits) + "/" + val.getMaximumScore() + " minutes", val.getPicture(), active));
        }
        GoalCardListViewAdapter adapter = new GoalCardListViewAdapter(this, R.layout.goal_card_list_view_adapter, data);
        cardListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            case R.id.nav_today: {
                currentFilter = FILTER_TODAY;
                loadGoalsIntoCards();
            }break;
            case R.id.nav_statistics: {
                Intent intent = new Intent(this, StatisticsActivity.class);
                startActivity(intent);
            }break;
            case R.id.nav_all: {
                currentFilter = FILTER_ALL;
                loadGoalsIntoCards();
            }break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CREATE_GOAL_REQUEST_CODE: recreate();break;
            case GOAL_DETAIL_REQUEST_CODE: recreate();break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void createNewGoal(View view) {
        Intent intent = new Intent(this, CreateGoalActivity.class);
        startActivityForResult(intent, CREATE_GOAL_REQUEST_CODE);
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
