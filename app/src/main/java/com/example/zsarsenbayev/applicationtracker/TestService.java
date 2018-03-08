package com.example.zsarsenbayev.applicationtracker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Applications_Provider;

public class TestService extends Service {

    public static final String TAG = "Zhanna";

    public TestService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
//        if(Methods.isAccessibilityEnabled(getApplicationContext())
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent aware = new Intent(this, Aware.class);
        startService(aware);

        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_APPLICATIONS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_APPLICATIONS, 20000);
        Aware.startSensor(getApplicationContext(), Aware_Preferences.STATUS_APPLICATIONS);

        Log.d(TAG, "Service started");
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Aware.setSetting(this, Aware_Preferences.STATUS_APPLICATIONS, false);
        super.onDestroy();
    }
}
