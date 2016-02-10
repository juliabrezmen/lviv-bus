package com.lvivbus.ui.map;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
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
import java.util.concurrent.TimeUnit;

public class MapPresenter {

    private Handler handler;
    private MapActivity activity;
    private Bus mBus;
    private boolean loadingStared;

    public void onAttachActivity(MapActivity mapActivity) {
        activity = mapActivity;
        HandlerThread handlerThread = new HandlerThread(HandlerThread.class.getName());
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        EventBus.getDefault().register(this);
    }

    public void onDetachActivity() {
        activity = null;
        EventBus.getDefault().unregister(this);
    }

    public void onActivityNotVisible() {
        loadingStared = false;
        handler.removeCallbacksAndMessages(null);
    }

    public void onActivityVisible() {
        if (mBus != null) {
            loadMarkers(mBus);
        }
    }

    public void onToolbarFilterClicked() {
        Intent intent = new Intent(activity, BusListActivity.class);
        activity.startActivity(intent);
    }

    @Subscribe
    public void onEvent(final SelectBusEvent event) {
        mBus = event.getBus();
        activity.setSubtitle(mBus.getName());
        activity.clearAllMarkers();
        loadMarkers(mBus);
    }

    private void loadMarkers(@NonNull final Bus bus) {
        if (!loadingStared) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onMarkersLoaded(Converter.toBusMarkerList(BusAPI.getBusLocation(bus.getCode())));
                    handler.postDelayed(this, TimeUnit.SECONDS.toMillis(5));
                }
            });
            loadingStared = true;
        }
    }

    private void onMarkersLoaded(@NonNull List<BusMarker> markerList) {
        if (activity != null) {
            activity.displayMarkers(markerList);
        }
    }
}
