package juro.copyjuro.repository;

import java.util.Optional;

import juro.copyjuro.dto.user.UserCreateRequestDto;
import juro.copyjuro.dto.user.UserDto;

public interface UserMapper {
	Optional<UserDto> getUserById(long id);

	UserDto createUser(UserCreateRequestDto dto);
}
