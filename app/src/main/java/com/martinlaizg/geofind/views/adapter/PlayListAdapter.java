package com.martinlaizg.geofind.views.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.views.fragment.single.TourFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayListAdapter
		extends RecyclerView.Adapter<PlayListAdapter.PlaysViewHolder> {

	private List<Play> plays = new ArrayList<>();
	private Context context;

	@NonNull
	@Override
	public PlaysViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.fragment_play_item, viewGroup, false);
		return new PlaysViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull PlaysViewHolder holder, final int i) {
		final Play play = plays.get(i);
		Tour t = play.getTour();
		holder.tour_name.setText(t.getName());
		holder.tour_creator.setText(t.getCreator().getUsername());
		holder.tour_description.setText(t.getDescription());

		if(t.getImage() != null && !t.getImage().isEmpty()) {
			Picasso.with(context).load(t.getImage()).into(holder.tour_image);
		} else {
			holder.tour_image.setImageResource(R.drawable.default_map_image);
		}
		int completed = play.getPlaces().size();
		int numPlaces = t.getPlaces().size();
		float progress = completed / (float) numPlaces * 100;
		holder.tour_progress.setProgress((int) progress, true);
		holder.tour_progress_text.setText(context.getString(R.string.div, completed, numPlaces));

		Bundle b = new Bundle();
		b.putInt(TourFragment.TOUR_ID, plays.get(i).getId());
		holder.tour_card
				.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.toTour, b));
	}

	@Override
	public int getItemCount() {
		return plays.size();
	}

	public void setPlays(List<Play> plays) {
		this.plays = plays;
		notifyDataSetChanged();
	}

	class PlaysViewHolder
			extends RecyclerView.ViewHolder {

		@BindView(R.id.tour_image)
		ImageView tour_image;
		@BindView(R.id.tour_name)
		TextView tour_name;
		@BindView(R.id.tour_creator)
		TextView tour_creator;
		@BindView(R.id.tour_description)
		TextView tour_description;
		@BindView(R.id.tour_card)
		MaterialCardView tour_card;

		@BindView(R.id.tour_progress)
		ProgressBar tour_progress;
		@BindView(R.id.tour_progress_text)
		TextView tour_progress_text;

		PlaysViewHolder(@NonNull View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
