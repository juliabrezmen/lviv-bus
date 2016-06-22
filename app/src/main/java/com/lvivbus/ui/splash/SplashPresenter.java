package com.lvivbus.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.lvivbus.model.db.BusDAO;
import com.lvivbus.model.event.NetworkChangedEvent;
import com.lvivbus.model.http.BusAPI;
import com.lvivbus.model.http.Converter;
import com.lvivbus.model.http.Internet;
import com.lvivbus.ui.R;
import com.lvivbus.ui.abs.AbsPresenter;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.map.MapActivity;
import com.lvivbus.utils.L;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SplashPresenter extends AbsPresenter<SplashActivity> {

    private EventBus eventBus = EventBus.getDefault();

    public SplashPresenter(SplashActivity activity) {
        super(activity);
    }

    @Override
    protected void initPresenter(@Nullable Bundle savedInstanceState) {
        eventBus.register(this);

        if (BusDAO.getAllCount() == 0) {
            loadData();
        } else {
            launchMapActivity();
            activity.finish();
        }
    }

    @Override
    protected void onDestroyActivity() {
        eventBus.unregister(this);
        super.onDestroyActivity();
    }

    @Subscribe
    public void onEvent(NetworkChangedEvent event) {
        if (event.isConnected()) {
            loadData();
        }
    }

    private void loadData() {
        if (Internet.isOn(activity.getApplicationContext())) {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    List<Bus> busList = Converter.toBusList(BusAPI.getBusList());
                    BusDAO.save(busList);
                    L.v(String.format("Buses saved: %d", busList.size()));
                    onLoadingComplete();
                }
            });
        } else {
            activity.showMessage(activity.getString(R.string.no_connection));
        }
    }

    private void onLoadingComplete() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    launchMapActivity();
                    activity.finish();
                }
            });
        }
    }

    private void launchMapActivity() {
        Intent intent = new Intent(activity, MapActivity.class);
        activity.startActivity(intent);
    }

}
