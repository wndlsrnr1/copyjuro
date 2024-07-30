package juro.copyjuro.controller.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import juro.copyjuro.exception.ErrorCode;
import lombok.*;

import java.io.IOException;

@Data
@RequiredArgsConstructor
public class ApiResponse<T> {
	private final ErrorCode errorCode;
	private final String errorMessage;
	private final T body;

	private ApiResponse() {
		this.errorCode = null;
		this.errorMessage = null;
		this.body = null;
	}

	public static <T> ApiResponse<T> success() {
		return new ApiResponse<>(null, null, null);
	}

	public static <T> ApiResponse<T> success(T body) {
		return new ApiResponse<>(null, null, body);
	}

	public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
		return new ApiResponse<>(errorCode, errorCode.getClientMessage(), null);
	}
}
