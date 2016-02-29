package com.lvivbus.ui.data;

import com.lvivbus.ui.selectbus.Displayable;

public class Title implements Displayable {
    private String value;

    public Title(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
