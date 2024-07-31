package juro.copyjuro.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import juro.copyjuro.config.JwtUtil;
import juro.copyjuro.dto.user.LoginRequestDto;
import juro.copyjuro.dto.user.UserRole;
import juro.copyjuro.repository.user.UserRepository;
import juro.copyjuro.repository.user.model.User;
import org.springframework.stereotype.Service;

import juro.copyjuro.dto.user.UserRegisterRequestDto;
import juro.copyjuro.dto.user.UserDto;
import juro.copyjuro.exception.ClientException;
import juro.copyjuro.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public String login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct=%s".formatted(dto)));

        if (!dto.getPassword().equals(user.getPassword())) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct=%s".formatted(dto));
        }
        return jwtUtil.generateToken(dto.getUsername());
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(
                        () -> new ClientException(ErrorCode.BAD_REQUEST, "cannot found user. userId = %s".formatted(id)));
        return UserDto.of(user);
    }

    @Transactional
    public UserDto register(UserRegisterRequestDto dto) {
        validatePassword(dto.getPassword());
        Optional<User> findUser = userRepository.findByUsername(dto.getUsername());
        if (findUser.isPresent()) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "username already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .roles(List.of(UserRole.USER))
                .build();

        User savedUser = userRepository.save(user);
        return UserDto.of(savedUser);
    }


    // 비밀번호 규칙 정의 (최소 8자, 대문자, 소문자, 숫자, 특수 문자 포함)

    private void validatePassword(String password) {
        if (password == null || !pattern.matcher(password).matches()) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "invalid password. password = %s".formatted(password));
        }
    }
}
