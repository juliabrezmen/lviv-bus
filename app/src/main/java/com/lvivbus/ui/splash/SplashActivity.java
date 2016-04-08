package com.lvivbus.ui.splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.lvivbus.ui.R;

public class SplashActivity extends AppCompatActivity {

    public static final String ACTION_CONNECTION_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private SplashPresenter presenter;
    private NetworkChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        presenter = new SplashPresenter();
        presenter.onAttachActivity(this);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, new IntentFilter(ACTION_CONNECTION_CHANGE));
    }

    @Override
    protected void onDestroy() {
        presenter.onDetachActivity();
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void showMessage(String message) {
        TextView txtMessage = (TextView) findViewById(R.id.txt_message);
        txtMessage.setText(message);
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            presenter.onReceiveBroadcast();
        }
    }
}
