package com.lvivbus.ui.map;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.lvivbus.ui.R;
import com.lvivbus.ui.abs.AbsActivity;
import com.lvivbus.ui.data.BusStation;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;

public class MapActivity extends AbsActivity<MapPresenter> {

    private static final int REQUEST_CODE_ACCESS_LOCATION = 1;

    private GoogleMap map;
    private Toolbar mToolbar;

    @Override
    protected void onRestart() {
        presenter.onActivityVisible();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        presenter.onActivityNotVisible();
        super.onPause();
    }

    @Override
    protected MapPresenter createPresenter() {
        return new MapPresenter(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.map_activity);
        initToolBar();
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        presenter.onSaveInstanceState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setSubtitle(@NonNull String text) {
        mToolbar.setSubtitle(text);
    }

    public void clearAllMarkers() {
        if (map != null) {
            map.clear();
        }
    }

    public Marker addMarker(MarkerOptions options) {
        return map.addMarker(options);
    }

    public CameraPosition getCameraPosition() {
        return map.getCameraPosition();
    }

    public void animateMarker(final Marker marker, final LatLng toPosition) {
        Projection projection = map.getProjection();
        Point startPoint = projection.toScreenLocation(marker.getPosition());
        final Handler handler = new Handler();
        final LatLng startLatLng = projection.fromScreenLocation(startPoint);
        final Interpolator interpolator = new LinearInterpolator();
        final long start = SystemClock.uptimeMillis();
        final long duration = 2000;

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void drawRoute(@NonNull List<BusStation> busStationList) {
        PolylineOptions options = new PolylineOptions()
                .width(5)
                .color(getResources().getColor(R.color.colorPrimary))
                .geodesic(true);
        for (BusStation station : busStationList) {
            LatLng point = new LatLng(station.getLat(), station.getLon());
            options.add(point);
        }
        map.addPolyline(options);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @TargetApi(M)
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
