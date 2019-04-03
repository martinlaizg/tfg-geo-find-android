package com.martinlaizg.geofind.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.views.fragment.creator.CreateLocationFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CreatorLocationAdapter
		extends RecyclerView.Adapter<CreatorLocationAdapter.CreatorLocationViewHolder> {

	private List<Location> locations;

	public CreatorLocationAdapter() {
		locations = new ArrayList<>();
	}

	@NotNull
	@Override
	public CreatorLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_createdlocation, parent, false);
		return new CreatorLocationViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final CreatorLocationViewHolder holder, int position) {
		holder.locName.setText(locations.get(position).getName());
		holder.deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				locations.remove(position);
				notifyDataSetChanged();
			}
		});
		Bundle b = new Bundle();
		b.putInt(CreateLocationFragment.LOC_POSITION, position);
		holder.location_card.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCreateLocation, b));
	}

	@Override
	public int getItemCount() {
		return locations.size();
	}

	public void setLocations(List<Location> locs) {
		if (locs != null) {
			// sort elements
			locs.sort((o1, o2) -> o1.getPosition() > o2.getPosition() ?
					1 :
					o1.getPosition() < o2.getPosition() ?
							-1 :
							0);
			locations = locs;
			notifyDataSetChanged();
		}
	}

	class CreatorLocationViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.location_card)
		MaterialCardView location_card;
		@BindView(R.id.delete_loc_bbutton)
		MaterialButton deleteButton;
		@BindView(R.id.loc_name)
		TextView locName;


		CreatorLocationViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}

	}
}
