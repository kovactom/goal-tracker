package com.vsb.tamz.goaltracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class OverviewActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        toolbar = findViewById(R.id.overview_toolbar);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_overview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void createNewGoal(View view) {
        Intent intent = new Intent(this, CreateGoalActivity.class);
        startActivity(intent);
    }
}
