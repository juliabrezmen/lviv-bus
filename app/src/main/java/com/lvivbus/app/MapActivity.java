package com.lvivbus.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.lvivbus.presenters.MapPresenter;

public class MapActivity extends AppCompatActivity {

    private MapPresenter mapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapPresenter = new MapPresenter();
        mapPresenter.onAttachActivity(this);
    }

    @Override
    protected void onDestroy() {
        mapPresenter.onDetachActivity();
        super.onDestroy();
    }
}
