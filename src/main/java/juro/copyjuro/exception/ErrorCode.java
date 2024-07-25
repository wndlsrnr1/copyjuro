package juro.copyjuro.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	BAD_REQUEST("잘못 입력하셨습니다.", HttpStatus.BAD_REQUEST),
	INVALID_PASSWORD("비밀번호 규칙을 확인하세요.", HttpStatus.BAD_REQUEST),
	INTERNAL_SERVER_ERROR("작업 중 에러가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
	FORBIDDEN("인증 정보가 잘못 되었습니다", HttpStatus.FORBIDDEN);

	private final String clientMessage;
	private final HttpStatus httpStatus;
}
