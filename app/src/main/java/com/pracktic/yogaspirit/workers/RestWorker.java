package com.pracktic.yogaspirit.workers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.pracktic.yogaspirit.R;

public class RestWorker  extends Worker {


    public RestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Result doWork() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        String name = "RestNotifier";
        String description = "Не забывайте маленько передохнуть!";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("RestNotifier", name, importance);
        channel.setDescription(description);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "RestNotifier")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("Не забывайте маленько передохнуть!")
                .setSmallIcon(R.drawable.rest);

        notificationManager.createNotificationChannel(channel);


        notificationManager.notify(2, builder.build());


        return Result.success();
    }
}
