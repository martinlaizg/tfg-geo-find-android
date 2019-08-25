package com.martinlaizg.geofind.views.fragment.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.enums.UserType;
import com.martinlaizg.geofind.views.adapter.TourListAdapter;
import com.martinlaizg.geofind.views.viewmodel.TourListViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TourListFragment
		extends Fragment {

	private static final String SEARCH_QUERY = "SEARCH_QUERY";
	private static final String TAG = TourListFragment.class.getSimpleName();

	@BindView(R.id.tour_list)
	RecyclerView tour_list;
	@BindView(R.id.create_tour_button)
	FloatingActionButton create_tour_button;
	@BindView(R.id.swipe_refresh)
	SwipeRefreshLayout swipe_refresh;
	@BindView(R.id.no_tours_card)
	MaterialCardView no_tours_card;
	@BindView(R.id.no_tours_card_text)
	TextView no_tours_card_text;

	private TourListViewModel viewModel;
	private TourListAdapter adapter;
	private String stringQuery;

	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater,
			@Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_tour_list, container, false);
		ButterKnife.bind(this, view);
		tour_list.setLayoutManager(new LinearLayoutManager(requireActivity()));
		adapter = new TourListAdapter();
		tour_list.setAdapter(adapter);

		Bundle arguments = getArguments();
		if(arguments != null) {
			stringQuery = arguments.getString(TourListFragment.SEARCH_QUERY, "");
			stringQuery = stringQuery.trim();
			if(!stringQuery.isEmpty()) { // With search
				swipe_refresh.setEnabled(false);
			} else {
				stringQuery = null;
				// Set that this fragment has options in toolbar
				setHasOptionsMenu(true);
			}
		}
		requireActivity().getOnBackPressedDispatcher()
				.addCallback(this, new OnBackPressedCallback(true) {
					@Override
					public void handleOnBackPressed() {
						Objects.requireNonNull(
								((AppCompatActivity) requireActivity()).getSupportActionBar())
								.setSubtitle(null);
					}
				});

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(this).get(TourListViewModel.class);
		swipe_refresh.setRefreshing(true);
		viewModel.getTours(stringQuery).observe(this, this::setTours);

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
		User u = Preferences.getLoggedUser(sp);
		if(u.getUser_type() != null && u.getUser_type() != UserType.USER) {
			create_tour_button.setOnClickListener(
					Navigation.createNavigateOnClickListener(R.id.toCreateTour));
		} else {
			create_tour_button.setOnClickListener(v -> Toast
					.makeText(requireContext(), getString(R.string.no_permissions),
					          Toast.LENGTH_SHORT).show());
		}
		swipe_refresh.setOnRefreshListener(
				() -> viewModel.getTours(stringQuery).observe(this, this::setTours));
	}

	private void setTours(List<Tour> tours) {
		swipe_refresh.setRefreshing(false);
		if(tours != null) {
			if(tours.isEmpty()) {
				swipe_refresh.setVisibility(View.GONE);
				no_tours_card.setVisibility(View.VISIBLE);
				no_tours_card_text.setText(getString(R.string.no_tours_match));
				return;
			}
			adapter.setTours(tours);
		} else {
			ErrorType error = viewModel.getError();
			if(error == ErrorType.NETWORK) {
				Toast.makeText(requireContext(), getResources().getString(R.string.network_error),
				               Toast.LENGTH_SHORT).show();
			} else {
				Log.e(TAG, "onCreateView: " + error);
			}
		}
	}

	@Override
	public void onStart() {
		Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
				.setSubtitle(stringQuery);
		super.onStart();
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		inflater.inflate(R.menu.toolbar_menu, menu);

		SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
		searchView.setIconifiedByDefault(false);
		searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				Bundle args = new Bundle();
				args.putString(SEARCH_QUERY, query);
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.navigate(R.id.searchTours, args);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				adapter.getFilter().filter(newText);
				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if(item.getItemId() == R.id.app_bar_search) {
			swipe_refresh.setEnabled(false);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
