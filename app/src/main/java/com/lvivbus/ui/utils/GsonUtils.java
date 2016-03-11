package com.lvivbus.ui.utils;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lvivbus.ui.data.BusMarker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonUtils {

    public static String toJson(@NonNull List<BusMarker> markerList) {
        return new Gson().toJson(markerList);
    }


    @NonNull
    public static List<BusMarker> fromJson(String json) {
        Type type = new TypeToken<ArrayList<BusMarker>>() {
        }.getType();
        List<BusMarker> markerList = new Gson().fromJson(json, type);
        return markerList != null ? markerList : new ArrayList<BusMarker>();
    }
}
