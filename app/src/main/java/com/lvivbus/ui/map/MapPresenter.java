package com.lvivbus.ui.map;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.lvivbus.model.http.BusAPI;
import com.lvivbus.model.http.Converter;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.ui.selectbus.BusListActivity;
import com.lvivbus.ui.utils.GsonUtils;
import com.lvivbus.ui.utils.PreferencesManager;
import com.lvivbus.utils.L;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapPresenter {

    private static final String KEY_MARKER_LIST = "Marker List";
    private static final int MIN_DISTANCE = 3;
    private MapActivity activity;
    private Bus selectedBus;
    private AsyncTask<Bus, Void, List<BusMarker>> task;
    private List<BusMarker> markerList;
    private SparseArray<Marker> markerMap;
    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (selectedBus != null) {
                loadMarkers();
            }
        }
    };

    public void onAttachActivity(MapActivity mapActivity) {
        activity = mapActivity;
        markerMap = new SparseArray<Marker>();
        handler = new Handler();

    }

    public void onActivityVisible() {
        Bus storedBus = PreferencesManager.getBus(activity.getApplicationContext());
        if (storedBus != null) {
            if (selectedBus == null || (selectedBus.getId() != storedBus.getId())) {
                selectedBus = storedBus;
                activity.setSubtitle(selectedBus.getName());
                activity.clearAllMarkers();
                markerMap.clear();
            }
        }
        handler.post(runnable);
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

        if (markerList != null) {
            displayMarkers(markerList);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(KEY_MARKER_LIST, GsonUtils.toJson(markerList));
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String markers = savedInstanceState.getString(KEY_MARKER_LIST);
        markerList = GsonUtils.fromJson(markers);
    }

    private void loadMarkers() {
        task = new AsyncTask<Bus, Void, List<BusMarker>>() {
            @Override
            protected List<BusMarker> doInBackground(Bus... params) {
                Bus bus = params[0];
                L.v(String.format("Loading markers for bus: %s", bus.getName()));
                markerList = Converter.toBusMarkerList(BusAPI.getBusLocation(bus.getCode()));
                return markerList;
            }

            @Override
            protected void onPostExecute(List<BusMarker> markerList) {
                displayMarkers(markerList);
                handler.postDelayed(runnable, TimeUnit.SECONDS.toMillis(5));
            }
        };
        task.execute(selectedBus);
    }

    private void cancelMarkerLoading() {
        if (task != null) {
            task.cancel(false);
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void displayMarkers(@NonNull final List<BusMarker> markerList) {
        L.v("Displaying markers: " + markerList.size());
        for (BusMarker busMarker : markerList) {
            Marker storedMarker = markerMap.get(busMarker.getVehicleId());
            if (storedMarker == null) {
                LatLng position = new LatLng(busMarker.getLat(), busMarker.getLon());
                BitmapDescriptor iconArrow = BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow);
                MarkerOptions options = new MarkerOptions()
                        .position(position)
                        .icon(iconArrow)
                        .rotation(busMarker.getAngle())
                        .anchor(0.5f, 0.5f);
                Marker marker = activity.addMarker(options);
                markerMap.put(busMarker.getVehicleId(), marker);
            } else {
                LatLng oldPosition = storedMarker.getPosition();
                LatLng newPosition = new LatLng(busMarker.getLat(), busMarker.getLon());
                if (calculateDistance(oldPosition, newPosition) > MIN_DISTANCE) {
                    float markerBearing = calculateAngle(oldPosition, newPosition);
                    float cameraBearing = activity.getCameraPosition().bearing;
                    storedMarker.setRotation(markerBearing - cameraBearing);
                    activity.animateMarker(storedMarker, newPosition);
                }
            }
        }
    }

    private float calculateAngle(LatLng from, LatLng to) {
        return createLocation(from).bearingTo(createLocation(to));
    }

    private float calculateDistance(LatLng from, LatLng to) {
        return createLocation(from).distanceTo(createLocation(to));
    }

    private Location createLocation(LatLng from) {
        Location pos1 = new Location("GPS");
        pos1.setLatitude(from.latitude);
        pos1.setLongitude(from.longitude);
        return pos1;
    }

}
