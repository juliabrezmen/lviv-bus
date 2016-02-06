package com.lvivbus.ui.data;

import com.google.android.gms.maps.model.Marker;

public class MarkerDisplayable {

    private Marker marker;

    private BusMarker busMarker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public BusMarker getBusMarker() {
        return busMarker;
    }

    public void setBusMarker(BusMarker busMarker) {
        this.busMarker = busMarker;
    }
}
