package juro.copyjuro.config;

import juro.copyjuro.config.auth.JwtUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {
    private final JwtUtil sut = new JwtUtil("V1RFTEpCSlRVUkRQVVBHUFBQSE5NTk1BVlNNU0pGV0JCWUpSU0dHVVVKTFRHVk5NUUdKWUQ", 1000L);

    @Test
    @DisplayName("Token 검증 - 256bit seceret key 입력시 검증 성공")
    void test() {
        //given
        String username = "username";
        String token = sut.generateToken(username);
        System.out.println("token = " + token);

        //when
        boolean b = sut.validateToken(token);

        //then
        Assertions.assertThat(b).isTrue();
    }

    @Test
    @DisplayName("username 얻기")
    public void test2() throws Exception {
        //given
        String username = "username";
        String token = sut.generateToken(username);

        //when
        String usernameFromToken = sut.getUsernameFromToken(token);

        //then
        Assertions.assertThat(usernameFromToken).isEqualTo("username");
    }
}
