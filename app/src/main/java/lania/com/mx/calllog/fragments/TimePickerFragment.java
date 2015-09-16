package lania.com.mx.calllog.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

import lania.com.mx.calllog.helpers.PhoneCallHistoryAlarmManager;

/**
 * Created by clerks on 9/16/15.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = TimePickerFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("hourToSendPhoneCallHistory", hourOfDay);
        editor.putInt("minuteToSendPhoneCallHistory", hourOfDay);
        editor.commit();
        PhoneCallHistoryAlarmManager.setAlarm(getActivity());
        Log.d(TAG, String.format("Hour %d and Time %d set to send information", hourOfDay, minute));
    }
}
