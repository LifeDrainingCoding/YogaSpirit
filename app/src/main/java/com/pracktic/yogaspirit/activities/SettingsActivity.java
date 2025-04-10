package com.pracktic.yogaspirit.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.pracktic.yogaspirit.R;
import com.pracktic.yogaspirit.data.consts.Prefs;
import com.pracktic.yogaspirit.job.JobDairy;
import com.pracktic.yogaspirit.job.JobHelper;
import com.pracktic.yogaspirit.job.JobRest;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TAG = "Preferences";

    private static final String TITLE_TAG = "Настройки";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

            setTitle(TITLE_TAG);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, new SyncFragment())
                .addToBackStack(null)
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
//        final Bundle args = pref.getExtras();
//        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
//                getClassLoader(),
//                pref.getFragment());
//        fragment.setArguments(args);
//        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, new SyncFragment())
                .addToBackStack(null)
                .commit();
        setTitle(pref.getTitle());
        return true;
    }



    public static class SyncFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.sync_preferences, rootKey);




            SwitchPreferenceCompat dairyPrefs = findPreference(Prefs.DAIRY);
            SwitchPreferenceCompat restPrefs = findPreference(Prefs.REST);
            Context context = requireContext();
            if (dairyPrefs != null && restPrefs != null) {
                dairyPrefs.setOnPreferenceChangeListener((preference, newValue) -> {
                    Log.d("Preferences", String.format(" Dairy Notifications enabled: %s", newValue));

                    boolean isEnabled = ((boolean) newValue);
                    if (!isEnabled){
                        JobHelper.cancelJob(context,JobDairy.JOB_ID );
                        Log.d(TAG, "onCreatePreferences: Dairy job canceled ");
                    }else {
                        Log.d(TAG, "onCreatePreferences:  Dairy job scheduled");
                        JobHelper.scheduleJob(context, JobDairy.class);
                    }

                    return true;
                });

                restPrefs.setOnPreferenceChangeListener((preference, newValue) -> {
                    Log.d("Preferences", String.format(" Rest Notifications enabled: %s", newValue));

                    boolean isEnabled = ((boolean) newValue);
                    if (!isEnabled){
                        JobHelper.cancelJob(context,JobRest.JOB_ID );
                        Log.d(TAG, "onCreatePreferences:  Rest job canceled");
                    }else {
                        Log.d(TAG, "onCreatePreferences:  Rest job scheduled");
                        JobHelper.scheduleJob(context, JobRest.class);
                    }

                    return true;
                });

            }

        }


    }
}