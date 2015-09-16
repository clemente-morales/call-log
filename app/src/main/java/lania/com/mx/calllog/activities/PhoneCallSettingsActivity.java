package lania.com.mx.calllog.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import lania.com.mx.calllog.R;
import lania.com.mx.calllog.fragments.TimePickerFragment;

/**
 * Created by clerks on 9/16/15.
 */
public class PhoneCallSettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private static final String TAG =  PhoneCallSettingsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferencesEvents();
    }

    /**
     * Attaches listener to the events launched for the controls.
     */
    private void bindPreferencesEvents() {
        Preference btnDateFilter = (Preference) findPreference(
                getString(R.string.phoneCallsHistorySettings_timeToSendPhoneCallHistoryKey));
        btnDateFilter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showTimePickerDialog();
                return false;
            }
        });
    }

    private void showTimePickerDialog() {
        new TimePickerFragment().show(getFragmentManager(), "timePicker");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        return true;
    }

}