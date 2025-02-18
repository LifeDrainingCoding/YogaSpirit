package com.pracktic.yogaspirit.data.user;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class UserData implements Serializable {

    private String email, password;
    private  HashMap<String,Integer> levels ;
    private HashMap<LocalDate, Integer > meditationStats;

    public UserData(String email, String password, @Nullable HashMap<String,Integer> levels) {
        this.levels = levels;
        this.email = email;
        this.password = password;
    }

    public void setLevels(HashMap<String, Integer> levels) {
        this.levels = levels;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, Integer> getLevels() {
        return levels;
    }

    public HashMap<LocalDate, Integer> getMeditationStats() {
        return meditationStats;
    }

    public void setMeditationStats(HashMap<LocalDate, Integer> meditationStats) {
        this.meditationStats = meditationStats;
    }

    @NonNull
    @Override
    public String toString() {
        return getEmail();
    }
}
