package com.vsb.tamz.goaltracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OverviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
    }

    public void createNewGoal(View view) {
        Intent intent = new Intent(this, CreateGoalActivity.class);
        startActivity(intent);
    }
}
