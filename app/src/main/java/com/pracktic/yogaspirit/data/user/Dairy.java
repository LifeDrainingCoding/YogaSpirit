package com.pracktic.yogaspirit.data.user;

import android.content.Context;

import com.pracktic.yogaspirit.data.Note;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.interfaces.OnDataLoader;
import com.pracktic.yogaspirit.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Dairy implements OnDataLoader<UserData> {

    private final Consumer<List<Note>> callback;

    public Dairy(Consumer<List<Note>> callback ){
        this.callback = callback;
    }
    public void loadMyNotes(Context context){
        Session session = SessionManager.restoreSession(context);
        DBUtils.getUserData(session,this);
    }

    @Override
    public void onLoad(UserData userData) {
        if (userData.getNotes() != null) {
            callback.accept(userData.getNotes().values().stream().collect(Collectors.toList()));
        }else {
            callback.accept(new ArrayList<>());
        }
    }
}
