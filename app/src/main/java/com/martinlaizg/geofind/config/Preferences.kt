package com.martinlaizg.geofind.config

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.utils.DateUtils

object Preferences {
	const val USER = "user"
	const val TOKEN = "token"
	private const val LOGIN = "login"
	fun getLoggedUser(sp: SharedPreferences?): User {
		val userString = sp!!.getString(USER, "")
		val gson = GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create()
		return gson.fromJson(userString, User::class.java)
	}

	fun setLoggedUser(sp: SharedPreferences?, user: User) {
		val userString = user.toJson()
		sp!!.edit().putString(USER, userString).apply()
	}

	fun logout(sp: SharedPreferences) {
		sp.edit().putString(USER, null).apply()
		sp.edit().putString(LOGIN, null).apply()
	}

	fun setLogin(sp: SharedPreferences?, login: Login?) {
		val loginString = login!!.toJson()
		sp!!.edit().putString(LOGIN, loginString).apply()
	}

	fun getLogin(sp: SharedPreferences?): Login {
		val loginString = sp!!.getString(LOGIN, "")
		val gson = GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create()
		return gson.fromJson(loginString, Login::class.java)
	}

	fun setToken(sp: SharedPreferences?, token: String?) {
		sp!!.edit().putString(TOKEN, token).apply()
	}

	fun getToken(sp: SharedPreferences): String? {
		return sp.getString(TOKEN, "")
	}
}