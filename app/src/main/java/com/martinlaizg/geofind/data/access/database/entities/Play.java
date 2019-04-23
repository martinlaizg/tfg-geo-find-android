package com.martinlaizg.geofind.data.access.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.List;

@Entity(tableName = "plays")
public class Play {

	@PrimaryKey
	@NonNull
	private final Integer id;
	private Integer tour_id;
	private Integer user_id;
	private Date created_at;
	private Date updated_at;

	@Ignore
	private Tour tour;
	@Ignore
	private User user;
	@Ignore
	private List<Place> places;

	public Play(@NonNull Integer id, Integer tour_id, Integer user_id, Date created_at, Date updated_at) {
		this.id = id;
		this.tour_id = tour_id;
		this.user_id = user_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	@Ignore
	public Play() {
		this.id = 0;
	}

	@Ignore
	public Play(int tour_id, int user_id) {
		this.id = 0;
		this.tour_id = tour_id;
		this.user_id = user_id;
	}

	@NotNull
	public Integer getId() {
		return id;
	}

	public Integer getTour_id() {
		return tour_id;
	}

	public void setTour_id(Integer tour_id) {
		this.tour_id = tour_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Tour getTour() {
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour_id = tour.getId();
		this.tour = tour;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user_id = user.getId();
		this.user = user;
	}

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}
}
