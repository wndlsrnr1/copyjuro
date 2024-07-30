package juro.copyjuro.config;

import jakarta.annotation.PostConstruct;
import juro.copyjuro.dto.user.UserRole;
import juro.copyjuro.repository.user.UserRepository;
import juro.copyjuro.repository.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile({"local", "dev"})
@RequiredArgsConstructor
@Component
public class DebuggingAuth {
    private final String USERNAME = "test";
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        userRepository.findByUsername(USERNAME)
                .orElseGet(() -> {
                    User user = User.builder()
                            .username(USERNAME)
                            .email("test@naver.com")
                            .password("test")
                            .roles(List.of(UserRole.USER))
                            .build();
                    return userRepository.save(user);
                });
    }

    public String getBearer() {
        return "Bearer " + jwtUtil.generateToken(USERNAME);
    }
}
