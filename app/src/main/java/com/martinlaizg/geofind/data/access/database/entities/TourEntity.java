package com.martinlaizg.geofind.data.access.database.entities;

import com.martinlaizg.geofind.data.enums.PlayLevel;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tours")//, foreignKeys = @ForeignKey(entity = UserEntity.class, parentColumns = "id", childColumns = "creator_id", onDelete = ForeignKey.CASCADE))
public class TourEntity {

	@PrimaryKey
	@NonNull
	private final String id;
	private String name;
	private String description;
	private PlayLevel min_level;
	private Date created_at;
	private Date updated_at;
	private Integer creator_id;
	@Ignore
	private List<PlaceEntity> placeEntities;
	@Ignore
	private UserEntity creator;

	public TourEntity(@NotNull String id, String name, String description, PlayLevel min_level, Date created_at, Date updated_at, Integer creator_id) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.min_level = min_level;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.creator_id = creator_id;
	}

	@Ignore
	public TourEntity() {
		id = "";
		name = "";
		description = "";
	}

	@NotNull
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(Integer creator_id) {
		this.creator_id = creator_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PlayLevel getMin_level() {
		return min_level;
	}

	public void setMin_level(PlayLevel min_level) {
		this.min_level = min_level;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public List<PlaceEntity> getPlaceEntities() {
		return placeEntities;
	}

	public void setPlaceEntities(List<PlaceEntity> placeEntities) {
		this.placeEntities = placeEntities;
	}

	public UserEntity getCreator() {
		return creator;
	}

	public void setCreator(UserEntity creator) {
		this.creator = creator;
	}
}
