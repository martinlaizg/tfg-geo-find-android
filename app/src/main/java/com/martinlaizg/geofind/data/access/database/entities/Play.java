package com.martinlaizg.geofind.data.access.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.martinlaizg.geofind.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "plays", foreignKeys = {
		@ForeignKey(entity = Tour.class, parentColumns = "id", childColumns = "tour_id",
		            onDelete = CASCADE),
		@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id",
		            onDelete = CASCADE)},
        indices = {@Index({"tour_id", "user_id"}), @Index("user_id")})
public class Play {

	@PrimaryKey
	@NonNull
	private final Integer id;
	private Integer tour_id;
	private Integer user_id;
	private Date created_at;
	private Date updated_at;
	private Date updated;

	@Ignore
	private Tour tour;
	@Ignore
	private User user;
	@Ignore
	private List<Place> places;

	public Play(@NonNull Integer id, Integer tour_id, Integer user_id, Date created_at,
			Date updated_at, Date updated) {
		this.id = id;
		this.tour_id = tour_id;
		this.user_id = user_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.updated = updated;
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

	public boolean isOutOfDate() {
		return DateUtils.isDateExpire(updated);
	}

	public Date getUpdated() {
		return new Date(Calendar.getInstance().getTime().getTime());
	}

	public void setUpdated(Date updated) {
		if(updated == null) {
			this.updated = new Date(Calendar.getInstance().getTime().getTime());
		}
		this.updated = updated;
	}
}
