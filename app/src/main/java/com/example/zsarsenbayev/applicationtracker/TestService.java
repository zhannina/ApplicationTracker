package com.example.zsarsenbayev.applicationtracker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Screen;

import java.util.Calendar;

import static com.example.zsarsenbayev.applicationtracker.MainActivity.CHANNEL_ID;

public class TestService extends Service {

    public static final String TAG = "APPS";
    private PhoneUnlockReceiver phoneUnlockReceiver;
    private Util util;
    public final String mClassName = "Test Service";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public final int NOTIFY_ID = 111;
    public final int FOREGROUND_ID = 3141;

    public TestService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        phoneUnlockReceiver = new PhoneUnlockReceiver();

//        Log.d(TAG, "" + calendar.get(Calendar.HOUR_OF_DAY));

        util = new Util();
        startJobScheduler();

    }


    // starts the ESM timer
    // should put job scheduler here
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startJobScheduler() {
        util.scheduleJob(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//
////        raiseNotification(intent, null);
//        Log.d(TAG, "insideonHandle");
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("HELLO1", " "+intent);

        Intent aware = new Intent(this, Aware.class);
        //startJobScheduler();
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Application tracker")
                    .setSmallIcon(R.drawable.notif_icon)
                    .setContentText("is running")
                    .build();
            startForeground(FOREGROUND_ID, notification);
            Log.d("API", "higher");
        } else {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Application tracker")
                    .setSmallIcon(R.drawable.notif_icon)
                    .setContentText("is running")
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
//            NotificationManager manager = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                manager = getSystemService(NotificationManager.class);
//            }
//            manager.createNotificationChannel(notificationChannel);
            Log.d("API", "lower");
            startForeground(FOREGROUND_ID, notification);
        }



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (phoneUnlockReceiver != null) {
            unregisterReceiver(phoneUnlockReceiver);
        }
    }


//    private void createNotificationChannel(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    CHANNEL_ID, "Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(notificationChannel);
//
//        }
//    }

//    private void raiseNotification(Intent inbound,
//                                   Exception e) {
//        Intent intent = new Intent(this, TestService.class);
//        //startTestService();
//        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        Context context = getApplicationContext();
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationChannel notifChannel = null;
//        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notifChannel = new NotificationChannel("testServiceNotification", "testServiceChannel",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notifChannel.enableVibration(true);
//            notifChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            manager.createNotificationChannel(notifChannel);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "testServiceNotification")
//                    .setContentTitle("Test service running")
//                    .setContentText("Test service")
//                    .setSmallIcon(R.drawable.notif_icon)
//                    .setOngoing(true)
//                    .setAutoCancel(true);
//
//            notificationBuilder.setContentIntent(pendingIntent).build();
////            notification.flags |= Notification.FLAG_AUTO_CANCEL;
////            notification.flags |= Notification.FLAG_AUTO_CANCEL;
////            notification.flags |= Notification.FLAG_ONGOING_EVENT;
//            Notification builtNotification = notificationBuilder.build();
////            manager.notify(NOTIFY_ID, builtNotification);
////            testServiceRef.startForeground(623, builtNotification);
////            startForeground(FOREGROUND_ID, builtNotification);
//        } else {
//            Notification notification = new Notification.Builder(context)
//                    .setContentTitle("Test service running")
//                    .setSmallIcon(R.drawable.notif_icon)
//                    .setContentIntent(pendingIntent).build();
//
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//            notification.flags |= Notification.FLAG_ONGOING_EVENT;
////            manager.notify(NOTIFY_ID, notification);
////            testServiceRef.startForeground(123, notification);
////            startForeground(FOREGROUND_ID, notification);
//        }
//    }
//
//    private Notification buildForegroundNotification() {
//        NotificationCompat.Builder b = new NotificationCompat.Builder(this, "testServiceNotification");
//
//        b.setOngoing(true)
//                .setContentTitle("TestServiceForeground")
//                .setContentText("TestServiceForeground")
//                .setSmallIcon(R.drawable.notif_icon);
//
//        return(b.build());
//    }


}



//    public void createNotification(Context context) {
//
//        intent = new Intent(context, TestService.class);
//        startService(intent);
//        //startTestService();
//        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
//
//        NotificationChannel notifChannel = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notifChannel = new NotificationChannel("testServiceNotification", "testServiceChannel",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notifChannel.enableVibration(true);
//            notifChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//            manager.createNotificationChannel(notifChannel);
//
//            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "testServiceNotification")
//                    .setContentTitle("Test service running")
//                    .setContentText("Test service")
//                    .setSmallIcon(R.drawable.notif_icon)
//                    .setOngoing(true)
//                    .setAutoCancel(true);
//
//            notification.setContentIntent(pendingIntent).build();
////        notification.flags |= Notification.FLAG_AUTO_CANCEL;
////            notification.flags |= Notification.FLAG_AUTO_CANCEL;
////            notification.flags |= Notification.FLAG_ONGOING_EVENT;
//            Notification builtNotification = notification.build();
//            manager.notify(623, builtNotification);
//            testServiceRef.startForeground(623, builtNotification);
//        } else {
//
//            Notification notification = new Notification.Builder(context)
//                    .setContentTitle("Test service running")
//                    .setSmallIcon(R.drawable.notif_icon)
//                    .setContentIntent(pendingIntent).build();
//            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//            notification.flags |= Notification.FLAG_ONGOING_EVENT;
//            manager.notify(123, notification);
//            testServiceRef.startForeground(123, notification);
//        }
//    }


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