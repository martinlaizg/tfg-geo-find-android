package com.martinlaizg.geofind.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.ItemClickListener;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.entity.Location;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private ArrayList<Location> locations;
    private ItemClickListener itemClickListener;

    public LocationAdapter(ArrayList<Location> locations) {
        this.locations = locations;
    }


    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_location, viewGroup, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int i) {
        holder.setItemClickListener(itemClickListener);
        holder.locationName.setText(locations.get(i).getName());
        // TODO: cambiar "300m" por la distancia real
        holder.locationDistance.setText("300m");

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView locationName;
        TextView locationDistance;
        ItemClickListener itemClickListener;

        LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.locationName = itemView.findViewById(R.id.location_name);
            this.locationDistance = itemView.findViewById(R.id.location_distance);
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
