package com.healthtracker.home;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.healthtracker.app.R;
import com.healthtracker.data.dataBase.HealthTrackerDataBase;
import com.healthtracker.data.pref.PrefConstant;
import com.healthtracker.data.pref.PrefUtil;
import com.healthtracker.home.entity.WaterCountEntity;
import com.healthtracker.home.work.WaterReminder;
import com.healthtracker.utils.AutoStartHelper;

import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    private static final int REMINDER_INTERVAL = 60;
    private AppCompatButton btReminder;
    private HealthTrackerDataBase dataBase;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("TAG", "onNewIntent: "+intent.getIntExtra(WaterReminder.NOTIFICATION_ID,-1) );
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(intent.getIntExtra(WaterReminder.NOTIFICATION_ID,-1));

        WaterCountEntity mWaterCountEntity = new WaterCountEntity();
        mWaterCountEntity.setCount(1);
        mWaterCountEntity.setDateTime(System.currentTimeMillis());

        dataBase.waterCountDao().insert(mWaterCountEntity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tracker);
        dataBase = HealthTrackerDataBase.getInstance(this);
        initView();
        initWidget();
        checkAutoStartPermission();

    }

    private void initView() {
        btReminder = findViewById(R.id.btWaterReminder);
    }

    private void initWidget() {
        btReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPeriodicWorker();
            }
        });
    }

    private void onPeriodicWorker() {
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(WaterReminder.class, REMINDER_INTERVAL, TimeUnit.MINUTES)
                .addTag("periodic")
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("test", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    private void checkAutoStartPermission() {
        if (!PrefUtil.getBooleanPreference(this, PrefConstant.PREF_KEY_APP_AUTO_START, false)) {
            AutoStartHelper.getInstance().getAutoStartPermission(this);
        }
    }
}