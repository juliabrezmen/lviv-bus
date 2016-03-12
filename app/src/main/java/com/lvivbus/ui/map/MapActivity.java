package com.lvivbus.ui.map;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lvivbus.ui.R;

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ACCESS_LOCATION = 1;

    private MapPresenter presenter;
    private GoogleMap map;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        initToolBar();
        initView();

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        presenter.onSaveInstanceState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
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
