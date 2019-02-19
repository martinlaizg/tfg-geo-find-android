package com.martinlaizg.geofind.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.dataAccess.database.entity.Location;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private final ArrayList<Location> locations;

    public LocationAdapter(final ArrayList<Location> locations) {
        this.locations = locations;
    }


    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_location, viewGroup, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationViewHolder holder, final int i) {
//        holder.setItemClickListener(itemClickListener);
        holder.locationName.setText(locations.get(i).getName());
        // TODO: cambiar "300m" por la distancia real
        holder.locationDistance.setText("300m");

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView locationName;
        TextView locationDistance;

        LocationViewHolder(@NonNull final View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.location_name);
            locationDistance = itemView.findViewById(R.id.location_distance);
        }

    }

}
