package com.example.zsarsenbayev.applicationtracker;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Screen;

public class EsmService extends Service {

    private EsmReceiver esmReceiver;

    public EsmService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        esmReceiver = new EsmReceiver();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_SCREEN, true);

        IntentFilter filterUnlock = new IntentFilter();
        filterUnlock.addAction(Screen.ACTION_AWARE_SCREEN_UNLOCKED);

        registerReceiver(esmReceiver, filterUnlock);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (esmReceiver != null) {
            unregisterReceiver(esmReceiver );
        }
    }
}
