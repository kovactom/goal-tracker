package com.vsb.tamz.goaltracker;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vsb.tamz.goaltracker.persistence.AppDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplicationPreferencesFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    private static final String URL_ANONYMOUS_STATISTICS = "http://192.168.49.96:6080/statistics";
    private AppDatabase db;
    RequestQueue queue;

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

        notifications.setOnPreferenceClickListener(this);
        sendFeedback.setOnPreferenceClickListener(this);
        sendAnonymousStats.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.equals(notifications)) {
            Toast.makeText(getContext(),"Notifications ticked: " + notifications.isChecked(), Toast.LENGTH_LONG).show();
        }
        else if (preference.equals(sendFeedback)) {

        } else if (preference.equals(sendAnonymousStats)) {
            sendAnonymousStatistics();
        }
        return true;
    }

    private void sendFeedback() {
        String message;

    }

    private void sendAnonymousStatistics() {
        String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        long totalGoalCount = db.goalDao().getGoalCount();
        long totalGoalProgress = db.goalProgressDao().getGoalProgressCount();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("totalGoalCount", totalGoalCount);
            jsonObject.put("totalGoalProgressCount", totalGoalProgress);

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
}
