package com.lvivbus.ui.data;

import com.lvivbus.ui.selectbus.Displayable;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Bus extends RealmObject implements Displayable {

    @PrimaryKey
    private int id;

    private String code;
    private String name;

    private long recentDate;

    private RealmList<BusStation> busStations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRecentDate() {
        return recentDate;
    }

    public void setRecentDate(long recentDate) {
        this.recentDate = recentDate;
    }

    public RealmList<BusStation> getBusStations() {
        return busStations;
    }

    public void setBusStations(RealmList<BusStation> busStations) {
        this.busStations = busStations;
    }

}
