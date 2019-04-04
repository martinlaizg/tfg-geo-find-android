package com.martinlaizg.geofind.data.access.database.entity;

import com.martinlaizg.geofind.data.access.database.entity.enums.PlayLevel;
import com.martinlaizg.geofind.data.access.retrofit.error.APIError;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "maps")
public class Map {

	@PrimaryKey
	@NonNull
	private final String id;
	private String name;
	private String description;
	private PlayLevel min_level;
	private Date created_at;
	private Date updated_at;
	private String creator_id;

	@Ignore
	private APIError error;

	public Map(@NotNull String id, String name, String description, PlayLevel min_level, Date created_at, Date updated_at, String creator_id) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.min_level = min_level;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.creator_id = creator_id;
	}

	@Ignore
	public Map() {
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

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
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


	public APIError getError() {
		return error;
	}

	public void setError(APIError error) {
		this.error = error;
	}
}
