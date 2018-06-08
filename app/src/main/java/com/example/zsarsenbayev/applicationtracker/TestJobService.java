package com.example.zsarsenbayev.applicationtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class TestJobService extends JobService {

//    private static final String TAG = "SyncService";

    public TestJobService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), EsmService.class);
        Intent testService = new Intent(getApplicationContext(), TestService.class);
        getApplicationContext().startService(service);
        getApplicationContext().startService(testService);
        Util.scheduleJob(getApplicationContext()); // reschedule the job
        //createNotification(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }






}
