package com.lvivbus.ui.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.utils.L;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapPresenter mapPresenter;
    private GoogleMap map;
    private SparseArray<Marker> markerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        markerMap = new SparseArray<Marker>();
        mapPresenter = new MapPresenter();
        mapPresenter.onAttachActivity(this);
    }

    @Override
    protected void onDestroy() {
        mapPresenter.onDetachActivity();
        super.onDestroy();
    }

    private void initView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(49.8416998d, 24.0295719d), 14);
                map.animateCamera(cameraUpdate);
            }
        });
    }

    public void clearAllMarkers() {
        map.clear();
        markerMap.clear();
    }

    public void displayMarkers(@NonNull final List<BusMarker> markerList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayMarkersInternal(markerList);
            }
        });
    }

    private void displayMarkersInternal(@NonNull List<BusMarker> busMarkers) {
        L.v("Displaying markers: " + busMarkers.size());
        for (BusMarker busMarker : busMarkers) {
            Marker storedMarker = markerMap.get(busMarker.getVehicleId());
            if (storedMarker == null) {
                LatLng position = new LatLng(busMarker.getLat(), busMarker.getLon());
                Marker marker = map.addMarker(new MarkerOptions().position(position));
                markerMap.put(busMarker.getVehicleId(), marker);
            } else {
                LatLng position = new LatLng(busMarker.getLat(), busMarker.getLon());
                storedMarker.setPosition(position);
            }
        }
    }

}
