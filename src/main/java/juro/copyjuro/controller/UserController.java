package juro.copyjuro.controller;

import juro.copyjuro.config.LoginUser;
import juro.copyjuro.controller.model.UserLoginRequest;
import juro.copyjuro.controller.model.UserRegisterRequest;
import juro.copyjuro.controller.model.UserResponse;
import juro.copyjuro.dto.user.UserDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import juro.copyjuro.dto.common.ApiResponse;
import juro.copyjuro.service.UserService;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/v1/auth/login")
	public ApiResponse<String> login(@Valid @RequestBody UserLoginRequest request) {
		String token = userService.login(request.toDto());
		return ApiResponse.success(token);
	}

	@PostMapping("/v1/auth/register")
	public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
		UserDto user = userService.register(request.toDto());
		UserResponse userResponse = UserResponse.of(user);
		return ApiResponse.success(userResponse);
	}

	@LoginUser
	@GetMapping("/v1/users/{id}")
	public ApiResponse<UserResponse> getUser(@PathVariable(value = "id") Long id) {
		UserDto user = userService.getUser(id);
		UserResponse userResponse = UserResponse.of(user);
		return ApiResponse.success(userResponse);
	}

}