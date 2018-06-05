package com.example.zsarsenbayev.applicationtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aware.Screen;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by zsarsenbayev on 5/24/18.
 */

public class EsmReceiver extends BroadcastReceiver {
    private static final String TAG = "ESMReceiver";
    private Calendar calendar;


    @Override
    public void onReceive(Context context, Intent intent) {
        calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (intent.getAction().equals(Screen.ACTION_AWARE_SCREEN_UNLOCKED)) {
            // TODO: launch esm activity
            Log.d(TAG, "Screen unlocked");

//            Intent i = new Intent(context.getApplicationContext(), EsmActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);
            Log.d(TAG, "" + calendar.get(Calendar.HOUR_OF_DAY));
            if (hourOfDay > 10 && hourOfDay < 20) {
                createNotification(context);
            } else {
                Log.d(TAG, "completely dismiss");
            }

        }
    }

    public void createNotification(Context context){

        Intent intent = new Intent(context, EsmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationChannel notifChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notifChannel = new NotificationChannel("questionnaireNotificiation", "myChannel",
                        NotificationManager.IMPORTANCE_DEFAULT);
            notifChannel.enableVibration(true);
            notifChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notifChannel);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "questionnaireNotificiation")
                    .setContentTitle("Tap to answer questionnaire")
                    .setSmallIcon(R.drawable.notif_icon)
                    .setAutoCancel(true);

            notification.setContentIntent(pendingIntent).build();
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(1, notification.build());
        } else {

            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Tap to answer questionnaire")
                    .setSmallIcon(R.drawable.notif_icon)
                    .setContentIntent(pendingIntent).build();
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(0, notification);
        }

    }
}
