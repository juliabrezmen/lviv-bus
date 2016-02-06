package com.lvivbus.presenters;

import com.lvivbus.app.MapActivity;

public class MapPresenter {
    private MapActivity mapActivity;

    public void onAttachActivity(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    public void onDetachActivity() {
        mapActivity = null;
    }
}
