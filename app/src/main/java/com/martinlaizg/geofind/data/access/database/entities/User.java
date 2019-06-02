package com.martinlaizg.geofind.data.access.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.data.enums.UserType;
import com.martinlaizg.geofind.utils.DateUtils;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Entity(tableName = "users", indices = {@Index({"id"})})
public class User {

	@PrimaryKey
	private final int id;
	private String email;
	private String username;
	private String name;
	private String image;
	private UserType user_type;
	private Date created_at;
	private Date updated_at;
	private Date updated;

	@Ignore
	private String secure;

	@Ignore
	private List<Place> createdPlaces;

	public User(Integer id, String email, String username, String name, UserType user_type,
			Date created_at, Date updated_at) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.name = name;
		this.user_type = user_type;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	@Ignore
	public User() {
		id = 0;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserType getUser_type() {
		return user_type;
	}

	public void setUser_type(UserType user_type) {
		this.user_type = user_type;
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

	public String getSecure() {
		return secure;
	}

	public void setSecure(String secure) {
		this.secure = secure;
	}

	public String toJson() {
		Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
		return gson.toJson(this);
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
