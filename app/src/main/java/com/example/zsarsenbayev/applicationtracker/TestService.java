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
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Screen;

import org.json.JSONException;

import java.util.Calendar;

public class TestService extends Service {

    public static final String TAG = "APPS";
    private PhoneUnlockReceiver phoneUnlockReceiver;
    private CountDownTimer timer = null;
    private Calendar calendar;
    private Util util;

    public TestService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        phoneUnlockReceiver = new PhoneUnlockReceiver();

//        Log.d(TAG, "" + calendar.get(Calendar.HOUR_OF_DAY));

        util = new Util();
        startTimer();

    }


    // starts the ESM timer
    // should put job scheduler here
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startTimer() {
        calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d(TAG, "" + calendar.get(Calendar.HOUR_OF_DAY));
        if (hourOfDay > 10 && hourOfDay < 20) {
            util.scheduleJob(getApplicationContext());
//            if (timer == null) {
//                timer = new CountDownTimer(60*60*1000, 1000) {
//                    //
//
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//
//                    }
//
//                    @RequiresApi(api = Build.VERSION_CODES.M)
//                    @Override
//                    public void onFinish() {
//                        // start ESM Service
////                        Intent esmServiceIntent = new Intent(TestService.this, EsmService.class);
////                        startService(esmServiceIntent);
////                        this.start();
//                        util.scheduleJob(getApplicationContext());
//                    }
//                };
//                timer.start();
//                Log.d(TAG, "start timer");
//            }
        } else {
            Log.d(TAG, "completely dismiss");
        }
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
//        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_APPLICATIONS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_BATTERY, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_NOTIFICATIONS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_WIFI, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_NETWORK_EVENTS, true);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_NETWORK_TRAFFIC, true);
        Aware.startScreen(getApplicationContext());
        Applications.isAccessibilityServiceActive(getApplicationContext());// to start applications sensor
        Applications.setSensorObserver(new Applications.AWARESensorObserver() {
            @Override
            public void onForeground(ContentValues data) {
                Log.d("AAAAAAAAAAA", data.toString());
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

        IntentFilter filterUnlock = new IntentFilter();
        IntentFilter filterLock = new IntentFilter();
        IntentFilter filterScreenOn = new IntentFilter();
        IntentFilter filterScreenOff = new IntentFilter();

        filterUnlock.addAction(Screen.ACTION_AWARE_SCREEN_UNLOCKED);
        filterLock.addAction(Screen.ACTION_AWARE_SCREEN_LOCKED);
        filterScreenOn.addAction(Screen.ACTION_AWARE_SCREEN_ON);
        filterScreenOff.addAction(Screen.ACTION_AWARE_SCREEN_OFF);

//        filter.addAction(Applications.ACTION_AWARE_APPLICATIONS_FOREGROUND);


        registerReceiver(phoneUnlockReceiver, filterUnlock);
        registerReceiver(phoneUnlockReceiver, filterLock);
        registerReceiver(phoneUnlockReceiver, filterScreenOn);
        registerReceiver(phoneUnlockReceiver, filterScreenOff);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (phoneUnlockReceiver != null) {
            unregisterReceiver(phoneUnlockReceiver);
        }
    }

}


//                    if(pid == 0) {
//                        this.start();
//                        return;
//                    }
//
//                    ActivityManager activityManager = (ActivityManager) context
//                            .getSystemService(Context.ACTIVITY_SERVICE);
//                    List<ActivityManager.RunningAppProcessInfo> activities = activityManager.getRunningAppProcesses();
//                    isMainActivityRunning = false;
//                    for(ActivityManager.RunningAppProcessInfo activity:activities) {
//                        Log.d(TAG, "inside timer:" + activity.pid);
//                        if (activity.pid == pid) {
//                            isMainActivityRunning = true;
//                            break;
//                        }
//                    }
//
//                    if(!isMainActivityRunning) {
//                        // stopAffectivaService(context);
//                        Log.d(TAG, "stopping service");
//                    }
//                    else {
//                        Log.d(TAG, "restart timer");
//
//                        this.start();
//                    }

// dismissed esm example
//        private void launchESM2(){
//            try {
//                ESMFactory factory = new ESMFactory();
//
//                //define ESM question
//                ESM_Radio esmRadio = new ESM_Radio();
//                esmRadio.addRadio("Sad")
//                        .addRadio("Suprised")
//                        .addRadio("Contempt")
//                        .addRadio("Disgusted")
//                        .addRadio("Fearful")
//                        .addRadio("Joyful")
//                        .setTitle("How are you feeling right now?")
//                        .setInstructions("Please select one option")
//                        .setSubmitButton("OK");
//
//                //add them to the factory
//                factory.addESM(esmRadio);
//
//                Intent queue = new Intent(ESM.ACTION_AWARE_QUEUE_ESM);
//                queue.putExtra(ESM.EXTRA_ESM, factory.build());
//                context.sendBroadcast(queue);
//
//                /// What happens when user interacts with ESM
//                IntentFilter filter = new IntentFilter();
//                filter.addAction(ESM.ACTION_AWARE_ESM_ANSWERED); // USER ANSWERS THE ESM
//                filter.addAction(ESM.ACTION_AWARE_ESM_DISMISSED);// User dismisses the ESM
//
//                context.registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        if (intent.getAction() == ESM.ACTION_AWARE_ESM_ANSWERED) {
//                            // TAKE ACTION
//                                try {
//                                    Log.d(TAG, ""+intent.getStringExtra(ESM.EXTRA_ANSWER));
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                        }
//                        if (intent.getAction() == ESM.ACTION_AWARE_ESM_DISMISSED) {
//                            Log.d("A", ""+ "ESM dismissed");
//                        }
//                    }
//                }, filter);
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }

//        private void launchESM(){
//            try {
//                ESMFactory factory = new ESMFactory();
//
//        //define ESM question
//                ESM_Likert esmLikert1 = new ESM_Likert();
//                esmLikert1.setLikertMax(5)
//                        .setLikertMaxLabel("Miserable")
//                        .setLikertMinLabel("Pleased")
//                        .setLikertStep(1)
//                        .setTitle("I currently feel")
//                        .setInstructions("")
//                        .setSubmitButton("OK");
//
//        //add them to the factory
//                factory.addESM(esmLikert1);
//
//                ESM_Likert esmLikert2 = new ESM_Likert();
//                esmLikert2.setLikertMax(5)
//                        .setLikertMaxLabel("Sleepy")
//                        .setLikertMinLabel("Aroused")
//                        .setLikertStep(1)
//                        .setTitle("I currently feel")
//                        .setInstructions("")
//                        .setSubmitButton("OK");
//
//
//        //add them to the factory
//                factory.addESM(esmLikert2);
//
//                ESM.queueESM(context, factory.build());
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }