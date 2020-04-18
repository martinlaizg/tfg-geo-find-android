package com.martinlaizg.geofind.data.access.api.entities

import com.google.gson.GsonBuilder
import com.martinlaizg.geofind.data.access.database.entities.User

class Login constructor(private val email: String?,
                        private val secure: String?,
                        private val provider: Provider? = Provider.OWN) {
	enum class Provider {
		OWN, GOOGLE
	}

	var user: User? = null

	val json: String
		get() = GsonBuilder().create().toJson(this)

}