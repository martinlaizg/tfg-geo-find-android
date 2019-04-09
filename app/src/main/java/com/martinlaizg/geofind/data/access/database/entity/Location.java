package com.martinlaizg.geofind.data.access.database.entity;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "locations", foreignKeys = @ForeignKey(entity = Map.class, parentColumns = "id", childColumns = "map_id", onDelete = CASCADE), indices = @Index("map_id"))
public class Location {

	@PrimaryKey
	@NonNull
	private final String id;
	private String name;
	private String lat;
	private String lon;
	private String map_id;
	private String description;
	private Integer position;
	private Date created_at;
	private Date updated_at;

	public Location(@NonNull String id, String name, String lat, String lon, String map_id, Date created_at, Date updated_at) {
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		this.map_id = map_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	@Ignore
	public Location() {
		id = "";
	}

	@NonNull
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
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

	public String getMap_id() {
		return map_id;
	}

	public void setMap_id(String map_id) {
		this.map_id = map_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LatLng getLatLng() {
		return new LatLng(Double.valueOf(getLat()), Double.valueOf(getLon()));
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
}
