package com.martinlaizg.geofind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinlaizg.geofind.adapter.MapsAdapter;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.model.MapListViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapListFragment extends Fragment {

    private static final String TAG = MapListFragment.class.getSimpleName();

    @BindView(R.id.map_list)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private MapListViewModel mapListViewModel;

    private MapsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_maps_list, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MapsAdapter();
        recyclerView.setAdapter(adapter);

        mapListViewModel = ViewModelProviders.of(this).get(MapListViewModel.class);

        mapListViewModel.getAllMaps().observe(this, new Observer<List<Map>>() {
            @Override
            public void onChanged(List<Map> maps) {
                adapter.setMaps(maps);
                swipeRefresh.setRefreshing(false);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMaps();
            }
        });

        return view;
    }

    private void loadMaps() {

        mapListViewModel.refreshMaps();
    }


}
