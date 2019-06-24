package com.martinlaizg.geofind.data.access.api.entities;

import com.martinlaizg.geofind.data.access.database.entities.User;

public class Token {

	private String token;

	private User user;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
