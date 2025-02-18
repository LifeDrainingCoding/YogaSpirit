package com.pracktic.yogaspirit.data;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pracktic.yogaspirit.data.user.Session;


public class SessionManager {
    public static void saveSession(Session session, Context context){
        SharedPreferences preferences = context.getSharedPreferences("Session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("session",new Gson().toJson(session));
        editor.apply();
    }

    public static Session restoreSession(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Session", MODE_PRIVATE);
        String json = preferences.getString("session","null");
        return new Gson().fromJson(json,Session.class);
    }

}
