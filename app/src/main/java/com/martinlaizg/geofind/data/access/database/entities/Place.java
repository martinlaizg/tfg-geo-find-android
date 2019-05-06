package com.martinlaizg.geofind.data.access.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.martinlaizg.geofind.utils.DateUtils;

import java.sql.Date;
import java.util.Calendar;

@Entity(tableName = "places", foreignKeys = @ForeignKey(entity = Tour.class, parentColumns = "id",
                                                        childColumns = "tour_id"),
        indices = @Index("tour_id"))
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
	private Date updated;

	public Place(Integer id, String name, Double lat, Double lon, Integer tour_id,
			String description, Integer order, Date created_at, Date updated_at, Date updated) {
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		this.tour_id = tour_id;
		this.description = description;
		this.order = order;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.updated = updated;
	}

	@Ignore
	public Place() {
		id = 0;
	}

	public Integer getId() {
		return id;
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

	public LatLng getPosition() {
		return new LatLng(getLat(), getLon());
	}

	public Double getLat() {
		return lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setPosition(LatLng position) {
		setLat(position.latitude);
		setLon(position.longitude);
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	boolean isValid() {
		return getName() != null && !getName().isEmpty() && getDescription() != null &&
				!getDescription().isEmpty() && getLat() != null && getLon() != null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
