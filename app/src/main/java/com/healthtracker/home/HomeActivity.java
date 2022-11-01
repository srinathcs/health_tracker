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
import androidx.room.Query;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    private static final int REMINDER_INTERVAL = 60;
    private AppCompatButton btReminder;
    private HealthTrackerDataBase dataBase;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("TAG", "onNewIntent: " + intent.getIntExtra(WaterReminder.NOTIFICATION_ID, -1));
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(intent.getIntExtra(WaterReminder.NOTIFICATION_ID, -1));

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
                getAllData();
                getTimeData();
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

    private void getAllData() {
        long fixedMorningTime = getFixedMorningTime();
        long fixedEveningTime = getFixedEveningTime();
        long currentTime = System.currentTimeMillis();
        List<WaterCountEntity> entities = dataBase.waterCountDao().getAll();
        Log.e("TAG", "getAllData: " + entities.size());

        if (fixedMorningTime < currentTime && fixedEveningTime > currentTime) {
            Log.e("TAG", "getAllData: mrng or evnig");

        }
    }

    private void getTimeData() {
        Calendar fixCalenderTime = Calendar.getInstance();
        fixCalenderTime.set(Calendar.HOUR_OF_DAY, 19);
        fixCalenderTime.set(Calendar.MINUTE, 0);
        fixCalenderTime.set(Calendar.SECOND, 0);
        long fixedTime = fixCalenderTime.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        List<WaterCountEntity> entities = dataBase.waterCountDao().getAll();
        Log.e("TAG", "testCal: " + entities.size());
        Log.e("TAG", "testCal: " + fixedTime);
        Log.e("TAG", "testCal: " + fixCalenderTime);
    }

    private long getFixedMorningTime() {
        Calendar fixedCalender = Calendar.getInstance();
        fixedCalender.set(Calendar.HOUR_OF_DAY, 7);
        fixedCalender.set(Calendar.MINUTE, 0);
        fixedCalender.set(Calendar.SECOND, 0);
        return fixedCalender.getTimeInMillis();
    }

    private long getFixedEveningTime() {
        Calendar fixedEveningCalenderTime = Calendar.getInstance();
        fixedEveningCalenderTime.set(Calendar.HOUR_OF_DAY, 19);
        fixedEveningCalenderTime.set(Calendar.MINUTE, 0);
        fixedEveningCalenderTime.set(Calendar.SECOND, 0);
        return fixedEveningCalenderTime.getTimeInMillis();

    }
}