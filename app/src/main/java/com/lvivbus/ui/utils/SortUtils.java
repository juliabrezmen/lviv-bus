package com.lvivbus.ui.utils;

import android.support.annotation.NonNull;
import com.lvivbus.ui.data.Bus;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils {
    public static void sortByName(@NonNull List<Bus> allBusList) {
        Collections.sort(allBusList, new Comparator<Bus>() {
            @Override
            public int compare(Bus lhs, Bus rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
    }
}
