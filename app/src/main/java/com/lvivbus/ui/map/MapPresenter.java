package com.lvivbus.ui.map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import com.lvivbus.model.http.BusAPI;
import com.lvivbus.model.http.Converter;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.BusMarker;
import com.lvivbus.ui.event.SelectBusEvent;
import com.lvivbus.ui.selectbus.BusListActivity;
import com.lvivbus.utils.L;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapPresenter {

    private MapActivity activity;
    private Bus selectedBus;
    private CountDownTimer timer;
    private AsyncTask<Bus, Void, List<BusMarker>> task;

    public void onAttachActivity(MapActivity mapActivity) {
        activity = mapActivity;
        initTimer();
        EventBus.getDefault().register(this);
    }

    public void onActivityVisible() {
        timer.start();
    }

    public void onActivityNotVisible() {
        cancelMarkerLoading();
    }

    public void onDetachActivity() {
        cancelMarkerLoading();
        activity = null;
        EventBus.getDefault().unregister(this);
    }

    public void onToolbarFilterClicked() {
        Intent intent = new Intent(activity, BusListActivity.class);
        activity.startActivity(intent);
    }

    @Subscribe
    public void onEvent(final SelectBusEvent event) {
        selectedBus = event.getBus();
        activity.setSubtitle(selectedBus.getName());
        activity.clearAllMarkers();
    }

    private void initTimer() {
        timer = new CountDownTimer(TimeUnit.MINUTES.toMillis(1), TimeUnit.SECONDS.toMillis(5)) {
            @Override
            public void onTick(long millisUntilFinished) {
                loadMarkers();
            }

            @Override
            public void onFinish() {
                timer.start();
            }
        };
    }

    private void loadMarkers() {
        if (selectedBus != null) {
            task = new AsyncTask<Bus, Void, List<BusMarker>>() {
                @Override
                protected List<BusMarker> doInBackground(Bus... params) {
                    Bus bus = params[0];
                    L.v(String.format("Loading markers for bus: %s", bus.getName()));
                    return Converter.toBusMarkerList(BusAPI.getBusLocation(bus.getCode()));
                }

                @Override
                protected void onPostExecute(List<BusMarker> markerList) {
                    activity.displayMarkers(markerList);
                }
            };
            task.execute(selectedBus);
        }
    }

    private void cancelMarkerLoading() {
        if (task != null) {
            task.cancel(false);
        }

        if(timer != null) {
            timer.cancel();
        }
    }
}
