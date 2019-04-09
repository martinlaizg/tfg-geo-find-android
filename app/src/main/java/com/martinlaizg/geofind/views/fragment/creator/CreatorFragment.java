package com.martinlaizg.geofind.views.fragment.creator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.CreatorLocationAdapter;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.fragment.single.MapFragment;
import com.martinlaizg.geofind.views.viewmodel.MapCreatorViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CreatorFragment
		extends Fragment
		implements View.OnClickListener {

	// View
	@BindView(R.id.create_map_name)
	TextView mapName;
	@BindView(R.id.create_map_description)
	TextView mapDescription;

	@BindView(R.id.add_location_button)
	MaterialButton add_location_button;
	@BindView(R.id.create_map_button)
	MaterialButton create_map_button;
	@BindView(R.id.edit_button)
	MaterialButton edit_button;

	@BindView(R.id.rec_view_loc_list)
	RecyclerView recyclerView;

	private MapCreatorViewModel viewModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_creator, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
		CreatorLocationAdapter adapter = new CreatorLocationAdapter();
		recyclerView.setAdapter(adapter);

		add_location_button.setOnClickListener(v -> {
			Bundle b = new Bundle();
			b.putInt(CreateLocationFragment.LOC_POSITION, viewModel.getCreatedLocations().size());
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).navigate(R.id.toCreateLocation, b);
		});
		edit_button.setOnClickListener(v -> {
			viewModel.setEdit(true);
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
		});
		create_map_button.setOnClickListener(this);

		viewModel = ViewModelProviders.of(requireActivity()).get(MapCreatorViewModel.class);
		Map map = viewModel.getCreatedMap();
		if (!map.getId().isEmpty()) {
			create_map_button.setText(R.string.update_map);
		}
		if (!map.getName().isEmpty()) {
			mapName.setText(map.getName());
		}
		if (!map.getDescription().isEmpty()) {
			mapDescription.setText(map.getDescription());
		}

		adapter.setLocations(viewModel.getCreatedLocations());
	}

	@Override
	public void onClick(View v) {
		create_map_button.setEnabled(false);
		viewModel.createMap().observe(this, map -> {
			create_map_button.setEnabled(true);
			if (map == null) {
				// TODO manage error
				APIException err = viewModel.getError();
				Toast.makeText(requireActivity(), err.getType().getMessage(), Toast.LENGTH_LONG).show();
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack(R.id.navMapList, false);
				viewModel.clear();
				return;
			}
			viewModel.clear();
			Toast.makeText(requireActivity(), R.string.map_created, Toast.LENGTH_SHORT).show();
			Bundle b = new Bundle();
			b.putString(MapFragment.MAP_ID, map.getId());
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).navigate(R.id.toNewMap, b);
		});
	}
}
