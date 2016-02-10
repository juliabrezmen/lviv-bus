package com.lvivbus.ui.map;

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import com.lvivbus.model.http.BusAPI;
import com.lvivbus.model.http.Converter;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.ui.event.SelectBusEvent;
import com.lvivbus.ui.selectbus.BusListActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MapPresenter {

    private Executor executor;
    private MapActivity activity;

    public void onAttachActivity(MapActivity mapActivity) {
        this.activity = mapActivity;
        this.executor = Executors.newSingleThreadExecutor();
        EventBus.getDefault().register(this);
    }

    public void onDetachActivity() {
        activity = null;
        EventBus.getDefault().unregister(this);
    }

    public void onActivityNotVisible() {

    }

    public void onActivityVisible() {

    }

    public void onToolbarFilterClicked() {
        Intent intent = new Intent(activity, BusListActivity.class);
        activity.startActivity(intent);
    }

    public void onToolbarBackClicked() {
        activity.finish();
    }

    @Subscribe
    public void onEvent(final SelectBusEvent event) {
        activity.setSubtitle(event.getBus().getName());
        activity.clearAllMarkers();
        loadMarkers(event.getBus());
    }

    private void loadMarkers(@NonNull final Bus bus) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                onMarkersLoaded(Converter.toBusMarkerList(BusAPI.getBusLocation(bus.getCode())));
                SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
                if (activity != null) {
                    executor.execute(this);
                }
            }
        });
    }

    private void onMarkersLoaded(@NonNull List<BusMarker> markerList) {
        if (activity != null) {
            activity.displayMarkers(markerList);
        }
    }
}
