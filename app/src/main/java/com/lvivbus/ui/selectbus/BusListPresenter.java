package com.lvivbus.ui.selectbus;

import com.lvivbus.model.db.BusDAO;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.event.SelectBusEvent;
import io.realm.Realm;
import org.greenrobot.eventbus.EventBus;

public class BusListPresenter {
    private BusListActivity activity;
    private Realm realm;

    public void onAttachActivity(BusListActivity busListActivity) {
        this.activity = busListActivity;

        realm = Realm.getDefaultInstance();
        activity.setData(BusDAO.getAll(realm));
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