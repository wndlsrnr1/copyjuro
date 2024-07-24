package juro.copyjuro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 가입 -> 로그인 -> USER ROLE 인가")
    public void test() throws Exception {
        // Register a new user
        String registerJson = "{ \"username\": \"hello\", \"password\": \"anyPassword!@ASD213\", \"email\": \"juro@naver.com\" }";

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk());

        // Login with the registered user
        String loginJson = "{ \"username\": \"hello\", \"password\": \"anyPassword!@ASD213\" }";

        MvcResult result = mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        // Extract token from login response
        String responseString = result.getResponse().getContentAsString();
        Map<String, String> responseMap = objectMapper.readValue(responseString, Map.class);
        String token = responseMap.get("body");

        // Get user details
        mockMvc.perform(get("/v1/users/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("invalid token - 유효하지 않은 토큰 입력시 403 Credential exception 던진다")
    public void test2() throws Exception {
        //given
        String invalidToken = "invalidToken";

        //when
        ResultActions results = mockMvc.perform(get("/v1/users/1")
                .header("Authorization", "Bearer " + invalidToken)
        );

        //then
        results.andExpect(status().is4xxClientError());
        Assertions.assertThat(
                results.andReturn().getResponse().getStatus()
        ).isEqualTo(403);
    }
}
