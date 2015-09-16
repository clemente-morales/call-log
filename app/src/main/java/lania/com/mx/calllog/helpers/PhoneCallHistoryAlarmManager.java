package lania.com.mx.calllog.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.TimeZone;

import lania.com.mx.calllog.receivers.AlarmReceiver;

/**
 * Created by clerks on 9/16/15.
 */
public final class PhoneCallHistoryAlarmManager {
    private static final String TAG = PhoneCallHistoryAlarmManager.class.getSimpleName();

    public static void setAlarm(Context contex) {
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(contex);
        boolean isFirstRun = wmbPreference.getBoolean("isFirstRun", true);
        boolean isSendingInformationEnabled = wmbPreference.getBoolean("isSendingInformationEnabled", false);

        if (isFirstRun && isSendingInformationEnabled) {
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();

            launchAlarm(contex);
        }
    }

    private static void launchAlarm(Context context) {
        Calendar updateTime = Calendar.getInstance();
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(context);
        int hourOfDay = wmbPreference.getInt("hourToSendPhoneCallHistory", 8);
        int minute = wmbPreference.getInt("minuteToSendPhoneCallHistory", 30);


        updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        updateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        updateTime.set(Calendar.MINUTE, minute);

        Intent senderPhoneCallHistory = new Intent(context, AlarmReceiver.class);
        PendingIntent recurringSendingInformation = PendingIntent.getBroadcast(context,
                0, senderPhoneCallHistory, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringSendingInformation);
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarms = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);
        Intent senderPhoneCallHistory = new Intent(context, AlarmReceiver.class);
        PendingIntent recurringSendingInformation = PendingIntent.getBroadcast(context,
                0, senderPhoneCallHistory, PendingIntent.FLAG_CANCEL_CURRENT);
        alarms.cancel(recurringSendingInformation);
    }
}
