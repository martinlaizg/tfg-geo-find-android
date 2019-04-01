package com.martinlaizg.geofind.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.fragment.single.MapFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapListAdapter
		extends RecyclerView.Adapter<MapListAdapter.MapsViewHolder> {

	private List<Map> maps = new ArrayList<>();
	private NavController navController;

	@NonNull
	@Override
	public MapsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_map_item, viewGroup, false);
		return new MapsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MapsViewHolder holder, final int i) {
		final Map map = maps.get(i);
		holder.mapName.setText(map.getName());
		holder.mapCreator.setText("Creador " + map.getCreator_id() + " TODO"); // Cambiar por valor real
		holder.mapDescription.setText(map.getDescription());

		Bundle b = new Bundle();
		b.putString(MapFragment.MAP_ID, maps.get(i).getId());
		holder.materialCardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Navigation.findNavController(v).navigate(R.id.toMap, b);
			}
		});
	}

	@Override
	public int getItemCount() {
		return maps.size();
	}

	public void setMaps(List<Map> maps) {
		this.maps = maps;
		notifyDataSetChanged();
	}

	class MapsViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.map_name)
		TextView mapName;
		@BindView(R.id.map_creator)
		TextView mapCreator;
		@BindView(R.id.map_description)
		TextView mapDescription;
		@BindView(R.id.map_list_item)
		MaterialCardView materialCardView;

		MapsViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
