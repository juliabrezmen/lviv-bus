package com.lvivbus.ui.selectbus;

import com.lvivbus.model.db.BusDAO;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.event.SelectBusEvent;
import io.realm.Realm;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class BusListPresenter {
    private BusListActivity activity;
    private Realm realm;

    public void onAttachActivity(BusListActivity busListActivity) {
        this.activity = busListActivity;

        realm = Realm.getDefaultInstance();

        List<Bus> recent = new ArrayList<Bus>();
//        Bus bus1 = new Bus();
//        bus1.setName("24");
//        Bus bus2 = new Bus();
//        bus2.setName("15");
//        Bus bus3 = new Bus();
//        bus3.setName("3");
//        recent.add(bus1);
//        recent.add(bus2);
//        recent.add(bus3);
        activity.setData(recent, BusDAO.getAll(realm));
    }

    public void onDetachActivity() {
        realm.close();
        activity = null;
    }

    public void onBusClicked(Bus bus) {
        EventBus.getDefault().post(new SelectBusEvent(Bus.makeRawCopy(bus)));
        activity.finish();
    }

    public void onToolbarBackClicked() {
        activity.finish();
    }
}