package com.pracktic.yogaspirit.data.singleton;

import android.os.Handler;
import android.os.Looper;

import java.time.Duration;
import java.time.LocalDate;

public class Timer {
    private Long start;

    public boolean isTimerStarted;
    private static Timer INSTANCE;
    private Timer(){}

    public static Timer getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new Timer();
        }

        return INSTANCE ;
    }

    public void startTimer(){
        isTimerStarted = true;
        start = System.currentTimeMillis();
    }
    public Timestamp stopTimer(){
        if (start == null){
            return null;
        }
        isTimerStarted = false;

        long endpoint = System.currentTimeMillis() - start;

        return new Timestamp((int)Duration.ofMillis(endpoint).getSeconds(), LocalDate.now());
    }
}
