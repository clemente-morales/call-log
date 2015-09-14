package lania.com.mx.calllog;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by clerks on 9/14/15.
 */
public class CallLogApplication extends Application {
    private static CallLogApplication context;
    private static Bus eventBus;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        eventBus = new Bus(ThreadEnforcer.ANY);
    }

    public static Bus getEventBus() {
        return eventBus;
    }
}
