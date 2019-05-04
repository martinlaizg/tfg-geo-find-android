package com.martinlaizg.geofind.views.fragment.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.views.adapter.TourListAdapter;
import com.martinlaizg.geofind.views.viewmodel.TourListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TourListFragment
		extends Fragment {

	private static final String TAG = TourListFragment.class.getSimpleName();

	@BindView(R.id.tour_list)
	RecyclerView tour_list;

	@BindView(R.id.create_tour_button)
	FloatingActionButton create_tour_button;

	private TourListViewModel viewModel;
	private TourListAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_tour_list, container, false);
		ButterKnife.bind(this, view);

		tour_list.setLayoutManager(new LinearLayoutManager(requireActivity()));
		adapter = new TourListAdapter();
		tour_list.setAdapter(adapter);

		viewModel = ViewModelProviders.of(this).get(TourListViewModel.class);
		viewModel.getTours().observe(this, tours -> {
			if(tours != null) {
				adapter.setTours(tours);
			} else {
				APIException error = viewModel.getError();
				Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		create_tour_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCreator));

		return view;
	}
}
