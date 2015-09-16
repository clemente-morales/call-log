package lania.com.mx.calllog.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import lania.com.mx.calllog.helpers.PhoneCallHistoryAlarmManager;
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
        Log.d(TAG, "Sending phone call history to Joshua");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_popup_sync)
                        .setContentTitle("Phone call history")
                        .setContentText("Phone call history send to Joshua!");

        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        // Release the wake lock provided by the AlarmReceiver.
        AlarmReceiver.completeWakefulIntent(intent);
    }
}
