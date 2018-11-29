package com.martinlaizg.geofind.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.entity.Map;

import java.util.List;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapViewHolder> {

    private List<Map> maps;

    public MapAdapter(List<Map> maps) {
        this.maps = maps;
    }

    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.map_list_item, parent, false);
        return new MapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapViewHolder holder, int position) {
        holder.name.setText(maps.get(position).getName());
        holder.country.setText(maps.get(position).getCountry());
        holder.state.setText(maps.get(position).getState());
        holder.city.setText(maps.get(position).getCity());
        holder.min_level.setText(maps.get(position).getMin_level().getText());
        holder.created_at.setText(maps.get(position).getCreated_at());
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }


    class MapViewHolder extends RecyclerView.ViewHolder {

        TextView name, country, state, city, min_level, created_at;

        MapViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.map_name);
            city =  itemView.findViewById(R.id.map_city);
//            country =  itemView.findViewById(R.id.map_country);
//            state =  itemView.findViewById(R.id.map_state);
//            min_level =  itemView.findViewById(R.id.map_min_level);
//            created_at =  itemView.findViewById(R.id.map_created_date);
        }
    }
}