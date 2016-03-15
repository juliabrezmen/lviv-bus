package com.lvivbus.model.http;

import android.support.annotation.NonNull;
import com.lvivbus.model.data.BusLocationResult;
import com.lvivbus.model.data.BusResult;
import com.lvivbus.model.data.BusStationResult;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.ui.data.BusStation;

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
            marker.setAngle(locationResult.angle);
            marker.setVehicleId(locationResult.vehicleId);
            latLngList.add(marker);
        }

        return latLngList;
    }

    @NonNull
    public static List<BusStation> toBusStationList(@NonNull List<BusStationResult> stationResultList) {
        List<BusStation> busStationList = new ArrayList<BusStation>();
        for (BusStationResult busStation : stationResultList) {
            BusStation station = new BusStation();
            station.setLat(busStation.y);
            station.setLon(busStation.x);
            busStationList.add(station);
        }
        return busStationList;
    }
}
