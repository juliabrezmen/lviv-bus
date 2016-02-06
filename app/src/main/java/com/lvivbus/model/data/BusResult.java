package com.lvivbus.model.data;

import com.google.gson.annotations.SerializedName;

public class BusResult {

    @SerializedName("Id")
    public int id;

    @SerializedName("Code")
    public String code;

    @SerializedName("Name")
    public String name;
}
