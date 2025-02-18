package com.pracktic.yogaspirit.widgets;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.radiobutton.MaterialRadioButton;

public class RadioButtonExt extends MaterialRadioButton {
    public int level = 0;

    public RadioButtonExt(@NonNull Context context) {
        super(context);
    }

    public RadioButtonExt(@NonNull Context context, int level) {
        super(context);
        this.level = level;
    }

}
