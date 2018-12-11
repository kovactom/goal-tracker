package com.vsb.tamz.goaltracker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vsb.tamz.goaltracker.persistence.AppDatabase;
import com.vsb.tamz.goaltracker.persistence.model.Goal;
import com.vsb.tamz.goaltracker.persistence.model.GoalNotification;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplicationPreferencesFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, OnSuccessListener<Location> {

    private static final String URL_ANONYMOUS_STATISTICS = "http://192.168.49.96:6080/statistics";
    private AppDatabase db;
    private RequestQueue queue;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SharedPreferences sharedPreferences;
    private AlarmManager alarmManager;

    private CheckBoxPreference notifications;
    private Preference sendFeedback;
    private Preference sendAnonymousStats;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.application_preferences, s);

        notifications = (CheckBoxPreference) findPreference("notifications");
        sendFeedback = findPreference("feedback");
        sendAnonymousStats = findPreference("statistics");

        db = AppDatabase.getDatabase(getContext());
        queue = Volley.newRequestQueue(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        notifications.setOnPreferenceClickListener(this);
        sendFeedback.setOnPreferenceClickListener(this);
        sendAnonymousStats.setOnPreferenceClickListener(this);

        loadSharedPreferences();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (preference.equals(notifications)) {
            boolean checked = notifications.isChecked();
            if (checked) enableAllNotifications();
            else disableAllNotifications();
            editor.putBoolean(getString(R.string.sp_enable_notifications), checked);
        } else if (preference.equals(sendFeedback)) {

        } else if (preference.equals(sendAnonymousStats)) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), this);
            }
        }

        editor.commit();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), this);
        } else {
            sendAnonymousStatistics(null);
        }
    }

    private void loadSharedPreferences() {
        boolean enableNotifications = sharedPreferences.getBoolean(getString(R.string.sp_enable_notifications), false);
        notifications.setChecked(enableNotifications);
    }

    private void sendFeedback() {
        String message;

    }

    private void sendAnonymousStatistics(Location location) {
        String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        long totalGoalCount = db.goalDao().getGoalCount();
        long totalGoalProgress = db.goalProgressDao().getGoalProgressCount();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("totalGoalCount", totalGoalCount);
            jsonObject.put("totalGoalProgressCount", totalGoalProgress);

            if (location != null) {
                jsonObject.put("locationLatitude", location.getLatitude());
                jsonObject.put("locationLongitude", location.getLongitude());
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    URL_ANONYMOUS_STATISTICS, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getContext().getApplicationContext(), "Statistics has been successfully sent.", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext().getApplicationContext(), "Error occurred during sending statistics." + error, Toast.LENGTH_LONG).show();
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Location location) {
        sendAnonymousStatistics(location);
    }

    private void enableAllNotifications() {
        for (GoalNotification goalNotification : db.goalNotificationDao().findAll()) {
            Goal goal = db.goalDao().findOneById(goalNotification.getGoalId());
            Intent intent = new Intent(getContext(), GoalNotificationBroadcaster.class);
            intent.putExtra("goalName", goal.getName());
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), (int) goalNotification.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP, goalNotification.getDate().getTime(), alarmIntent);
        }
    }

    private void disableAllNotifications() {
        for (GoalNotification goalNotification : db.goalNotificationDao().findAll()) {
            Intent intent = new Intent(getContext(), GoalNotificationBroadcaster.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), (int) goalNotification.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(alarmIntent);
        }
    }
}
