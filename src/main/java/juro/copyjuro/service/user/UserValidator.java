package juro.copyjuro.service.user;

import juro.copyjuro.exception.ClientException;
import juro.copyjuro.exception.ErrorCode;
import juro.copyjuro.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=%s".formatted(userId));
        }
    }

}
