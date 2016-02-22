package com.lvivbus.ui.selectbus;

import com.lvivbus.model.db.BusDAO;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.event.SelectBusEvent;
import com.lvivbus.ui.utils.SortUtils;
import io.realm.Realm;
import io.realm.RealmResults;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class BusListPresenter {

    private static final int MAX_RECENT_COUNT = 3;

    private BusListActivity activity;
    private Realm realm;

    public void onAttachActivity(BusListActivity busListActivity) {
        activity = busListActivity;
        realm = Realm.getDefaultInstance();
        loadData();
    }

    public void onDetachActivity() {
        realm.close();
        activity = null;
    }

    public void onBusClicked(Bus bus) {
        EventBus.getDefault().post(new SelectBusEvent(Bus.makeRawCopy(bus)));
        BusDAO.setRecentDate(bus, System.currentTimeMillis());
        activity.finish();
    }

    public void onToolbarBackClicked() {
        activity.finish();
    }

    private void loadData() {
        List<Bus> recentBusList = new ArrayList<Bus>();
        List<Bus> allBusList = new ArrayList<Bus>();
        RealmResults<Bus> busList = BusDAO.getAll(realm);

        int recentAdded = 0;
        for (Bus bus : busList) {
            if (bus.getRecentDate() != 0 && recentAdded < MAX_RECENT_COUNT) {
                recentAdded++;
                recentBusList.add(bus);
            } else {
                allBusList.add(bus);
            }
        }

        SortUtils.sortByName(allBusList);

        activity.setData(recentBusList, allBusList);
    }

}