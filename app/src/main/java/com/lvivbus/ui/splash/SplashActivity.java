package com.lvivbus.ui.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.lvivbus.ui.R;

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
}
