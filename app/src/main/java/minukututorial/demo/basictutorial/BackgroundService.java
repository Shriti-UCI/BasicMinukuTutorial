package minukututorial.demo.basictutorial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import edu.umich.si.inteco.minuku.config.Constants;
import edu.umich.si.inteco.minuku.manager.MinukuStreamManager;

/**
 * Created by shriti on 6/12/17.
 * This service runs continuously to ensure
 * the streams are updated according to their
 * update frequency
 * More on android service: https://developer.android.com/guide/components/services.html
 */

public class BackgroundService extends Service {
    MinukuStreamManager streamManager;
    private static final String TAG = "BackgroundService";

    public BackgroundService() {
        super();
        streamManager = MinukuStreamManager.getInstance();
        Log.d(TAG, "Started background service.");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start command called.");
        streamManager.updateStreamGenerators();
        //sets an alarm that goes off every one second
        //onStartCommand is called whenever the alarm goes off, which means update stream is called every one second
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + Constants.PROMPT_SERVICE_REPEAT_MILLISECONDS,
                PendingIntent.getService(this, 0, new Intent(this, BackgroundService.class), 0)
        );
        return START_STICKY_COMPATIBILITY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
