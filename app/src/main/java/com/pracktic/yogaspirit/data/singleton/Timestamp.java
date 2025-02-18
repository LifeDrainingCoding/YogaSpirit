package com.pracktic.yogaspirit.data.singleton;

import java.time.LocalDate;

public class Timestamp {
    private int totalTime;
    private LocalDate date;

    public Timestamp(int totalTime, LocalDate date) {
        this.totalTime = totalTime;
        this.date = date;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
