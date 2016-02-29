package com.lvivbus.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.lvivbus.ui.data.Bus;

public class PreferencesManager {

    private static final String LVIV_BUS_PREFS = "Lviv-Bus-Prefs";
    private static final String KEY_ID = "Id";
    private static final String KEY_CODE = "Code";
    private static final String KEY_NAME = "Name";
    private static final String KEY_RECENT_DATE = "recentDate";

    public static void saveBus(@NonNull Bus bus, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LVIV_BUS_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.clear();
        editor.putInt(KEY_ID, bus.getId());
        editor.putString(KEY_CODE, bus.getCode());
        editor.putString(KEY_NAME, bus.getName());
        editor.putLong(KEY_RECENT_DATE, bus.getRecentDate());

        editor.apply();
    }

    @Nullable
    public static Bus getBus(Context context) {
        Bus bus = null;
        SharedPreferences prefs = context.getSharedPreferences(LVIV_BUS_PREFS, Activity.MODE_PRIVATE);
        int id = prefs.getInt(KEY_ID, 0);
        if (id != 0) {
            bus = new Bus();
            bus.setId(id);
            bus.setCode(prefs.getString(KEY_CODE, null));
            bus.setName(prefs.getString(KEY_NAME, null));
            bus.setRecentDate(prefs.getLong(KEY_RECENT_DATE, 0));
        }
        return bus;
    }
}
