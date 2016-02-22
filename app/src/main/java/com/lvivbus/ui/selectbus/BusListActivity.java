package com.lvivbus.ui.selectbus;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.utils.DividerItemDecoration;
import com.lvivbus.utils.L;

import java.util.List;

public class BusListActivity extends AppCompatActivity {
    private BusListAdapter adapter;
    private BusListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_list_activity);

        initToolBar();
        initView();

        presenter = new BusListPresenter();
        presenter.onAttachActivity(this);

    }

    @Override
    protected void onDestroy() {
        presenter.onDetachActivity();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_bus, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                L.i("onQueryTextChange " + newText);
                adapter.filter(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                L.i("onQueryTextSubmit " + query);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                presenter.onToolbarBackClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setData(@NonNull List<Bus> recentList, @NonNull List<Bus> busList) {
        adapter.setData(recentList, busList);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        RecyclerView rvBusList = (RecyclerView) findViewById(R.id.rv_bus_list);
        rvBusList.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, R.drawable.divider);

        int leftPadding = (int) getResources().getDimension(R.dimen.decorator_left_padding);
        int rightPadding = (int) getResources().getDimension(R.dimen.decorator_right_padding);
        dividerItemDecoration.setPadding(leftPadding, rightPadding);
        rvBusList.addItemDecoration(dividerItemDecoration);

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

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
