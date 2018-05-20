package com.example.zsarsenbayev.applicationtracker;

import android.*;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Applications_Provider;
import com.aware.utils.Aware_Sensor;
import com.backendless.Backendless;

public class MainActivity extends AppCompatActivity {

    private Button serviceBtn;
    public static final String TAG = "Zhanna";
    public static String message = "Please enable accessibility service";
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    public final static int REQUEST_CODE = 200;
    Intent serviceIntent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDrawOverlayPermission();
        checkPermissions();
        if (android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        serviceBtn = findViewById(R.id.serviceBtn);
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTestService();
            }
        });
        serviceIntent = new Intent(this, TestService.class);
        
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermissions() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void startTestService() {
        Intent serviceIntent = new Intent(this, TestService.class);
        Log.d(TAG,serviceIntent+"");
        Log.d(TAG, TestService.class.getName());
        startService(serviceIntent);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        stopService(serviceIntent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            /** if so check once again if we have permission */
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
            }
        }
    }
}
