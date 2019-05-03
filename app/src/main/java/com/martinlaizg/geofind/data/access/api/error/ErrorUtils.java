package com.martinlaizg.geofind.data.access.api.error;

import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

	/**
	 * Parse a response to a APIError
	 *
	 * @param response response to parse
	 * @return parsed response
	 */
	public static APIException parseError(Response<?> response) {
		Converter<ResponseBody, APIException> converter = RetrofitInstance.getRetrofitInstance()
				.responseBodyConverter(APIException.class, new Annotation[0]);

		APIException error = null;
		try {
			if(response.errorBody() != null) {
				error = converter.convert(response.errorBody());
			}
		} catch(IOException e) {
			return new APIException(ErrorType.PARSE);
		}

		return error;
	}
}