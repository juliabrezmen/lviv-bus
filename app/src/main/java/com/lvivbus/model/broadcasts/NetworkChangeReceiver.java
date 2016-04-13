package com.lvivbus.model.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.lvivbus.model.http.Internet;
import com.lvivbus.utils.L;
import org.greenrobot.eventbus.EventBus;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        L.i("Broadcast from manifest received");
        EventBus.getDefault().post(Internet.isOn(context));
    }
}

