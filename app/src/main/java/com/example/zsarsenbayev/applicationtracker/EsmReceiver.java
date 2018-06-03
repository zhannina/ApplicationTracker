package com.example.zsarsenbayev.applicationtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aware.Screen;

import static android.content.Context.NOTIFICATION_SERVICE;

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

//            Intent i = new Intent(context.getApplicationContext(), EsmActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);
            createNotification(context);
        }
    }

    public void createNotification(Context context){
        Intent intent = new Intent(context, EsmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("Tap to answer questionnaire")
                .setSmallIcon(R.drawable.notif_icon)
                .setContentIntent(pendingIntent).build();
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);
    }
}
