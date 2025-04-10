package com.pracktic.yogaspirit.job;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pracktic.yogaspirit.R;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class JobDairy extends JobService {
    public static final int JOB_ID = 101;



    private  static final String CHANNEL_ID = "DairyNotifier";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("job", "onStartJob: jobStarted");

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
                .setContentTitle("Не забывайте ввести дневник!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = "Dairy";
        String description = "Channel for dairy notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
