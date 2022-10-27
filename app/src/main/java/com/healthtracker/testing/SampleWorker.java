package com.healthtracker.testing;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;
import static androidx.core.app.NotificationCompat.DEFAULT_ALL;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.healthtracker.app.R;

import java.util.Calendar;

public class SampleWorker extends Worker {

    private static final String NOTIFICATION_ID = "appName_notification_id";
    private static final String NOTIFICATION_NAME = "appName";
    private static final String NOTIFICATION_CHANNEL = "appName_channel_01";

    private Context mContext;

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 7 && timeOfDay < 19) {
            sendNotification(0);
        }
        return Result.success();
    }

    private void sendNotification(int id) {
        Intent intent = new Intent(getApplicationContext(), Testing.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, id);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Title")
                .setContentText("Time to Drink")
                .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true);

        notification.setPriority(NotificationCompat.PRIORITY_MAX);

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL);
            NotificationChannel channel =
                    new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notification.build());
    }
}