package com.lvivbus.ui.splash;

import android.widget.TextView;
import com.lvivbus.ui.R;
import com.lvivbus.ui.abs.AbsActivity;

public class SplashActivity extends AbsActivity<SplashPresenter> {

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.splash_activity);
    }

    public void showMessage(String message) {
        TextView txtMessage = (TextView) findViewById(R.id.txt_message);
        txtMessage.setText(message);
    }
}
