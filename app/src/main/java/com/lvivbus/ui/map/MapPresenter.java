package com.lvivbus.ui.map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.lvivbus.model.http.BusAPI;
import com.lvivbus.model.http.Converter;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.ui.selectbus.BusListActivity;
import com.lvivbus.ui.utils.PreferencesManager;
import com.lvivbus.utils.L;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapPresenter {

    private MapActivity activity;
    private Bus selectedBus;
    private CountDownTimer timer;
    private AsyncTask<Bus, Void, List<BusMarker>> task;

    public void onAttachActivity(MapActivity mapActivity) {
        activity = mapActivity;
        initTimer();
    }

    public void onActivityVisible() {
        Bus storedBus = PreferencesManager.getBus(activity.getApplicationContext());
        if (storedBus != null) {
            if (selectedBus == null || (selectedBus.getId() != storedBus.getId())) {
                selectedBus = storedBus;
                activity.setSubtitle(selectedBus.getName());
                activity.clearAllMarkers();
            }
        }
        timer.start();
    }

    public void onActivityNotVisible() {
        cancelMarkerLoading();
    }

    public void onDetachActivity() {
        cancelMarkerLoading();
        activity = null;
    }

    public void onToolbarFilterClicked() {
        Intent intent = new Intent(activity, BusListActivity.class);
        activity.startActivity(intent);
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng lvivCenter = new LatLng(49.842465, 24.026625);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lvivCenter, 12f));
    }

    private void initTimer() {
        timer = new CountDownTimer(TimeUnit.MINUTES.toMillis(1), TimeUnit.SECONDS.toMillis(5)) {
            @Override
            public void onTick(long millisUntilFinished) {
                loadMarkers();
            }

            @Override
            public void onFinish() {
                timer.start();
            }
        };
    }

    private void loadMarkers() {
        if (selectedBus != null) {
            task = new AsyncTask<Bus, Void, List<BusMarker>>() {
                @Override
                protected List<BusMarker> doInBackground(Bus... params) {
                    Bus bus = params[0];
                    L.v(String.format("Loading markers for bus: %s", bus.getName()));
                    return Converter.toBusMarkerList(BusAPI.getBusLocation(bus.getCode()));
                }

                @Override
                protected void onPostExecute(List<BusMarker> markerList) {
                    activity.displayMarkers(markerList);
                }
            };
            task.execute(selectedBus);
        }
    }

    private void cancelMarkerLoading() {
        if (task != null) {
            task.cancel(false);
        }

        if (timer != null) {
            timer.cancel();
        }
    }
}
