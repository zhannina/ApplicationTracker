package com.example.zsarsenbayev.applicationtracker;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by zsarsenbayev on 6/1/18.
 */

public class Util {


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(60 * 60 * 1000); // wait at least
        builder.setOverrideDeadline(60 * 80 * 1000); // maximum delay
//        builder.setPeriodic(/*60 * **/30 * 1000);
        builder.setPersisted(true); // keeps the job alive if the device is being rebooted

        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
        Log.d("SCHEDULE", "job scheduled");
    }

}
