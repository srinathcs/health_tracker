package com.healthtracker.testing;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.healthtracker.app.R;

import java.util.concurrent.TimeUnit;

public class Testing extends AppCompatActivity {
    private static final int REMINDER_INTERVAL = 15;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);
        onPeriodicWorker();
    }

    private void onPeriodicWorker() {
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SampleWorker.class, REMINDER_INTERVAL, TimeUnit.MINUTES)
                .addTag("periodic")
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("test", ExistingPeriodicWorkPolicy.REPLACE,periodicWorkRequest);
    }
}