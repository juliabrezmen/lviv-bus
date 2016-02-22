package com.lvivbus.ui.data;

import android.support.annotation.NonNull;
import com.lvivbus.ui.selectbus.Displayable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Bus extends RealmObject implements Displayable {

    @PrimaryKey
    private int id;

    private String code;

    private String name;

    private long recentDate;

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

    @NonNull
    public static Bus makeRawCopy(@NonNull Bus from) {
        Bus to = new Bus();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setCode(from.getCode());
        to.setRecentDate(from.getRecentDate());

        return to;
    }
}
