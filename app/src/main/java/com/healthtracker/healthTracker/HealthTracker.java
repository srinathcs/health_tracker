package com.healthtracker.healthTracker;




import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.healthtracker.app.R;

public class HealthTracker extends AppCompatActivity {
    private AppCompatButton btReminder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tracker);
        btReminder = findViewById(R.id.btWaterReminder);



        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        btReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EventHandler.periodRequest();
            }
        });
    }
}