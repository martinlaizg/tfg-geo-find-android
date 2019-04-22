package com.martinlaizg.geofind.data.access.database.entities.relations;

import androidx.room.Relation;

import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;

import java.sql.Date;
import java.util.List;

public class Play {

	@Relation(parentColumn = "tour_id", entityColumn = "id")
	private Tour tour;

	@Relation(parentColumn = "user_id", entityColumn = "id")
	private User user;

	@Relation(parentColumn = "id", entityColumn = "play_id")
	private List<PlayPlaces> places;

	private Date created_at;
	private Date updated_at;

}
