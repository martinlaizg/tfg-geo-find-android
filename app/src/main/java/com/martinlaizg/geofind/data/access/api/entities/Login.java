package com.martinlaizg.geofind.data.access.api.entities;

import com.google.gson.annotations.SerializedName;

public class Login {

	public enum Provider {
		@SerializedName("own") OWN,
		@SerializedName("google") GOOGLE
	}


	private final String name;
	private final String email;
	private final String username;
	private final String password;
	private final Provider provider;

	/**
	 * For registry action
	 *
	 * @param name     the name
	 * @param email    the email
	 * @param username the username
	 * @param password the password
	 * @param provider the login type
	 */
	public Login(String name, String email, String username, String password, Provider provider) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
		this.provider = provider;
	}

	/**
	 * For login action
	 *
	 * @param email    the email
	 * @param password the password
	 * @param provider the login type
	 */
	public Login(String email, String password, Provider provider) {
		this.name = "";
		this.email = email;
		this.username = "";
		this.password = password;
		this.provider = provider;
	}

	public String getEmail() {
		return email;
	}

}
