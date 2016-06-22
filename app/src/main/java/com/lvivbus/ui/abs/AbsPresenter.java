package com.lvivbus.ui.abs;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class AbsPresenter<T extends AbsActivity> {

    protected T activity;

    protected abstract void initPresenter(@Nullable Bundle savedInstanceState);

    public AbsPresenter(T activity) {
        this.activity = activity;
    }

    protected void onDestroyActivity() {
        if (activity != null) {
            activity = null;
        }
    }
}