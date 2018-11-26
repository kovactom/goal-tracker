package com.vsb.tamz.goaltracker;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

public class ApplicationPreferencesFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    private CheckBoxPreference notifications;
    private Preference sendFeedback;
    private Preference sendAnonymousStats;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.application_preferences, s);

        notifications = (CheckBoxPreference) findPreference("notifications");
        sendFeedback = findPreference("feedback");
        sendAnonymousStats = findPreference("statistics");

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

        }
        return true;
    }
}
