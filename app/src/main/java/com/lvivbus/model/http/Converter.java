package com.lvivbus.model.http;

import android.support.annotation.NonNull;
import com.lvivbus.model.data.BusResult;
import com.lvivbus.ui.data.Bus;

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
}
