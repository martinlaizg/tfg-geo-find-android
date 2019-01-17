package com.martinlaizg.geofind.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.ItemClickListener;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.entity.Maps;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.MapsViewHolder> {

    private ArrayList<Maps> maps;
    private ItemClickListener itemClickListener;

    public MapsAdapter(ArrayList<Maps> maps) {
        this.maps = maps;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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
        holder.setItemClickListener(itemClickListener);
        mapName.setText(maps.get(i).getName());
        mapCreator.setText(maps.get(i).getCreator().getUsername());

    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    class MapsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mapName;
        TextView mapCreator;
        ItemClickListener itemClickListener;

        MapsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mapName = itemView.findViewById(R.id.map_name);
            this.mapCreator = itemView.findViewById(R.id.map_creator);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

}
