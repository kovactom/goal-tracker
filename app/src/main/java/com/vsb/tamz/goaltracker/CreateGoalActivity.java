package com.vsb.tamz.goaltracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vsb.tamz.goaltracker.persistence.AppDatabase;
import com.vsb.tamz.goaltracker.persistence.model.Goal;
import com.vsb.tamz.goaltracker.persistence.model.GoalNotification;
import com.vsb.tamz.goaltracker.persistence.repository.GoalNotificationRepository;
import com.vsb.tamz.goaltracker.persistence.repository.GoalRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateGoalActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int FROM_DATE_PICKER = 1;
    private static final int TO_DATE_PICKER = 2;
    private static final int FILE_PICKER_OPEN_REQUEST_CODE = 1;

    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    private AlarmManager alarmManager;
    private AppDatabase db;
    private GoalRepository goalRepository;
    private Toolbar toolbar;
    private Spinner categorySpinner;
    private Spinner repeatSpinner;
    private Spinner notificationSpinner;
    private EditText nameText;
    private EditText locationText;
    private EditText fromText;
    private EditText toText;
    private EditText timeText;
    private EditText durationText;
    private EditText pictureSrcText;
    private Uri pictureSrc;
    private int currentOpenedDatePicker;

    private SharedPreferences sharedPreferences;
    private Goal goal;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        toolbar = findViewById(R.id.toolbar);
        categorySpinner = findViewById(R.id.cg_category);
        repeatSpinner = findViewById(R.id.cg_repeat);
        notificationSpinner = findViewById(R.id.cg_notification);
        nameText = findViewById(R.id.cg_name);
        locationText = findViewById(R.id.cg_location);
        fromText = findViewById(R.id.cg_from);
        toText = findViewById(R.id.cg_to);
        timeText = findViewById(R.id.cg_time);
        durationText = findViewById(R.id.cg_duration);
        pictureSrcText = findViewById(R.id.cg_picture);

        db = AppDatabase.getDatabase(this);
        goalRepository = new GoalRepository(getApplication());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("editMode", false);

        if (isEdit) {
            long goalId = intent.getLongExtra("goalId", 0);
            goal = db.goalDao().findOneById(goalId);
            populateFromObject();
        } else {
            goal = new Goal();
            setDefaultDateTimeValues();
        }

        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_create_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void populateFromObject() {
        nameText.setText(goal.getName());
        locationText.setText(goal.getLocation());
        categorySpinner.setSelection((int) goal.getCategoryId());
        fromText.setText(dateFormat.format(goal.getFrom()));
        toText.setText(dateFormat.format(goal.getTo()));
        timeText.setText(timeFormat.format(goal.getTime()));
        repeatSpinner.setSelection((int) goal.getRepeatId());
        notificationSpinner.setSelection((int) goal.getNotificationId());
        durationText.setText(Long.toString(goal.getDuration()));
        pictureSrcText.setText(goal.getPicture());
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
        timeText.setOnClickListener(this);
        pictureSrcText.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();

        if (view.equals(fromText)) currentOpenedDatePicker = FROM_DATE_PICKER;
        else if (view.equals(toText)) currentOpenedDatePicker = TO_DATE_PICKER;

        if (view.equals(fromText)) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            try {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                if (toText.length() != 0) {
                    Date toDate = dateFormat.parse(toText.getText().toString());
                    datePickerDialog.getDatePicker().setMaxDate(toDate.getTime());
                }
                datePickerDialog.show();
            } catch (ParseException e) {
                Toast.makeText(getBaseContext(), "Invalid date format!", Toast.LENGTH_LONG).show();
            }
        } else if(view.equals(toText)) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            try {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                if (fromText.length() != 0) {
                    Date fromDate = dateFormat.parse(fromText.getText().toString());
                    datePickerDialog.getDatePicker().setMinDate(fromDate.getTime());
                }
                datePickerDialog.show();
            } catch (ParseException e) {
                Toast.makeText(getBaseContext(), "Invalid date format!", Toast.LENGTH_LONG).show();
            }
            datePickerDialog.show();
        } else if (view.equals(timeText)) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        } else if (view.equals(pictureSrcText)) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, FILE_PICKER_OPEN_REQUEST_CODE);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        String formattedDate = dateFormat.format(calendar.getTime());

        switch (currentOpenedDatePicker) {
            case FROM_DATE_PICKER: fromText.setText(formattedDate);break;
            case TO_DATE_PICKER: toText.setText(formattedDate);break;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT);
        String formattedTime = timeFormatter.format(calendar.getTime());
        timeText.setText(formattedTime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_PICKER_OPEN_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();

            if (uri ==  null)
                return;

            try(Cursor cursor = getContentResolver().query(uri, null, null, null, null, null)) {
                if (cursor != null && cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    pictureSrc = uri;
                    pictureSrcText.setText(displayName);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cg_action_save: validateForm();break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void validateForm() {
        if (ValidationUtils.isFieldBlank(nameText)) ValidationUtils.setBlankFieldError(nameText);
        else ValidationUtils.resetError(nameText);

        if (ValidationUtils.isFieldBlank(locationText)) ValidationUtils.setBlankFieldError(locationText);
        else ValidationUtils.resetError(locationText);

        if (ValidationUtils.isFieldBlank(durationText)) ValidationUtils.setBlankFieldError(durationText);
        else ValidationUtils.resetError(durationText);

        if (ValidationUtils.isFieldBlank(pictureSrcText)) ValidationUtils.setBlankFieldError(pictureSrcText);
        else ValidationUtils.resetError(pictureSrcText);
    }

    private void setDefaultDateTimeValues() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        fromText.setText(dateFormat.format(new Date()));
        toText.setText(dateFormat.format(new Date()));
        timeText.setText(timeFormat.format(new Date()));
    }

    private void scheduleNotifications() {
        for (GoalNotification goalNotification : db.goalNotificationDao().findAllByGoalId(goal.getId())) {
            Intent intent = new Intent(getApplicationContext(), GoalNotificationBroadcaster.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) goalNotification.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(alarmIntent);
        }

        db.goalNotificationDao().deleteByGoalId(goal.getId());

        for (GregorianCalendar day : goal.generateGoalDays()) {
            GoalNotification goalNotification = new GoalNotification();
            goalNotification.setGoalId(goal.getId());
            goalNotification.setDate(day.getTime());
            long id = db.goalNotificationDao().insert(goalNotification);

            if (sharedPreferences.getBoolean(getString(R.string.sp_enable_notifications), false))  {
                Intent intent = new Intent(getApplicationContext(), GoalNotificationBroadcaster.class);
                intent.putExtra("goalName", goal.getName());
                PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, day.getTimeInMillis(), alarmIntent);
            }
        }
    }

    public void save(MenuItem item) {
        long goalId = goal.getId();
        try {
            goal.setName(nameText.getText().toString());
            goal.setLocation(locationText.getText().toString());
            goal.setCategoryId(categorySpinner.getSelectedItemId());
            goal.setFrom(dateFormat.parse(fromText.getText().toString()));
            goal.setTo(dateFormat.parse(toText.getText().toString()));
            goal.setTime(timeFormat.parse(timeText.getText().toString()));
            goal.setRepeatId(repeatSpinner.getSelectedItemId());
            goal.setNotificationId(notificationSpinner.getSelectedItemId());
            goal.setDuration(Long.parseLong(durationText.getText().toString()));
            goal.setPicture(pictureSrc != null ? pictureSrc.toString() : pictureSrcText.getText().toString());

            if (isEdit) goalRepository.update(goal);
            else goalId = db.goalDao().insert(goal);
            goal.setId(goalId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        scheduleNotifications();
        finish();
    }

    public void discard(MenuItem item) {
        finish();
    }
}
