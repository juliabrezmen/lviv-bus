package com.lvivbus.model.data;

import com.google.gson.annotations.SerializedName;

public class BusLocationResult {

    @SerializedName("X")
    public float x;

    @SerializedName("Y")
    public float y;

    @SerializedName("Angle")
    public float angle;

    @SerializedName("RouteId")
    public int routeId;

    @SerializedName("VehicleId")
    public int vehicleId;

    @SerializedName("VehicleName")
    public String vehicleName;

}
