package com.lvivbus.model.http;

import android.support.annotation.NonNull;
import com.lvivbus.model.data.BusLocationResult;
import com.lvivbus.model.data.BusResult;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusMarker;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    @NonNull
    public static List<Bus> toBusList(@NonNull List<BusResult> busResultList) {
        List<Bus> busList = new ArrayList<Bus>();
        for (BusResult busResult : busResultList) {
            Bus bus = new Bus();
            bus.setId(busResult.id);
            bus.setCode(busResult.code);
            bus.setName(busResult.name);
            busList.add(bus);
        }

        return busList;
    }

    @NonNull
    public static List<BusMarker> toBusMarkerList(@NonNull List<BusLocationResult> locationResultList) {
        List<BusMarker> latLngList = new ArrayList<BusMarker>();
        for (BusLocationResult locationResult : locationResultList) {
            BusMarker marker = new BusMarker();
            marker.setLat(locationResult.y);
            marker.setLon(locationResult.x);
            marker.setVehicleId(locationResult.vehicleId);
            latLngList.add(marker);
        }

        return latLngList;
    }
}
