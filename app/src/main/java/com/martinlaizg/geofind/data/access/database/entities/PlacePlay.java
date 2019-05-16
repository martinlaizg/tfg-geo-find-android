package com.martinlaizg.geofind.data.access.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.martinlaizg.geofind.utils.DateUtils;

import java.sql.Date;
import java.util.Calendar;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "place_play", primaryKeys = {"play_id", "place_id"}, foreignKeys = {
		@ForeignKey(entity = Play.class, parentColumns = "id", childColumns = "play_id",
		            onDelete = CASCADE),
		@ForeignKey(entity = Place.class, parentColumns = "id", childColumns = "place_id",
		            onDelete = CASCADE)}, indices = @Index({"place_id", "play_id"}))
public class PlacePlay {

	@NonNull
	private Integer place_id;
	@NonNull
	private Integer play_id;
	@NonNull
	private Date created_at;
	@NonNull
	private Date updated_at;
	private Date updated;

	public PlacePlay(@NonNull Integer place_id, @NonNull Integer play_id) {
		this.play_id = play_id;
		this.place_id = place_id;
		this.created_at = new Date(Calendar.getInstance().getTime().getTime());
		this.updated_at = new Date(Calendar.getInstance().getTime().getTime());
	}

	@NonNull
	public Integer getPlay_id() {
		return play_id;
	}

	public void setPlay_id(@NonNull Integer play_id) {
		this.play_id = play_id;
	}

	@NonNull
	public Integer getPlace_id() {
		return place_id;
	}

	public void setPlace_id(@NonNull Integer place_id) {
		this.place_id = place_id;
	}

	@NonNull
	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(@NonNull Date created_at) {
		this.created_at = created_at;
	}

	@NonNull
	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(@NonNull Date updated_at) {
		this.updated_at = updated_at;
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

	public boolean isOutOfDate() {
		return DateUtils.isDateExpire(updated);
	}

}
