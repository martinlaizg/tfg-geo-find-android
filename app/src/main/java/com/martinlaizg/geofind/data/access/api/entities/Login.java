package com.martinlaizg.geofind.data.access.api.entities;

import com.google.gson.annotations.SerializedName;
import com.martinlaizg.geofind.data.Crypto;

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
	private final String token;

	/**
	 * For registry action
	 *
	 * @param name     the name
	 * @param email    the email
	 * @param username the username
	 * @param password the password
	 * @param provider the loginProvider type
	 */
	public Login(String name, String email, String username, String password, Provider provider) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
		this.token = "";
		this.provider = provider;
	}

	/**
	 * Create loginProvider for own loginProvider action
	 *
	 * @param email the user email
	 */
	public Login(String email, String password) {
		this.name = "";
		this.username = "";
		this.email = email;
		this.password = Crypto.hash(password);
		this.token = "";
		this.provider = Provider.OWN;
	}

	/**
	 * Create loginProvider for provider loginProvider action
	 *
	 * @param email the user email
	 */
	public Login(String email, String token, Provider provider) {
		this.name = "";
		this.username = "";
		this.email = email;
		this.password = "";
		this.token = token;
		this.provider = provider;
	}

	public String getEmail() {
		return email;
	}

	public Provider getProvider() {
		return provider;
	}
}
