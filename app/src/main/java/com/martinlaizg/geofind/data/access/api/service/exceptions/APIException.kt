package com.martinlaizg.geofind.data.access.api.service.exceptions

import com.martinlaizg.geofind.data.access.api.error.ErrorType

/**
 * Exception from API
 */
class APIException(val type: ErrorType, override val message: String = "") : Throwable(message)