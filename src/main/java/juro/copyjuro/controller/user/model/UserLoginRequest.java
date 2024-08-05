package juro.copyjuro.controller.user.model;

import juro.copyjuro.service.user.model.LoginRequestDto;
import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;

    public LoginRequestDto toDto() {
        return LoginRequestDto.builder()
                .username(username)
                .password(password)
                .build();
    }
}
