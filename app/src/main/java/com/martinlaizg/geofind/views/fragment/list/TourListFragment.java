package com.martinlaizg.geofind.views.fragment.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.enums.UserType;
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

	@BindView(R.id.swipe_refresh)
	SwipeRefreshLayout swipe_refresh;

	private TourListViewModel viewModel;
	private TourListAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater,
			@Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_tour_list, container, false);
		ButterKnife.bind(this, view);

		tour_list.setLayoutManager(new LinearLayoutManager(requireActivity()));
		adapter = new TourListAdapter();
		tour_list.setAdapter(adapter);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(this).get(TourListViewModel.class);
		swipe_refresh.setRefreshing(true);
		refreshTours();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
		User u = Preferences.getLoggedUser(sp);
		if(u.getUser_type() != null && u.getUser_type() != UserType.USER) {
			create_tour_button
					.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCreator));
		} else {
			create_tour_button.setOnClickListener(v -> Toast
					.makeText(requireContext(), getString(R.string.no_permissions),
					          Toast.LENGTH_SHORT).show());
		}
		swipe_refresh.setOnRefreshListener(this::refreshTours);
	}

	private void refreshTours() {
		viewModel.getTours().observe(this, tours -> {
			swipe_refresh.setRefreshing(false);
			if(tours != null) {
				adapter.setTours(tours);
			} else {
				APIException error = viewModel.getError();
				if(error.getType() == ErrorType.NETWORK) {
					Toast.makeText(requireContext(),
					               getResources().getString(R.string.network_error),
					               Toast.LENGTH_SHORT).show();
				} else {
					Log.e(TAG, "onCreateView: ", error);
				}
			}
		});
	}
}
