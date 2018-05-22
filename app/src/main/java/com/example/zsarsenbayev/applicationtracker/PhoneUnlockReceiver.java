package com.example.zsarsenbayev.applicationtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.aware.Aware_Preferences;
import com.aware.Screen;

/**
 * Created by zsarsenbayev on 3/12/18.
 */

public class PhoneUnlockReceiver extends BroadcastReceiver {

    public static String message = "Phone unlocked";
    public static final String TAG = "Receiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_UNLOCKED)) {
            startAffectivaService(context);
        } else if (intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_LOCKED) || intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_OFF)){
            stopAffectivaService(context);
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
