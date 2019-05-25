package com.martinlaizg.geofind.data.access.api.entities;

import com.google.gson.annotations.SerializedName;
import com.martinlaizg.geofind.data.Crypto;

public class Login {

	public enum Provider {
		@SerializedName("own") OWN,
		@SerializedName("google") GOOGLE
	}


	private final String email;
	private final String password;
	private final Provider provider;
	private final String token;

	/**
	 * Create login for own login/registry action (with email and password)
	 *
	 * @param email
	 * 		the email
	 * @param password
	 * 		the password
	 */
	public Login(String email, String password) {
		this.email = email;
		this.password = Crypto.hash(password);
		this.token = "";
		this.provider = Provider.OWN;
	}

	/**
	 * Create login for login/registry action with provider
	 *
	 * @param email
	 * 		the user email
	 * @param token
	 * 		the user token
	 * @param provider
	 * 		the user provider
	 */
	public Login(String email, String token, Provider provider) {
		this.password = "";
		this.email = email;
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
