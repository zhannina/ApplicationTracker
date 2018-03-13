package com.example.zsarsenbayev.applicationtracker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
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
import com.aware.Screen;
import com.aware.providers.Applications_Provider;
import com.aware.utils.Aware_Sensor;

public class TestService extends Service {

    public static final String TAG = "Zhanna";
    private PhoneUnlockReceiver phoneUnlockReceiver;

    public TestService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        phoneUnlockReceiver = new PhoneUnlockReceiver();
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

        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_SCREEN, true);

        Aware.startScreen(getApplicationContext());
        Applications.isAccessibilityServiceActive(getApplicationContext());// to start applications sensor
        Applications.setSensorObserver(new Applications.AWARESensorObserver() {
            @Override
            public void onForeground(ContentValues data) {
                Log.d(TAG, data.toString());
            }

            @Override
            public void onNotification(ContentValues data) {

            }

            @Override
            public void onCrash(ContentValues data) {

            }

            @Override
            public void onKeyboard(ContentValues data) {

            }

            @Override
            public void onBackground(ContentValues data) {

            }

            @Override
            public void onTouch(ContentValues data) {

            }
        });

        Log.d(TAG, "" + Applications.isAccessibilityServiceActive(getApplicationContext()));
        Log.d(TAG, "" + Applications.getSensorObserver());

        IntentFilter filter = new IntentFilter();
        filter.addAction(Screen.ACTION_AWARE_SCREEN_UNLOCKED);
        registerReceiver(phoneUnlockReceiver, filter);

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Aware.setSetting(this, Aware_Preferences.STATUS_APPLICATIONS, false);
        super.onDestroy();
        unregisterReceiver(phoneUnlockReceiver);
    }
}
