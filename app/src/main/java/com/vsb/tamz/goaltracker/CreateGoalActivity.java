package com.vsb.tamz.goaltracker;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateGoalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner categorySpinner;
    private Spinner repeatSpinner;
    private Spinner notificationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        toolbar = findViewById(R.id.toolbar);
        categorySpinner = findViewById(R.id.spinner);
        repeatSpinner = findViewById(R.id.spinner2);
        notificationSpinner = findViewById(R.id.spinner3);

        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_create_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        final ArrayAdapter<CharSequence> categoryArray = ArrayAdapter.createFromResource(this, R.array.cg_sp_category, android.R.layout.simple_spinner_item);
        categoryArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryArray);

        final ArrayAdapter<CharSequence> repeatArray = ArrayAdapter.createFromResource(this, R.array.cg_sp_repeat, android.R.layout.simple_spinner_item);
        repeatArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(repeatArray);

        final ArrayAdapter<CharSequence> notificationArray = ArrayAdapter.createFromResource(this, R.array.cg_sp_notification, android.R.layout.simple_spinner_item);
        notificationArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationSpinner.setAdapter(notificationArray);
    }

}
