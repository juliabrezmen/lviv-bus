package com.lvivbus.ui.map;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import com.lvivbus.model.http.BusAPI;
import com.lvivbus.model.http.Converter;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.ui.event.SelectBusEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        EventBus.getDefault().register(this);
    }

    public void onDetachActivity() {
        mapActivity = null;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(final SelectBusEvent event) {
        mapActivity.clearAllMarkers();
        loadMarkers(event.getBus());
    }

    private void loadMarkers(@NonNull final Bus bus) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                onMarkersLoaded(Converter.toBusMarkerList(BusAPI.getBusLocation(bus.getCode())));
                SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
                if (mapActivity != null) {
                    executor.execute(this);
                }
            }
        });
    }

    private void onMarkersLoaded(@NonNull List<BusMarker> markerList) {
        if (mapActivity != null) {
            mapActivity.displayMarkers(markerList);
        }
    }
}
