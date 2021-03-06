package com.vsb.tamz.goaltracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.vsb.tamz.goaltracker.persistence.AppDatabase;
import com.vsb.tamz.goaltracker.persistence.model.Goal;
import com.vsb.tamz.goaltracker.persistence.model.GoalNotification;
import com.vsb.tamz.goaltracker.persistence.repository.GoalRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GoalDetailActivity extends AppCompatActivity {

    private static final int EDIT_REQUEST_CODE = 1;

    private AlarmManager alarmManager;
    private AppDatabase db;
    private GoalRepository goalRepository;
    private Toolbar toolbar;
    private ImageView goalPicture;
    private TextView goalName;
    private TextView goalCategory;
    private TextView goalLocation;
    private TextView goalRepeat;
    private TextView goalDuration;
    private TextView goalScore;
    private TextView goalFrom;
    private TextView goalTo;
    private TextView goalTime;
    private TextView goalNotification;

    private Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        toolbar = findViewById(R.id.toolbarGoalDetail);
        goalPicture = findViewById(R.id.goalDetailImage);
        goalName = findViewById(R.id.goalDetailName);
        goalCategory = findViewById(R.id.goalDetailCategory);
        goalLocation = findViewById(R.id.goalDetailLocation);
        goalRepeat = findViewById(R.id.goalDetailRepeat);
        goalDuration = findViewById(R.id.goalDetailDuration);
        goalScore = findViewById(R.id.goalDetailScore);
        goalFrom = findViewById(R.id.goalDetailFrom);
        goalTo = findViewById(R.id.goalDetailTo);
        goalTime = findViewById(R.id.goalDetailTime);
        goalNotification = findViewById(R.id.goalDetailNotification);

        db = AppDatabase.getDatabase(this);
        goalRepository = new GoalRepository(getApplication());

        Intent intent = getIntent();
        long goalId = intent.getLongExtra("goalId", 0);
        loadFromDatabase(goalId);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_goal_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case EDIT_REQUEST_CODE: {
//                cancelNotifications();
                recreate();
            }break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void delete(MenuItem item) {
        cancelNotifications();
        goalRepository.delete(goal);
        finish();
    }

    public void edit(MenuItem item) {
        Intent intent = new Intent(this, CreateGoalActivity.class);
        intent.putExtra("editMode", true);
        intent.putExtra("goalId", goal.getId());

        startActivityForResult(intent, EDIT_REQUEST_CODE);
        loadFromDatabase(goal.getId());
    }

    private void loadFromDatabase(long goalId) {
        goal = db.goalDao().findOneById(goalId);
        populateFromObject();
    }

    private void cancelNotifications() {
        for (GoalNotification goalNotification : db.goalNotificationDao().findAllByGoalId(goal.getId())) {
            Intent intent = new Intent(getApplicationContext(), GoalNotificationBroadcaster.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) goalNotification.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(alarmIntent);
        }
        db.goalNotificationDao().deleteByGoalId(goal.getId());
    }

    private void populateFromObject() {
        final String[] repeats = getResources().getStringArray(R.array.cg_sp_repeat);
        final String[] categories = getResources().getStringArray(R.array.cg_sp_category);
        final String[] notifications = getResources().getStringArray(R.array.cg_sp_notification);

        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        DateFormat timeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
        goalPicture.setImageURI(Uri.parse(goal.getPicture()));
        goalName.setText(goal.getName());
        goalCategory.setText(categories[(int) goal.getCategoryId()]);
        goalLocation.setText(goal.getLocation());
        goalRepeat.setText(repeats[(int) goal.getRepeatId()]);
        goalDuration.setText(goal.getDuration() + " minutes");
        goalScore.setText(goal.getScore(db.goalProgressDao().getCountByGoalId(goal.getId())) + "/" + goal.getMaximumScore() + " minutes");
        goalFrom.setText(dateFormat.format(goal.getFrom()));
        goalTo.setText(dateFormat.format(goal.getTo()));
        goalTime.setText(timeFormat.format(goal.getTime()));
        goalNotification.setText(notifications[(int) goal.getNotificationId()]);
    }
}
