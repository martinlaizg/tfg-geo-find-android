package com.martinlaizg.geofind.data.access.database.entities;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "places", foreignKeys = @ForeignKey(entity = TourEntity.class, parentColumns = "id", childColumns = "tour_id", onDelete = CASCADE), indices = @Index("tour_id"))
public class PlaceEntity {

	@PrimaryKey
	@NonNull
	private final Integer id;
	private String name;
	private Float lat;
	private Float lon;
	private String tour_id;
	private String description;
	private Integer order;
	private Date created_at;
	private Date updated_at;

	public PlaceEntity(@NonNull Integer id, String name, Float lat, Float lon, String tour_id, Date created_at, Date updated_at) {
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		this.tour_id = tour_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	@Ignore
	public PlaceEntity() {
		id = 0;
	}

	@NonNull
	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public String getTour_id() {
		return tour_id;
	}

	public void setTour_id(String tour_id) {
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

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
