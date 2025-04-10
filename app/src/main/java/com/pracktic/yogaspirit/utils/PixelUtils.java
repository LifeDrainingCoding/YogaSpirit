package com.pracktic.yogaspirit.utils;

import android.content.res.Resources;

public class PixelUtils {
    public static int dpToPx(Resources resources, int dp){
        return (int) (dp*resources.getDisplayMetrics().density);
    }
}
