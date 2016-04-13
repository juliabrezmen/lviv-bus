package com.lvivbus.model.event;

public class NetworkChangedEvent {
    private boolean isConnected;

    public NetworkChangedEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
