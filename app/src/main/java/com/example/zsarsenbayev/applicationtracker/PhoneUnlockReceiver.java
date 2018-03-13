package com.example.zsarsenbayev.applicationtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_UNLOCKED)){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Phone unlocked");
//        }
    }
}
