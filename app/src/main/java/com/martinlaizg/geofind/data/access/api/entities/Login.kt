package com.martinlaizg.geofind.data.access.api.entities

import com.google.gson.GsonBuilder
import com.martinlaizg.geofind.data.access.database.entities.User

class Login {
    enum class Provider {
        OWN, GOOGLE
    }

    val email: String?
    val provider: Provider?
    var secure: String?
    var user: User? = null

    /**
     * Create login for own login/registry action (with email and secure)
     *
     * @param email
     * the email
     * @param secure
     * the secure
     */
    constructor(email: String?, secure: String?) {
        this.email = email
        this.secure = secure
        provider = Provider.OWN
    }

    /**
     * Create login for login/registry action with provider
     *
     * @param email
     * the user email
     * @param secure
     * the user secure
     * @param provider
     * the user provider
     */
    constructor(email: String?, secure: String?, provider: Provider?) {
        this.email = email
        this.secure = secure
        this.provider = provider
    }

    fun toJson(): String {
        return GsonBuilder().create().toJson(this)
    }

}