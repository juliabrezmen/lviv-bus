package com.lvivbus.model.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.lvivbus.model.data.BusLocationResult;
import com.lvivbus.model.data.BusResult;
import com.lvivbus.model.data.BusStationResult;
import com.lvivbus.utils.L;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusAPI {

    @NonNull
    public static List<BusResult> getBusList() {
        String url = "http://82.207.107.126:13541/SimpleRIDE/LAD/SM.WebApi/api/CompositeRoute";
        BusResult[] busResults = get("", url, BusResult[].class);
        return busResults == null ? new ArrayList<BusResult>() : Arrays.asList(busResults);
    }

    @NonNull
    public static List<BusLocationResult> getBusLocation(@NonNull String code) {
        String url = "http://82.207.107.126:13541/SimpleRIDE/LAD/SM.WebApi/api/RouteMonitoring/?code=";
        BusLocationResult[] locationResults = get(code, url, BusLocationResult[].class);
        return locationResults == null ? new ArrayList<BusLocationResult>() : Arrays.asList(locationResults);
    }

    @NonNull
    public static List<BusStationResult> getBusRoute(@NonNull String code) {
        String url = "http://82.207.107.126:13541/SimpleRIDE/LAD/SM.WebApi/api/path/?code=";
        BusStationResult[] stationResults = get(code, url, BusStationResult[].class);
        return stationResults == null ? new ArrayList<BusStationResult>() : Arrays.asList(stationResults);
    }

    @Nullable
    private static <T> T get(@NonNull String busCode, String url, Class<T> cls) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .url(url + busCode)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = toJson(response.body().string());
                Gson gson = new Gson();
                return gson.fromJson(json, cls);
            }
        } catch (IOException e) {
            L.w(e);
        }

        return null;
    }

    @NonNull
    private static String toJson(@NonNull String xml) {
        int start = xml.indexOf('[');
        int end = xml.lastIndexOf(']');
        if (start != -1 && end != -1 && end > start) {
            return xml.substring(start, end + 1);
        }

        return xml;
    }

}
