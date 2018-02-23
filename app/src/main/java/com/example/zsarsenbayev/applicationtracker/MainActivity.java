package com.example.zsarsenbayev.applicationtracker;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Applications_Provider;
import com.aware.utils.Aware_Sensor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AWARE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent aware = new Intent(this, Aware.class);
        startService(aware);

        Aware.setSetting(this, Aware_Preferences.STATUS_APPLICATIONS, true);

        Aware.setSetting(this, Aware_Preferences.FREQUENCY_APPLICATIONS, 20000);

        Aware.startSensor(this, Aware_Preferences.STATUS_APPLICATIONS);

        Uri applicationsLog = Applications_Provider.Applications_Foreground.CONTENT_URI;



    }
}
