package com.vsb.tamz.goaltracker;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView cardListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

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
}
