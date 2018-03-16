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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Phone unlocked");
        startAffectivaService(context);
    }

    private void startAffectivaService(Context context) {
        Intent serviceIntent = new Intent(context, AffectivaService.class);
        context.startService(serviceIntent);
    }
}
