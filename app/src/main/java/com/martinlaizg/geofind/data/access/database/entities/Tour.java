package com.martinlaizg.geofind.data.access.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.martinlaizg.geofind.data.enums.PlayLevel;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tours")
public class Tour {

	@PrimaryKey
	private final Integer id;
	private String name;
	private String description;
	private PlayLevel min_level;
	private Date created_at;
	private Date updated_at;
	private Integer creator_id;

	@Ignore
	private List<Place> places;
	@Ignore
	private User creator;

	public Tour(Integer id, String name, String description, PlayLevel min_level, Date created_at, Date updated_at, Integer creator_id) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.min_level = min_level;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.creator_id = creator_id;
	}

	@Ignore
	public Tour() {
		id = 0;
		name = "";
		description = "";
		places = new ArrayList<>();
	}

	@NotNull
	public Integer getId() {
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

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}


	//-------------------------


	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public boolean isValid() {
		for (Place p : getPlaces()) {
			if (!p.isValid()) return false;
		}
		return getName() != null && !getName().isEmpty() && //
				getDescription() != null && !getDescription().isEmpty() && //
				getMin_level() != null;
	}
}
