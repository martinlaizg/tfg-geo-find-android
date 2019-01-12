package com.martinlaizg.geofind.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.entity.Maps;

import java.util.ArrayList;

public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.MapsViewHolder> {

    private ArrayList<Maps> maps;

    public MapsAdapter(ArrayList<Maps> maps) {
        this.maps = maps;
    }

    @NonNull
    @Override
    public MapsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.map_list_item, viewGroup, false);

        return new MapsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapsViewHolder holder, int i) {
        TextView mapName = holder.mapName;
        TextView mapCreator = holder.mapCreator;

        mapName.setText(maps.get(i).getName());
        mapCreator.setText(maps.get(i).getCreator().getUsername());

    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public class MapsViewHolder extends RecyclerView.ViewHolder {

        TextView mapName;
        TextView mapCreator;

        public MapsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mapName = itemView.findViewById(R.id.map_name);
            this.mapCreator = itemView.findViewById(R.id.map_creator);
        }
    }
}
