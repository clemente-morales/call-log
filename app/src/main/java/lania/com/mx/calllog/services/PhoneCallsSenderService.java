package lania.com.mx.calllog.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import lania.com.mx.calllog.R;
import lania.com.mx.calllog.helpers.PhoneCallHistoryAlarmManager;
import lania.com.mx.calllog.models.Email;
import lania.com.mx.calllog.receivers.AlarmReceiver;

/**
 * Created by clerks on 9/16/15.
 */
public class PhoneCallsSenderService extends IntentService {
    private static final String TAG = PhoneCallHistoryAlarmManager.class.getSimpleName();

    public PhoneCallsSenderService() {
        super("PhoneCallsSenderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String emailKey = getString(R.string.phoneCallsHistorySettings_emailToSendPhoneCallHistoryKey);
        String email = sharedPreferences.getString(emailKey, "defaultEmail");
        Log.d(TAG, "Sending phone call history to " + email);
        boolean messageSent = sendEmail(email);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.stat_notify_sync)
                        .setContentTitle("Phone call history");

        if (messageSent)
            mBuilder.setContentText(String.format("Phone call history send to %s!", email));
        else
            mBuilder.setContentText(String.format("Could not send Phone call history to %s!", email));


        int mNotificationId = 0;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotifyMgr.notify(mNotificationId, notification);

        Log.d(TAG, "phone call history sent to " + email);

        // Release the wake lock provided by the AlarmReceiver.
        AlarmReceiver.completeWakefulIntent(intent);
    }

    private boolean sendEmail(String email) {
        boolean messageSent = false;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String emailFromKey = getString(R.string.phoneCallsHistorySettings_emailFromKey);
        String fromEmail = sharedPreferences.getString(emailFromKey, "morales.fernandez.clemente@gmail.com");
        String passwordFromKey = getString(R.string.phoneCallsHistorySettings_emailFromPasswordKey);
        String passwordFromEmail = sharedPreferences.getString(passwordFromKey , "DefaultPassword");

        Log.d(TAG, String.format("From %s Password %s",fromEmail, passwordFromEmail));

        Email m = new Email(fromEmail, passwordFromEmail);

        String[] toArr = {email, "morales.clements@gmail.com"};
        m.setTo(toArr);
        m.setFrom(fromEmail);
        m.setSubject("Phone call history.");
        m.setBody("The phone call history is attached in the email.");

        try {
            m.addAttachment("/sdcard/phoneCalls.txt");

            messageSent = m.send();

        } catch (Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
        }

        return messageSent;
    }
}
