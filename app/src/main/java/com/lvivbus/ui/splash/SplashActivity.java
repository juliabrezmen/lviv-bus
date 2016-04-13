package com.lvivbus.ui.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.lvivbus.model.event.NetworkChangedEvent;
import com.lvivbus.ui.R;
import org.greenrobot.eventbus.Subscribe;

public class SplashActivity extends AppCompatActivity {

    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        presenter = new SplashPresenter();
        presenter.onAttachActivity(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetachActivity();
        super.onDestroy();
    }

    public void showMessage(String message) {
        TextView txtMessage = (TextView) findViewById(R.id.txt_message);
        txtMessage.setText(message);
    }

    @Subscribe
    public void onEvent(NetworkChangedEvent event) {
        presenter.onEvent(event);
    }
}
