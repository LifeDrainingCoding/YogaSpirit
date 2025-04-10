package com.pracktic.yogaspirit.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.data.consts.TabTypes;
import com.pracktic.yogaspirit.job.JobDairy;
import com.pracktic.yogaspirit.job.JobHelper;
import com.pracktic.yogaspirit.job.JobRest;
import com.pracktic.yogaspirit.workers.DairyWorker;
import com.pracktic.yogaspirit.workers.RestWorker;

import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();
    private TabLayout tabLayout;

    private MaterialToolbar toolbar;
    private MaterialButton settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intent =new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        if (EasyPermissions.hasPermissions(this, Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WAKE_LOCK)) {
            Log.d(TAG, "onCreate: hasPermission");
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            if (settings.getBoolean("dairy", false)){
                JobHelper.scheduleJob(this, JobDairy.class);
            }

            if (settings.getBoolean("rest", false)){
                JobHelper.scheduleJob(this, JobRest.class);
            }

        }else {
            EasyPermissions.requestPermissions(this,"Нужно разрешение для показа уведомлений",200,
                    Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WAKE_LOCK
            );

        }


        tabLayout = findViewById(R.id.tabLayout);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            tabLayout.getTabAt(i).setTag(TabTypes.values()[i].name());


        }
    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {

                }
            });

    @Override
    protected void onStart() {
        super.onStart();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag()!=null && tab.getTag() instanceof String tag ){
                    try {

                        TabTypes tabType = TabTypes.valueOf(tag);
                        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                                .replace(R.id.fragmentContainer,tabType.fragment)
                                .commit();


                    }catch (IllegalArgumentException ex){
                        Toast.makeText(MainActivity.this, "Ошибка в названии тега!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Log.e(TAG, "onTabSelected: "+tab.getTag());
                    Toast.makeText(MainActivity.this,"ТЕГ НЕ СТРОКА, СООБЩИТЕ РАЗРАБОТЧИКУ!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getTag()!=null && tab.getTag() instanceof String tag ){
                    try {

                        TabTypes tabType = TabTypes.valueOf(tag);

                        tabType.fragment.onDestroy();



                    }catch (IllegalArgumentException ex){
                        Toast.makeText(MainActivity.this, "Ошибка в названии тега!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Log.e(TAG, "onTabSelected: "+tab.getTag());
                    Toast.makeText(MainActivity.this,"ТЕГ НЕ СТРОКА, СООБЩИТЕ РАЗРАБОТЧИКУ!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}