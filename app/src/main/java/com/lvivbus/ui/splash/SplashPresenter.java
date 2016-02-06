package com.lvivbus.ui.splash;

import android.content.Intent;
import com.lvivbus.model.db.BusDAO;
import com.lvivbus.model.http.Converter;
import com.lvivbus.model.http.LvivBusAPI;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.map.MapActivity;
import com.lvivbus.utils.L;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SplashPresenter {

    private SplashActivity activity;

    public void onAttachActivity(SplashActivity mapActivity) {
        this.activity = mapActivity;

        if (BusDAO.getAllCount() == 0) {
            loadData();
        } else {
            launchMapActivity();
            activity.finish();
        }
    }

    public void onDetachActivity() {
        activity = null;
    }

    private void loadData() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Bus> busList = Converter.toBusList(LvivBusAPI.getBusList());
                BusDAO.save(busList);
                L.v(String.format("Buses saved: %d", busList.size()));
                onLoadingComplete();
            }
        });
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
