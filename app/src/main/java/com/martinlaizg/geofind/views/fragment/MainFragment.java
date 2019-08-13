package com.martinlaizg.geofind.views.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.activity.MainActivity;
import com.martinlaizg.geofind.views.adapter.PlayListAdapter;
import com.martinlaizg.geofind.views.viewmodel.MainFragmentViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment
		extends Fragment {

	private static String TAG = MainFragment.class.getSimpleName();

	@BindView(R.id.toursInProgressView)
	RecyclerView toursInProgressView;
	@BindView(R.id.toursCompletedText)
	TextView toursCompletedText;
	@BindView(R.id.toursInProgressText)
	TextView toursInProgressText;

	private User user;
	private PlayListAdapter adapter;
	@SuppressWarnings("FieldCanBeLocal")
	private MainFragmentViewModel viewModel;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_main, container, false);
		ButterKnife.bind(this, view);

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
		user = Preferences.getLoggedUser(sp);
		MainActivity mainActivity = (MainActivity) requireActivity();
		if(user == null) {
			// If user is not logged, go to LoginFragment
			mainActivity.setToolbarAndDrawer(false);
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toLogin);
			return null;
		}
		mainActivity.setToolbarAndDrawer(true);

		toursInProgressView.setLayoutManager(new LinearLayoutManager(requireActivity()));
		adapter = new PlayListAdapter();
		toursInProgressView.setAdapter(adapter);

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel.class);
		viewModel.getUserPlays(user.getId()).observe(this, plays -> {
			adapter.setPlays(plays);
			int inProgress = 0;
			int completed = 0;
			for(Play p : plays) {
				if(p.isCompleted()) {
					completed++;
				} else {
					inProgress++;
				}
			}
			toursCompletedText.setText(getResources()
					                           .getQuantityString(R.plurals.you_complete_num_places,
					                                              completed, completed));
			toursInProgressText.setText(getResources()
					                            .getQuantityString(R.plurals.you_progress_tours,
					                                               inProgress, inProgress));
		});
	}
}
