package com.martinlaizg.geofind.data.access.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;

@Entity(tableName = "places")
public class Place {

	@PrimaryKey
	private final Integer id;
	private String name;
	private Double lat;
	private Double lon;
	private Integer tour_id;
	private String description;
	private Integer order;
	private Date created_at;
	private Date updated_at;

	public Place(Integer id, String name, Double lat, Double lon, Integer tour_id, Date created_at, Date updated_at) {
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		this.tour_id = tour_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	@Ignore
	public Place() {
		id = 0;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
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

	public Integer getTour_id() {
		return tour_id;
	}

	public void setTour_id(Integer tour_id) {
		this.tour_id = tour_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LatLng getPosition() {
		return new LatLng(getLat(), getLon());
	}

	public void setPosition(LatLng position) {
		setLat(position.latitude);
		setLon(position.longitude);
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}


	boolean isValid() {
		return getName() != null && !getName().isEmpty() && getDescription() != null && !getDescription().isEmpty() && getLat() != null && getLon() != null;
	}
}
