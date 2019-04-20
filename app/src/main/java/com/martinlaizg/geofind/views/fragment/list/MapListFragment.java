package com.martinlaizg.geofind.views.fragment.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.TourListAdapter;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.views.viewmodel.MapListViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapListFragment
		extends Fragment {

	private static final String TAG = MapListFragment.class.getSimpleName();

	@BindView(R.id.map_list)
	RecyclerView recyclerView;

	@BindView(R.id.create_map_button)
	FloatingActionButton create_map_button;

	private MapListViewModel viewModel;
	private TourListAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(
			@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_tour_list, container, false);
		ButterKnife.bind(this, view);

		recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
		adapter = new TourListAdapter();
		recyclerView.setAdapter(adapter);

		viewModel = ViewModelProviders.of(this).get(MapListViewModel.class);
		viewModel.getTours().observe(this, tours -> {
			if (tours != null) {
				adapter.setTours(tours);
			} else {
				APIException error = viewModel.getError();
				Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		create_map_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCreator));

		return view;
	}
}
