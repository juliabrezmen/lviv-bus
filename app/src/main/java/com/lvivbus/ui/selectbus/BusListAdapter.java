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
    private List<Bus> busList;
    private Listener listener;

    public BusListAdapter(Listener listener) {
        busList = new ArrayList<Bus>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Bus bus = busList.get(i);
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
        return busList.size();
    }


    public void setData(@NonNull List<Bus> busList) {
        this.busList = busList;
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