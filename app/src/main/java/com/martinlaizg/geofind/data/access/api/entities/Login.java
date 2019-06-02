package com.martinlaizg.geofind.data.access.api.entities;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.martinlaizg.geofind.data.access.database.entities.User;

public class Login {

	public enum Provider {
		@SerializedName("own") OWN,
		@SerializedName("google") GOOGLE
	}


	private final String email;
	private final Provider provider;
	private String secure;
	private User user;

	/**
	 * Create login for own login/registry action (with email and secure)
	 *
	 * @param email
	 * 		the email
	 * @param secure
	 * 		the secure
	 */
	public Login(String email, String secure) {
		this.email = email;
		this.secure = secure;
		this.provider = Provider.OWN;
	}

	/**
	 * Create login for login/registry action with provider
	 *
	 * @param email
	 * 		the user email
	 * @param secure
	 * 		the user secure
	 * @param provider
	 * 		the user provider
	 */
	public Login(String email, String secure, Provider provider) {
		this.email = email;
		this.secure = secure;
		this.provider = provider;
	}

	public String getEmail() {
		return email;
	}

	public String getSecure() {
		return secure;
	}

	public void setSecure(String secure) {
		this.secure = secure;
	}

	public Provider getProvider() {
		return provider;
	}

	public String toJson() {
		return new GsonBuilder().create().toJson(this);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
