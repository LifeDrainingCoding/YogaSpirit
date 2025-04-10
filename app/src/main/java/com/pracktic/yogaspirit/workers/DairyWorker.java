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
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.pracktic.yogaspirit.R;

public class DairyWorker extends Worker {
    public DairyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Result doWork() {


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        String name = "DairyNotifier";
        String description = "Не забывайте ввести дневник!";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("RestNotifier", name, importance);
        channel.setDescription(description);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "DairyNotifier")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("Не забывайте ввести дневник!")
                .setSmallIcon(R.drawable.edit);



        notificationManager.notify(1, builder.build());




        return Result.success();
    }
}
