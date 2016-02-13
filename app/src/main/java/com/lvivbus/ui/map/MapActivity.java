package com.lvivbus.ui.map;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.utils.L;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ACCESS_LOCATION = 1;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbar.inflateMenu(R.menu.map_main);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                presenter.onToolbarFilterClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setSubtitle(@NonNull String text) {
        mToolbar.setSubtitle(text);
    }

    public void clearAllMarkers() {
        map.clear();
        markerMap.clear();
    }

    @UiThread
    public void displayMarkers(@NonNull final List<BusMarker> markerList) {
        L.v("Displaying markers: " + markerList.size());
        for (BusMarker busMarker : markerList) {
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
                presenter.onMapReady(googleMap);
                displayMyLocation();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void displayMyLocation() {
        int accessFineLocationResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (accessFineLocationResult != PackageManager.PERMISSION_GRANTED && accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_LOCATION);
        } else {
            map.setMyLocationEnabled(true);
        }
    }

}
