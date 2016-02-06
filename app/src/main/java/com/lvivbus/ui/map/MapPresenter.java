package com.lvivbus.ui.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapPresenter {
    private MapActivity mapActivity;

    public void onAttachActivity(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    public void onDetachActivity() {
        mapActivity = null;
    }

    public void onMapReady() {
        mapActivity.displayMarkers(new ArrayList<LatLng>());
    }
}
