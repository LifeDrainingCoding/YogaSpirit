package com.pracktic.yogaspirit.job;

import static com.pracktic.yogaspirit.job.JobRest.JOB_ID;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;

public class JobHelper {



    public static <T extends JobService> void  scheduleJob(Context context, Class<T> tClass) {
        ComponentName componentName = new ComponentName(context, tClass);
        JobInfo.Builder builder = null;
        if (tClass == JobRest.class){
           builder  = new JobInfo.Builder(JOB_ID, componentName);
        }else {
            builder = new JobInfo.Builder(JobDairy.JOB_ID, componentName);
        }



        builder.setPeriodic(1 * 60 * 1000);


        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresCharging(false);

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (scheduler != null) {
            scheduler.schedule(builder.build());
        }
    }

    public static void cancelJob(Context context, int jobId) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (scheduler != null) {
            scheduler.cancel(jobId);
        }
    }
}
