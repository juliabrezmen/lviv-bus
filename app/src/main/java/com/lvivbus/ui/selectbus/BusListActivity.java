package com.lvivbus.ui.selectbus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.Bus;

import java.util.List;

public class BusListActivity extends AppCompatActivity {
    private BusListAdapter adapter;
    private BusListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_list_activity);
        initView();

        presenter = new BusListPresenter();
        presenter.onAttachActivity(this);

    }

    @Override
    protected void onDestroy() {
        presenter.onDetachActivity();
        super.onDestroy();
    }

    public void setData(@NonNull List<Bus> busList) {
        adapter.setData(busList);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        RecyclerView rvBusList = (RecyclerView) findViewById(R.id.rv_bus_list);
        rvBusList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvBusList.setLayoutManager(layoutManager);

        adapter = new BusListAdapter(new BusListAdapter.Listener() {
            @Override
            public void onBusClicked(Bus bus) {
                presenter.onBusClicked(bus);
            }
        });
        rvBusList.setAdapter(adapter);
    }
}
