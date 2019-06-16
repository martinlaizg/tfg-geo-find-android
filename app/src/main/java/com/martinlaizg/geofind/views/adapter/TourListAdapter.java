package com.martinlaizg.geofind.views.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.views.fragment.single.TourFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TourListAdapter
		extends RecyclerView.Adapter<TourListAdapter.ToursViewHolder> {

	private List<Tour> tours = new ArrayList<>();
	private Context context;

	@NonNull
	@Override
	public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.fragment_tour_item, viewGroup, false);
		return new ToursViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ToursViewHolder holder, final int i) {
		final Tour tour = tours.get(i);
		holder.tourName.setText(tour.getName());
		holder.tourCreator.setText(tour.getCreator().getUsername());
		holder.tourDescription.setText(tour.getDescription());

		if(tour.getImage() != null && !tour.getImage().isEmpty()) {
			Picasso.with(context).load(tour.getImage()).into(holder.tour_image);
		} else {
			holder.tour_image.setImageResource(R.drawable.default_map_image);
		}

		Bundle b = new Bundle();
		b.putInt(TourFragment.TOUR_ID, tours.get(i).getId());
		holder.materialCardView
				.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.toTour, b));
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

		@BindView(R.id.tour_image)
		ImageView tour_image;
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
