package com.martinlaizg.geofind.views.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.fragment.single.PlaceFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceListAdapter
		extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

	private final int disabled_color;
	private List<Place> places;
	private boolean completed;

	@NonNull
	@Override
	public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.fragment_place_item, viewGroup, false);
		return new PlaceViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
		Place l = places.get(position);
		holder.place_name.setText(l.getName());
		holder.place_description.setText(l.getDescription());
		Bundle b = new Bundle();
		b.putInt(PlaceFragment.PLACE_ID, l.getId());
		holder.place_card
				.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toPlace, b));
		if(completed) {
			holder.place_name.setTextColor(disabled_color);
			holder.place_description.setTextColor(disabled_color);
		}
	}

	@Override
	public int getItemCount() {
		return places.size();
	}

	public PlaceListAdapter(boolean completed, int disabled_color) {
		places = new ArrayList<>();
		this.completed = completed;
		this.disabled_color = disabled_color;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
		notifyDataSetChanged();
	}

	class PlaceViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.place_name)
		TextView place_name;
		@BindView(R.id.place_description)
		TextView place_description;
		@BindView(R.id.place_card)
		CardView place_card;

		PlaceViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
