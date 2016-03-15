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
import com.lvivbus.model.db.BusDAO;
import com.lvivbus.model.http.BusAPI;
import com.lvivbus.model.http.Converter;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.ui.data.BusStation;
import com.lvivbus.ui.selectbus.BusListActivity;
import com.lvivbus.ui.utils.GsonUtils;
import com.lvivbus.ui.utils.PreferencesManager;
import com.lvivbus.utils.L;
import io.realm.Realm;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapPresenter {

    private static final String KEY_MARKER_LIST = "Marker List";
    private static final int MIN_DISTANCE = 3;
    private MapActivity activity;
    private Bus selectedBus;
    private AsyncTask<String, Void, List<BusMarker>> loadMarkersTask;
    private List<BusMarker> markerList;
    private SparseArray<Marker> markerMap;
    private Handler handler;

    //TODO: network check

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (selectedBus != null) {
                loadMarkers();
            }
        }
    };
    private Realm realm;
    private AsyncTask<Void, Void, List<BusStation>> loadRouteTask;

    public void onAttachActivity(MapActivity mapActivity) {
        activity = mapActivity;
        markerMap = new SparseArray<Marker>();
        handler = new Handler();

        realm = Realm.getDefaultInstance();

    }

    public void onActivityVisible() {
        loadData();
    }

    private void loadData() {
        int id = PreferencesManager.getBusId(activity.getApplicationContext());
        if (id != 0) {
            if (selectedBus == null || (selectedBus.getId() != id)) {
                selectedBus = BusDAO.getBus(realm, id);
                if (selectedBus != null) {
                    activity.setSubtitle(selectedBus.getName());
                    activity.clearAllMarkers();
                    markerMap.clear();
                }
            }
        }
        handler.post(runnable);
        loadRoute();
    }

    public void onActivityNotVisible() {
        cancelMarkerLoading();
    }

    public void onDetachActivity() {
        realm.close();
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
        loadData();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(KEY_MARKER_LIST, GsonUtils.toJson(markerList));
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String markers = savedInstanceState.getString(KEY_MARKER_LIST);
        markerList = GsonUtils.fromJson(markers);
    }

    private void loadRoute() {
        if (selectedBus != null) {
            if (selectedBus.getBusStations().isEmpty()) {
                loadRouteFromInternet();
            } else {
                activity.drawRoute(selectedBus.getBusStations());
            }
        }
    }

    private void loadMarkers() {
        loadMarkersTask = new LoadMarkersTask().execute(selectedBus.getCode());
    }

    private void loadRouteFromInternet() {
        loadRouteTask = new LoadRouteTask(selectedBus.getId(), selectedBus.getCode()).execute();
    }

    private void cancelMarkerLoading() {
        if (loadMarkersTask != null) {
            loadMarkersTask.cancel(false);
        }

        if (loadRouteTask != null) {
            loadRouteTask.cancel(false);
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

    private class LoadMarkersTask extends AsyncTask<String, Void, List<BusMarker>> {
        @Override
        protected List<BusMarker> doInBackground(String... params) {
            //L.v(String.format("Loading markers for bus: %s", bus.getName()));
            markerList = Converter.toBusMarkerList(BusAPI.getBusLocation(params[0]));
            return markerList;
        }

        @Override
        protected void onPostExecute(List<BusMarker> markerList) {
            displayMarkers(markerList);
            handler.postDelayed(runnable, TimeUnit.SECONDS.toMillis(5));
        }
    }

    private class LoadRouteTask extends AsyncTask<Void, Void, List<BusStation>> {

        private int busId;
        private String busCode;

        public LoadRouteTask(int busId, String busCode) {
            this.busId = busId;
            this.busCode = busCode;
        }

        @Override
        protected List<BusStation> doInBackground(Void... params) {
            List<BusStation> busStationList = Converter.toBusStationList(BusAPI.getBusRoute(busCode));
            BusDAO.setStationList(busId, busStationList);
            return busStationList;

        }

        @Override
        protected void onPostExecute(List<BusStation> busStationList) {
            activity.drawRoute(busStationList);
        }
    }

}
