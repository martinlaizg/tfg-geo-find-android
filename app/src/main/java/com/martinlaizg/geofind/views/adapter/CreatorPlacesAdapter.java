package com.martinlaizg.geofind.views.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.fragment.creator.CreatePlaceFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatorPlacesAdapter
		extends RecyclerView.Adapter<CreatorPlacesAdapter.CreatorPlacesViewHolder> {

	private List<Place> places;

	@NotNull
	@Override
	public CreatorPlacesViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_editable_place, parent, false);
		return new CreatorPlacesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NotNull final CreatorPlacesViewHolder holder, int position) {
		holder.place_name.setText(places.get(position).getName());
		holder.place_delete_button.setOnClickListener(v -> {
			places.remove(position);
			notifyDataSetChanged();
		});
		Bundle b = new Bundle();
		b.putInt(CreatePlaceFragment.PLACE_POSITION, position);
		holder.place_card.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCreatePlace, b));
	}

	@Override
	public int getItemCount() {
		return places.size();
	}

	public CreatorPlacesAdapter() {
		places = new ArrayList<>();
	}

	public void setPlaces(List<Place> places) {
		if(places != null) {
			// sort elements
			places.sort((o1, o2) -> o1.getOrder() > o2.getOrder() ?
					1 :
					o1.getOrder() < o2.getOrder() ?
							-1 :
							0);
			this.places = places;
			notifyDataSetChanged();
		}
	}

	class CreatorPlacesViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.place_card)
		MaterialCardView place_card;
		@BindView(R.id.place_delete_button)
		MaterialButton place_delete_button;
		@BindView(R.id.place_name)
		TextView place_name;

		CreatorPlacesViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

	}
}
