package juro.copyjuro.dto.common;

import juro.copyjuro.exception.ErrorCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse<T> {
	private final ErrorCode errorCode;
	private final String errorMessage;
	private final T body;

	public static <T> ApiResponse<T> success(T body) {
		return new ApiResponse<>(null, null, body);
	}

	public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
		return new ApiResponse<>(errorCode, errorCode.getClientMessage(), null);
	}
}
