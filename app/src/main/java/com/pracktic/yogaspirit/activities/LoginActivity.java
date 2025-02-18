package com.pracktic.yogaspirit.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.data.SessionManager;
import com.pracktic.yogaspirit.data.user.Session;
import com.pracktic.yogaspirit.data.user.UserData;
import com.pracktic.yogaspirit.utils.DBUtils;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText loginEdt, passwordEdt;
    private MaterialButton regBtn, loginBtn;
    private FirebaseAuth auth;
    public static final String TAG = "Login Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEdt = findViewById(R.id.loginEdt);
        passwordEdt = findViewById(R.id.passEdt);
        loginBtn = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.registerBtn);
        auth = FirebaseAuth.getInstance();

        //todo включить на релизе
//        passwordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isUserActive()){
            proceedUser();
        }
        regBtn.setOnClickListener(btn -> {
            if (checkFields()){
                registerUser(loginEdt.getText().toString().trim(), passwordEdt.getText().toString().trim());
            }
        });
        loginBtn.setOnClickListener(btn ->{
            if (checkFields()){
                loginUser(loginEdt.getText().toString().trim(), passwordEdt.getText().toString().trim());
            }
        });
    }
    private boolean checkFields(){
        if (loginEdt.getText() == null || passwordEdt.getText() == null ){
            return false;
        }
        return true;
    }

    private boolean isUserActive(){
        return auth.getCurrentUser()!=null;
    }
    private void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference usersRef = DBUtils.getUsersRef();
                        Session session =  new Session(email, password);
                        UserData userData = new UserData(email,password,null);
                        usersRef.child(session.getLogin()).setValue(userData).addOnSuccessListener(unused -> {
                            Toast.makeText(this,"Успешный вход",Toast.LENGTH_SHORT).show();
                            SessionManager.saveSession(session,this);
                            proceedUser();
                        });
                        Log.d(TAG, "createUserWithEmail:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        proceedUser();
                        Log.d(TAG, "login:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "login:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Ошибка при входе.",
                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, task.getException().getLocalizedMessage(),Toast.LENGTH_LONG ).show();

                    }
                });
    }
    private void proceedUser(){
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}