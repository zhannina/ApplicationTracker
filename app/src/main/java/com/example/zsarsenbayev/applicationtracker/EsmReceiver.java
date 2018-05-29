package com.example.zsarsenbayev.applicationtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aware.Screen;

/**
 * Created by zsarsenbayev on 5/24/18.
 */

public class EsmReceiver extends BroadcastReceiver {
    private static final String TAG = "ESMReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_UNLOCKED)) {
            // TODO: launch esm activity
            Log.d(TAG, "Screen unlocked");

            Intent i = new Intent(context.getApplicationContext(), EsmActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
    }
}
