package com.martinlaizg.geofind.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.fragment.single.LocationFragment;

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

	private List<Place> locationEntities;

	public LocationListAdapter() {
		locationEntities = new ArrayList<>();
	}

	@NonNull
	@Override
	public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_place_item, viewGroup, false);
		return new LocationsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull LocationsViewHolder holder, int position) {
		Place l = locationEntities.get(position);
		holder.location_name.setText(l.getName());
		holder.location_description.setText(l.getDescription());
		Bundle b = new Bundle();
		b.putInt(LocationFragment.PLACE_ID, l.getId());
		holder.location_card.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navPlace, b));
	}

	public void setLocationEntities(List<Place> locationEntities) {
		this.locationEntities = locationEntities;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return locationEntities.size();
	}


	class LocationsViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.location_name)
		TextView location_name;
		@BindView(R.id.location_description)
		TextView location_description;
		@BindView(R.id.place_card)
		CardView location_card;

		LocationsViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
