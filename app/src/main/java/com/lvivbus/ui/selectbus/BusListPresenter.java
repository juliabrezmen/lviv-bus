package com.lvivbus.ui.selectbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.lvivbus.model.db.BusDAO;
import com.lvivbus.ui.abs.AbsPresenter;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.utils.PreferencesManager;
import com.lvivbus.ui.utils.SortUtils;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;

public class BusListPresenter extends AbsPresenter<BusListActivity> {

    private static final int MAX_RECENT_COUNT = 3;

    private Realm realm;

    public BusListPresenter(BusListActivity activity) {
        super(activity);
    }

    @Override
    protected void initPresenter(@Nullable Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        loadData();
    }

    @Override
    protected void onDestroyActivity() {
        realm.close();
        super.onDestroyActivity();
    }

    public void onBusClicked(Bus bus) {
        BusDAO.setRecentDate(bus.getId(), System.currentTimeMillis());
        PreferencesManager.saveBusId(bus.getId(), activity.getApplicationContext());
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