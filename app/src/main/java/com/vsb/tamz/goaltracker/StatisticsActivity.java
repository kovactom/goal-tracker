package com.vsb.tamz.goaltracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.vsb.tamz.goaltracker.persistence.AppDatabase;

public class StatisticsActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextView goalCountText;
    private TextView goalProgressCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        goalCountText = findViewById(R.id.goalCount);
        goalProgressCountText = findViewById(R.id.goalProgressCount);

        db = AppDatabase.getDatabase(this);
        long goalCount = db.goalDao().getGoalCount();
        long goalProgressCount = db.goalProgressDao().getGoalProgressCount();

        goalCountText.setText(Long.toString(goalCount));
        goalProgressCountText.setText(Long.toString(goalProgressCount));
    }
}
