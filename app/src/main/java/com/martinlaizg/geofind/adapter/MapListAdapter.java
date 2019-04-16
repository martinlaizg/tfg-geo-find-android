package com.martinlaizg.geofind.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.views.fragment.single.TourFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapListAdapter
		extends RecyclerView.Adapter<MapListAdapter.MapsViewHolder> {

	private List<Tour> mapEntities = new ArrayList<>();

	@NonNull
	@Override
	public MapsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_map_item, viewGroup, false);
		return new MapsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MapsViewHolder holder, final int i) {
		final Tour tour = mapEntities.get(i);
		holder.mapName.setText(tour.getName());
		holder.mapCreator.setText(tour.getCreator().getUsername());
		holder.mapDescription.setText(tour.getDescription());

		Bundle b = new Bundle();
		b.putInt(TourFragment.TOUR_ID, mapEntities.get(i).getId());
		holder.materialCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.toTour, b));
	}

	@Override
	public int getItemCount() {
		return mapEntities.size();
	}

	public void setTours(List<Tour> mapEntities) {
		this.mapEntities = mapEntities;
		notifyDataSetChanged();
	}

	class MapsViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.tour_name)
		TextView mapName;
		@BindView(R.id.tour_creator)
		TextView mapCreator;
		@BindView(R.id.tour_description)
		TextView mapDescription;
		@BindView(R.id.map_list_item)
		MaterialCardView materialCardView;

		MapsViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
