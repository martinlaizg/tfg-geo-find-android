package com.martinlaizg.geofind.data.access.api.service.exceptions

import com.martinlaizg.geofind.data.access.api.error.ErrorType

/**
 * Exception from API
 */
class APIException : Throwable {
    val type: ErrorType
    override val message: String

    constructor(type: ErrorType) {
        this.type = type
        message = null
    }

    constructor(type: ErrorType, message: String) {
        this.type = type
        this.message = message
    }

}