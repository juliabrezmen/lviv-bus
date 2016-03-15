package com.lvivbus.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String LVIV_BUS_PREFS = "Lviv-Bus-Prefs";
    private static final String KEY_ID = "Id";

    public static void saveBusId(int id, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LVIV_BUS_PREFS, Activity.MODE_PRIVATE);
        prefs.edit().putInt(KEY_ID, id).apply();
    }

    public static int getBusId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LVIV_BUS_PREFS, Activity.MODE_PRIVATE);
        return prefs.getInt(KEY_ID, 0);
    }
}
