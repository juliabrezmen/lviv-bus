package com.lvivbus.utils;

import android.util.Log;

public class L {

    static String TAG = "LvivBus";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void v(String msg) {
        Log.v(TAG, msg);
    }

    public static void v(String message, Object... args) {
        Log.v(TAG, String.format(message, args));
    }

    public static void w(String msg, Throwable t) {
        Log.w(TAG, msg, t);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void w(Throwable t) {
        Log.w(TAG, t);
    }

}
