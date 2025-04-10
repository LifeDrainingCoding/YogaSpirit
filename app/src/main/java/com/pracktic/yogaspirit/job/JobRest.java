package com.pracktic.yogaspirit.job;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pracktic.yogaspirit.R;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class JobRest extends JobService {

    public static final int JOB_ID = 100;


    private  static final String CHANNEL_ID = "RestNotifier";

    @Override
    public boolean onStartJob(JobParameters params) {

        sendNotification();
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return true;
    }

    @SuppressLint({"SpecifyJobSchedulerIdRange", "MissingPermission"})
    private void sendNotification() {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.rest)
                .setContentTitle("Не забывайте маленько передохнуть!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2, builder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = "RestNotifier";
        String description = "Channel for rest notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
