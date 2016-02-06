package com.lvivbus.ui.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lvivbus.ui.R;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapPresenter mapPresenter;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapPresenter = new MapPresenter();
        mapPresenter.onAttachActivity(this);

        initView();

    }

    private void initView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                mapPresenter.onMapReady();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mapPresenter.onDetachActivity();
        super.onDestroy();
    }

    public void displayMarkers(@NonNull List<LatLng> placeList) {
        if (!placeList.isEmpty()) {
            for (LatLng place : placeList) {
                map.addMarker(new MarkerOptions().position(place));
            }
            map.moveCamera(CameraUpdateFactory.newLatLng(placeList.get(0)));
        }
    }

}
