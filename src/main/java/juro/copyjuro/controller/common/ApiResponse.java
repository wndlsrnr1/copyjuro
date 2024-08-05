package juro.copyjuro.controller.common;

import juro.copyjuro.exception.ErrorCode;
import lombok.*;

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
