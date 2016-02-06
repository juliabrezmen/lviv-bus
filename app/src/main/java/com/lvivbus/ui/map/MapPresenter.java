package com.lvivbus.ui.map;

import android.os.SystemClock;
import com.lvivbus.model.http.BusAPI;
import com.lvivbus.model.http.Converter;
import com.lvivbus.ui.data.BusMarker;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MapPresenter {

    private Executor executor;
    private MapActivity mapActivity;

    public void onAttachActivity(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void onDetachActivity() {
        mapActivity = null;
    }

    public void onMapReady() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                onMarkersLoaded(Converter.toBusMarkerList(BusAPI.getBusLocation("C2|713025")));
                SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
                if (mapActivity != null) {
                    executor.execute(this);
                }
            }
        });
    }

    private void onMarkersLoaded(final List<BusMarker> markerList) {
        if (mapActivity != null) {
            mapActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mapActivity.displayMarkers(markerList);
                }
            });
        }
    }
}
