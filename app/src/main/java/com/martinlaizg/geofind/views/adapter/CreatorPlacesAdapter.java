package com.martinlaizg.geofind.views.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.fragment.creator.CreatePlaceFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatorPlacesAdapter
		extends RecyclerView.Adapter<CreatorPlacesAdapter.CreatorPlacesViewHolder>
		implements ItemTouchHelperAdapter {

	private List<Place> places;
	private FragmentActivity fragmentActivity;

	@NonNull
	@Override
	public CreatorPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_editable_place, parent, false);
		return new CreatorPlacesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final CreatorPlacesViewHolder holder, int position) {
		Place place = places.get(position);

		holder.place_name.setText(place.getName());
		holder.place_delete_button
				.setOnClickListener(v -> showExitDialog(v.getContext(), position));
		holder.questionnaire_icon.setVisibility(View.GONE);
		if(place.getQuestion() != null) {
			holder.questionnaire_icon.setVisibility(View.VISIBLE);
		}
		Bundle b = new Bundle();
		b.putInt(CreatePlaceFragment.PLACE_POSITION, place.getOrder());
		holder.place_card.setOnClickListener(
				v -> Navigation.findNavController(fragmentActivity, R.id.main_fragment_holder)
						.navigate(R.id.toCreatePlace, b));
	}

	@Override
	public int getItemCount() {
		return places.size();
	}

	private void showExitDialog(Context context, int position) {
		new MaterialAlertDialogBuilder(context).setTitle(R.string.are_you_sure)
				.setMessage(context.getString(R.string.permanent_delete))
				.setPositiveButton(context.getString(R.string.ok),
				                   (dialog, which) -> remove(position))
				.setNegativeButton(context.getString(R.string.cancel),
				                   (dialogInterface, i) -> dialogInterface.dismiss()).show();
	}

	private void remove(int position) {
		places.remove(position);
		for(int i = 0; i < places.size(); i++) {
			places.get(i).setOrder(i);
		}
		notifyDataSetChanged();
	}

	@Override
	public void onItemMove(int from, int to) {
		Collections.swap(places, from, to);

		for(int i = 0; i < places.size(); i++) {
			places.get(i).setOrder(i);
		}

		notifyItemMoved(from, to);
		notifyItemChanged(from);
		notifyItemChanged(to);
	}

	public CreatorPlacesAdapter() {
		places = new ArrayList<>();
	}

	public void setPlaces(FragmentActivity fragmentActivity, List<Place> places) {
		this.fragmentActivity = fragmentActivity;
		if(places != null) {
			// sort elements
			places.sort((o1, o2) -> o1.getOrder().compareTo(o2.getOrder()));
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
		@BindView(R.id.questionnaire_icon)
		ImageView questionnaire_icon;

		CreatorPlacesViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

	}
}
