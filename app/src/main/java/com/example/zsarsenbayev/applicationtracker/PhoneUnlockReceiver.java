package com.example.zsarsenbayev.applicationtracker;

import android.app.ActivityManager;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.aware.Aware_Preferences;
import com.aware.Screen;

import java.util.List;

/**
 * Created by zsarsenbayev on 3/12/18.
 */

public class PhoneUnlockReceiver extends BroadcastReceiver {

    public static String message = "Phone unlocked";
    public static final String TAG = "Receiver";
//    public boolean isMainActivityRunning = true;
//    private int pid = 0;
//    private CountDownTimer timer = null;

    @Override
    public void onReceive(final Context context, Intent intent) {

//        int receivedPID = intent.getIntExtra("PID", 0);
//        if(pid == 0) {
//            pid = receivedPID;
//        }
//        Log.d("PID received ", pid+"");
//
//        if(timer == null) {
//            timer = new CountDownTimer(10000, 1000){
//
//                @Override
//                public void onTick(long millisUntilFinished) {
//
//                }
//
//                @Override
//                public void onFinish() {
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
//
//                }
//            };
//            timer.start();
//            Log.d(TAG, "start timer");
//        }


//        if(receivedPID == 0) {
            if (intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_UNLOCKED)) {
                startAffectivaService(context);
                Log.d(TAG, "Screen unlocked");
            }
            if (intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_LOCKED) || intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_OFF)) {
                stopAffectivaService(context);
                Log.d(TAG, "Screen locked");
            }

    }


    private void startAffectivaService(Context context) {
        Intent serviceIntent = new Intent(context, AffectivaService.class);
        context.startService(serviceIntent);
    }

    private void stopAffectivaService(Context context){
        Intent intent = new Intent(context, AffectivaService.class);
        context.stopService(intent);
    }

}
