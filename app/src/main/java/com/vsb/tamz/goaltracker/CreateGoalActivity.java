package com.vsb.tamz.goaltracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class CreateGoalActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final int FROM_DATE_PICKER = 1;
    private static final int TO_DATE_PICKER = 2;

    private Toolbar toolbar;
    private Spinner categorySpinner;
    private Spinner repeatSpinner;
    private Spinner notificationSpinner;
    private EditText fromText;
    private EditText toText;
    private EditText timeText;
    private int currentOpenedDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        toolbar = findViewById(R.id.toolbar);
        categorySpinner = findViewById(R.id.cg_category);
        repeatSpinner = findViewById(R.id.cg_repeat);
        notificationSpinner = findViewById(R.id.cg_notification);
        fromText = findViewById(R.id.cg_from);
        toText = findViewById(R.id.cg_to);
        timeText = findViewById(R.id.cg_time);

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

        fromText.setOnClickListener(this);
        toText.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();


        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        if (view.equals(fromText)) currentOpenedDatePicker = FROM_DATE_PICKER;
        else if (view.equals(toText)) currentOpenedDatePicker = TO_DATE_PICKER;

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String formattedDate = dateFormatter.format(calendar.getTime());

        switch (currentOpenedDatePicker) {
            case FROM_DATE_PICKER: fromText.setText(formattedDate);break;
            case TO_DATE_PICKER: toText.setText(formattedDate);break;
        }
    }
}
