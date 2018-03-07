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

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Applications_Provider;

public class TestService extends Service {

    public static final String TAG = "Zhanna";
    public String foreground_package;
    public ContextReceiver contextReceiver;
    public MediaPlayer player;

    public TestService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "service started");
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        player.start();

        Intent aware = new Intent(this, Aware.class);
        startService(aware);

        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_APPLICATIONS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_APPLICATIONS, 20000);
        Aware.startSensor(getApplicationContext(), Aware_Preferences.STATUS_APPLICATIONS);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Applications.ACTION_AWARE_APPLICATIONS_FOREGROUND);
        registerReceiver(contextReceiver, filter);

        return START_STICKY;
    }

    public class ContextReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Applications.ACTION_AWARE_APPLICATIONS_FOREGROUND)){
                Cursor cursorApp = context.getContentResolver().query(Applications_Provider.
                                Applications_Foreground.CONTENT_URI, null, null, null,
                        Applications_Provider.Applications_Foreground.TIMESTAMP + " DESC LIMIT 1");
                Log.d(TAG, "inside first if");
                if (cursorApp != null && cursorApp.moveToFirst()) {
                    Log.d(TAG, "inside second if");
                    foreground_package = cursorApp.getString(cursorApp.getColumnIndex(Applications_Provider.Applications_Foreground.PACKAGE_NAME));
                    PackageManager cjj = getPackageManager();
                    Intent LaunchIntent = cjj.getLaunchIntentForPackage(foreground_package);
                    if (foreground_package.equals("com.example.zsarsenbayev.applicationtracker")) return;
                    else if (LaunchIntent == null) return;
                    else {
                        Log.d(TAG, "inside else");
                        Log.d(TAG, foreground_package);
                    }
                }
                if (cursorApp != null && !cursorApp.isClosed()){
                    cursorApp.close();
                }
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Aware.setSetting(this, Aware_Preferences.STATUS_APPLICATIONS, false);
        if(contextReceiver != null) {
            unregisterReceiver(contextReceiver);
        }

        super.onDestroy();
//        kill the service
        player.stop();
    }
}
