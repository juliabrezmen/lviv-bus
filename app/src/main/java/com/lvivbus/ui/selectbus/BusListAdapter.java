package com.lvivbus.ui.selectbus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lvivbus.ui.R;
import com.lvivbus.ui.data.Bus;
import com.lvivbus.ui.data.Title;
import com.lvivbus.ui.utils.SortUtils;

import java.util.ArrayList;
import java.util.List;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.ViewHolder> {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_BUS = 1;
    public static final int TYPE_UNDEFINED = 100;

    private List<Displayable> filterList;
    private List<Displayable> originList;
    private Listener listener;

    public BusListAdapter(Listener listener) {
        this.filterList = new ArrayList<Displayable>();
        this.listener = listener;
        this.originList = new ArrayList<Displayable>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;
        switch (viewType) {
            case TYPE_TITLE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.title_list_item, viewGroup, false);
                break;
            case TYPE_BUS:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_item, viewGroup, false);
                break;
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Displayable displayable = filterList.get(i);

        if (getItemViewType(i) == TYPE_BUS) {
            final Bus bus = (Bus) displayable;
            initBus(viewHolder, bus);
        } else {
            if (getItemViewType(i) == TYPE_TITLE) {
                final Title title = (Title) displayable;
                viewHolder.txtTitle.setText(title.getValue().toUpperCase());
            }
        }
    }

    private void initBus(ViewHolder viewHolder, final Bus bus) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBusClicked(bus);
            }
        });
        viewHolder.txtTitle.setText(bus.getName());
    }

    @Override
    public int getItemViewType(int position) {
        Displayable displayable = filterList.get(position);
        if (displayable instanceof Title) {
            return TYPE_TITLE;
        } else if (displayable instanceof Bus) {
            return TYPE_BUS;
        }
        return TYPE_UNDEFINED;
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }


    public void setData(@NonNull List<Bus> recentList, @NonNull List<Bus> busList) {
        if (!recentList.isEmpty()) {
            Title recentTitle = new Title();
            recentTitle.setValue("Recent");
            this.filterList.add(recentTitle);
            this.filterList.addAll(recentList);
            Title allTitle = new Title();
            allTitle.setValue("All");
            this.filterList.add(allTitle);
        }
        this.filterList.addAll(busList);
        this.originList.addAll(filterList);
    }

    public void filter(@NonNull String text) {
        filterList.clear();
        if (text.isEmpty()) {
            filterList.addAll(originList);
        } else {
            List<Bus> busList = new ArrayList<Bus>();
            for (Displayable displayable : originList) {
                if (displayable instanceof Bus) {
                    Bus bus = (Bus) displayable;
                    String busName = bus.getName().toLowerCase();
                    if (busName.contains(text.toLowerCase())) {
                        busList.add(bus);
                    }
                }
            }
            SortUtils.sortByName(busList);
            filterList.addAll(busList);
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