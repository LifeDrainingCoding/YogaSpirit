package com.pracktic.yogaspirit.data.consts;

import androidx.fragment.app.Fragment;

import com.pracktic.yogaspirit.fragments.AddonsFragment;
import com.pracktic.yogaspirit.fragments.DairyFragment;
import com.pracktic.yogaspirit.fragments.MeditationFragment;
import com.pracktic.yogaspirit.fragments.ProgressFragment;
import com.pracktic.yogaspirit.fragments.RecFragment;
import com.pracktic.yogaspirit.fragments.TestsFragment;

public enum TabTypes {
    REC(new RecFragment()), MED(new MeditationFragment()),TESTS(new TestsFragment()), DAIRY(new DairyFragment()),
    PROGRESS(new ProgressFragment()),   ADDONS(new AddonsFragment());

    public final Fragment fragment;
    TabTypes(Fragment fragment){
        this.fragment = fragment;
    }
}
