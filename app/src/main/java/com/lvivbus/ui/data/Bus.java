package com.lvivbus.ui.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Bus extends RealmObject {

    @PrimaryKey
    private int id;

    private String code;

    private String name;

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
}
