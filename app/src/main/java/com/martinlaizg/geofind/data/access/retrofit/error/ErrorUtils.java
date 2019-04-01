package com.martinlaizg.geofind.data.access.retrofit.error;

import com.martinlaizg.geofind.data.access.retrofit.RetrofitService;

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
	public static APIError parseError(Response<?> response) {
		Converter<ResponseBody, APIError> converter = RetrofitService.getRetrofitInstance().responseBodyConverter(APIError.class, new Annotation[0]);

		APIError error = null;
		try {
			if (response.errorBody() != null) {
				error = converter.convert(response.errorBody());
			}
		} catch (IOException e) {
			return new APIError();
		}

		return error;
	}
}