package com.martinlaizg.geofind.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.enums.PlayLevel;
import com.martinlaizg.geofind.views.fragment.LocationFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationListAdapter
		extends RecyclerView.Adapter<LocationListAdapter.LocationsViewHolder> {

	private List<Location> locations;
	private PlayLevel playLevel;

	public LocationListAdapter() {
		locations = new ArrayList<>();
	}

	@NonNull
	@Override
	public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_location_item, viewGroup, false);
		return new LocationsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull LocationsViewHolder holder, int position) {
		Location l = locations.get(position);
		holder.location_name.setText(l.getName());
		holder.location_description.setText(l.getDescription());
		Bundle b = new Bundle();
		b.putString(LocationFragment.LOC_ID, l.getId());
		holder.location_card.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navLocation, b));
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return locations.size();
	}

	public void setPlayLevel(PlayLevel playLevel) {
		this.playLevel = playLevel;
		notifyDataSetChanged();
	}

	class LocationsViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.location_name)
		TextView location_name;
		@BindView(R.id.location_description)
		TextView location_description;
		@BindView(R.id.location_card)
		CardView location_card;

		LocationsViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
