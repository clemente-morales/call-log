package lania.com.mx.calllog.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import lania.com.mx.calllog.services.PhoneCallsSenderService;

/**
 * Created by clerks on 9/16/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm fire for Sending phone call history to email.");
        // Start the service, keeping the device awake while the service is
        // launching. This is the Intent to deliver to the service.
        Intent downloader = new Intent(context, PhoneCallsSenderService.class);
        startWakefulService(context, downloader);
    }
}
