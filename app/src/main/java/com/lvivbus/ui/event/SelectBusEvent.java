package com.lvivbus.ui.event;

import android.support.annotation.NonNull;
import com.lvivbus.ui.data.Bus;

public class SelectBusEvent {

    private Bus bus;

    public SelectBusEvent(Bus bus) {
        this.bus = bus;
    }

    @NonNull
    public Bus getBus() {
        return bus;
    }
}
