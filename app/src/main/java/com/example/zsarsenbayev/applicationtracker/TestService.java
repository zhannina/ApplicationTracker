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
import com.aware.ESM;
import com.aware.Screen;
import com.aware.providers.Applications_Provider;
import com.aware.providers.Battery_Provider;
import com.aware.ui.esms.ESMFactory;
import com.aware.ui.esms.ESM_Likert;
import com.aware.ui.esms.ESM_Radio;
import com.aware.utils.Aware_Sensor;
import com.aware.utils.Scheduler;

import org.json.JSONException;

public class TestService extends Service {

    public static final String TAG = "APPS";
    private PhoneUnlockReceiver phoneUnlockReceiver;
    private int pid;
    private Intent pidIntent;

    public TestService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        phoneUnlockReceiver = new PhoneUnlockReceiver();
//        if(Methods.isAccessibilityEnabled(getApplicationContext())
//        pid = android.os.Process.myPid();
//        pidIntent = new Intent(TestService.this, PhoneUnlockReceiver.class);
//        pidIntent.putExtra("PID", pid);

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
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, true);
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

        EmotionESM esm = new EmotionESM(getApplicationContext());
        esm.launchEmotionESM();
//        esm.launchESM();

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

//        sendBroadcast(pidIntent);
//        Log.d("PID sent ", pid+"");

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        if (phoneUnlockReceiver != null) {
            unregisterReceiver(phoneUnlockReceiver);


        }
        super.onDestroy();
//        Aware.setSetting(this, Aware_Preferences.STATUS_APPLICATIONS, false);
        Aware.setSetting(this, Aware_Preferences.STATUS_SCREEN, false);
    }


    public class EmotionESM {
        private Context context;

        public  EmotionESM(Context context) {
            this.context = context;
        }

        private void launchEmotionESM(){
            try {
                ESMFactory factory = new ESMFactory();

                //define ESM question
                ESM_Likert esmLikert1 = new ESM_Likert();
                esmLikert1.setLikertMax(5)
                        .setLikertMaxLabel("Miserable")
                        .setLikertMinLabel("Pleased")
                        .setLikertStep(1)
                        .setTitle("I currently feel")
                        .setInstructions("")
                        .setSubmitButton("OK");

                ESM_Likert esmLikert2 = new ESM_Likert();
                esmLikert2.setLikertMax(5)
                        .setLikertMaxLabel("Sleepy")
                        .setLikertMinLabel("Aroused")
                        .setLikertStep(1)
                        .setTitle("I currently feel")
                        .setInstructions("")
                        .setSubmitButton("OK");


                //add them to the factory
                factory.addESM(esmLikert1);
                factory.addESM(esmLikert2);

                Scheduler.Schedule scheduler = new Scheduler.Schedule("testRandom");
                scheduler.addHour(10);
                scheduler.addHour(20);
                scheduler.random(6, 60);
                scheduler.addContext(Screen.ACTION_AWARE_SCREEN_ON);

                scheduler.setActionType(Scheduler.ACTION_TYPE_BROADCAST)
                        .setActionIntentAction(ESM.ACTION_AWARE_QUEUE_ESM)
                        .addActionExtra(ESM.EXTRA_ESM, factory.build());

                Scheduler.saveSchedule(context, scheduler);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void launchESM(){
            try {
                ESMFactory factory = new ESMFactory();

                //define ESM question
                ESM_Likert esmLikert1 = new ESM_Likert();
                esmLikert1.setLikertMax(5)
                        .setLikertMaxLabel("Miserable")
                        .setLikertMinLabel("Pleased")
                        .setLikertStep(1)
                        .setTitle("I currently feel")
                        .setInstructions("")
                        .setSubmitButton("OK");


                ESM_Likert esmLikert2 = new ESM_Likert();
                esmLikert2.setLikertMax(5)
                        .setLikertMaxLabel("Sleepy")
                        .setLikertMinLabel("Aroused")
                        .setLikertStep(1)
                        .setTitle("I currently feel")
                        .setInstructions("")
                        .setSubmitButton("OK");


                //add them to the factory
                factory.addESM(esmLikert1);
                factory.addESM(esmLikert2);

                ESM.queueESM(context, factory.build());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }



}






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