package com.lvivbus.ui.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.utils.L;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapPresenter presenter;
    private GoogleMap map;
    private SparseArray<Marker> markerMap;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        initToolBar();
        initView();

        markerMap = new SparseArray<Marker>();
        presenter = new MapPresenter();
        presenter.onAttachActivity(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetachActivity();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        presenter.onActivityNotVisible();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presenter.onActivityVisible();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                presenter.onToolbarFilterClicked();
                return true;
            case android.R.id.home:
                presenter.onToolbarBackClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setSubtitle(@NonNull String text) {
        mToolbar.setSubtitle(text);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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
