package com.martinlaizg.geofind.data.access.api.error

import com.martinlaizg.geofind.data.access.api.RetrofitInstance
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import retrofit2.Response
import java.io.IOException

object ErrorUtils {
    /**
     * Parse a response to a APIException
     *
     * @param response response to parse
     * @return parsed response
     */
    fun parseError(response: Response<*>): APIException? {
        val converter = RetrofitInstance.getRetrofitInstance()
                .responseBodyConverter<APIException>(APIException::class.java, arrayOfNulls(0))
        var error: APIException? = null
        try {
            if (response.errorBody() != null) {
                error = converter.convert(response.errorBody())
            }
        } catch (e: IOException) {
            return APIException(ErrorType.OTHER)
        }
        return error
    }
}