package com.martinlaizg.geofind.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationsViewHolder> {

    List<Location> locations;

    @NonNull
    @Override
    public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_location_item, viewGroup, false);
        return new LocationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsViewHolder holder, int position) {
        holder.location_name.setText(locations.get(position).getName());
        holder.location_description.setText(locations.get(position).getDescription());
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class LocationsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.location_name)
        TextView location_name;
        @BindView(R.id.location_description)
        TextView location_description;

        LocationsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
