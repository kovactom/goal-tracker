package com.vsb.tamz.goaltracker;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateGoalActivity extends Activity {

    private Spinner categorySpinner;
    private Spinner repeatSpinner;
    private Spinner notificationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        categorySpinner = findViewById(R.id.spinner);
        repeatSpinner = findViewById(R.id.spinner2);
        notificationSpinner = findViewById(R.id.spinner3);

        init();
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
