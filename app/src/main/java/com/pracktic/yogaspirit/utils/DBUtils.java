package com.pracktic.yogaspirit.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pracktic.yogaspirit.data.OnDataLoader;
import com.pracktic.yogaspirit.data.OnDataUploader;
import com.pracktic.yogaspirit.data.user.Session;
import com.pracktic.yogaspirit.data.user.UserData;

public class DBUtils {

    private static final String TAG = DBUtils.class.getName();
    public static DatabaseReference getUsersRef(){
        FirebaseDatabase db = getDBI();
        return db.getReference("users");
    }


    public static FirebaseDatabase getDBI(){
        return FirebaseDatabase.getInstance();
    }

    public static void getUserData(Session session, OnDataLoader<UserData> retriever){
         getUsersRef().child(session.getLogin()).get().addOnSuccessListener(dataSnapshot -> {
             retriever.onLoad(dataSnapshot.getValue(UserData.class));
         }).addOnFailureListener(e -> {
             Log.e(TAG, "getUserData: error during retrieving user data", e );
         });
    }
    public static void uploadUserData(Session session, UserData userData , OnDataUploader uploader){
        getUsersRef().child(session.getLogin()).setValue(userData).addOnSuccessListener(unused -> {
            Log.d(TAG, "uploadUserData: successfully uploaded user data for: "+userData.getEmail() );
            uploader.onUpload();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "uploadUserData: error during uploading data", e );
        });
    }
}
