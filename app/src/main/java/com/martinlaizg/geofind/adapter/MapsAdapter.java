package com.martinlaizg.geofind.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.activity.GoogleMapsActivity;
import com.martinlaizg.geofind.entity.Maps;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.MapsViewHolder> {

    private ArrayList<Maps> maps;
    private Context context;

    public MapsAdapter(Context context, ArrayList<Maps> maps) {
        this.maps = maps;
        this.context = context;
    }

    @NonNull
    @Override
    public MapsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_maps, viewGroup, false);
        return new MapsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapsViewHolder holder, final int i) {
        final Maps map = maps.get(i);
        holder.mapName.setText(map.getName());
        holder.mapCreator.setText(map.getCreator().getUsername());
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoogleMapsActivity.class);
                intent.putExtra(GoogleMapsActivity.MAP_ID_KEY, map.getId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    class MapsViewHolder extends RecyclerView.ViewHolder {

        TextView mapName;
        TextView mapCreator;
        MaterialCardView materialCardView;

        MapsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mapName = itemView.findViewById(R.id.map_name);
            this.mapCreator = itemView.findViewById(R.id.map_creator);
            this.materialCardView = itemView.findViewById(R.id.map_list_item);
        }
    }

}
