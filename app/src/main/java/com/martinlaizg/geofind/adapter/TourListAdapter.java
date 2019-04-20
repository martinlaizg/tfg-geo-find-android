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

public class TourListAdapter
		extends RecyclerView.Adapter<TourListAdapter.ToursViewHolder> {

	private List<Tour> tours = new ArrayList<>();

	@NonNull
	@Override
	public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_tour_item, viewGroup, false);
		return new ToursViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ToursViewHolder holder, final int i) {
		final Tour tour = tours.get(i);
		holder.tourName.setText(tour.getName());
		holder.tourCreator.setText(tour.getCreator().getUsername());
		holder.tourDescription.setText(tour.getDescription());

		Bundle b = new Bundle();
		b.putInt(TourFragment.TOUR_ID, tours.get(i).getId());
		holder.materialCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.toTour, b));
	}

	@Override
	public int getItemCount() {
		return tours.size();
	}

	public void setTours(List<Tour> tours) {
		this.tours = tours;
		notifyDataSetChanged();
	}

	class ToursViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.tour_name)
		TextView tourName;
		@BindView(R.id.tour_creator)
		TextView tourCreator;
		@BindView(R.id.tour_description)
		TextView tourDescription;
		@BindView(R.id.tour_card)
		MaterialCardView materialCardView;

		ToursViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
