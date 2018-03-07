package com.example.zsarsenbayev.applicationtracker;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Applications_Provider;
import com.aware.utils.Aware_Sensor;

public class MainActivity extends AppCompatActivity {

    private Button serviceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceBtn = findViewById(R.id.serviceBtn);

        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTestService();
            }
        });
    }

    private void startTestService() {
        Intent serviceIntent = new Intent(this, TestService.class);
        startService(serviceIntent);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
