package com.lvivbus.ui.selectbus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.Bus;

import java.util.ArrayList;
import java.util.List;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.ViewHolder> {
    private List<Bus> filterList;
    private List<Bus> originList;
    private Listener listener;

    public BusListAdapter(Listener listener) {
        this.filterList = new ArrayList<Bus>();
        this.listener = listener;
        this.originList = new ArrayList<Bus>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Bus bus = filterList.get(i);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBusClicked(bus);
            }
        });

        viewHolder.txtTitle.setText(bus.getName());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }


    public void setData(@NonNull List<Bus> busList) {
        this.originList.addAll(busList);
        this.filterList.addAll(busList);
    }

    public void filter(String newText) {
        filterList.clear();
        for (Bus bus : originList) {
            String busName= bus.getName().toLowerCase();
            if (busName.contains(newText.toLowerCase())) {
                filterList.add(bus);
            }
        }
        this.notifyDataSetChanged();

    }

    public interface Listener {
        void onBusClicked(Bus bus);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
        }
    }

}