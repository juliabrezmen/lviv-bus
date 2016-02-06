package com.lvivbus.model.http;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lvivbus.model.data.BusResult;
import com.lvivbus.utils.L;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LvivBusAPI {

    @NonNull
    public static List<BusResult> getBusList() {
        List<BusResult> resultList = new ArrayList<BusResult>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .url("http://82.207.107.126:13541/SimpleRIDE/LAD/SM.WebApi/api/CompositeRoute")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = toJson(response.body().string());

                Type type = new TypeToken<List<BusResult>>() {
                }.getType();
                Gson gson = new Gson();

                List<BusResult> parsedList = gson.fromJson(json, type);
                resultList.addAll(parsedList);
            }
        } catch (IOException e) {
            L.w(e);
        }

        return resultList;
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
