package com.jp.boiler.base.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.boiler.base.common.exception.BoilerException;
import com.jp.boiler.base.controller.param.roles.Role;
import com.jp.boiler.base.controller.param.user.UserParam;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CorsFilter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 테스트")
    @Transactional
    void signInTest() throws Exception {

        UserParam userParam = UserParam.builder()
                .username("test")
                .password("trycatcH12!@")
                .role(Role.ROLE_ADMIN)
                .build();

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(userParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("resultCode").value("0000"))
                .andExpect(jsonPath("resultMessage").value("성공"))
                .andExpect(jsonPath("resultData.username").value("test"));

        User user = userRepository.findByUsername("test");
        Assertions.assertEquals("ADMIN",user.getModId());

    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    @Transactional
    void conflictSignInTest() throws Exception {

        UserParam userParam = UserParam.builder()
                .username("test")
                .password("trycatcH12!@")
                .role(Role.ROLE_ADMIN)
                .build();

        UserParam userParam1 = UserParam.builder()
                .username("test")
                .password("trycatcH12!@")
                .role(Role.ROLE_ADMIN)
                .build();

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(userParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("resultCode").value("0000"))
                .andExpect(jsonPath("resultMessage").value("성공"))
                .andExpect(jsonPath("resultData.username").value("test"));

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(userParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect((result) ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(BoilerException.class))
                )
                .andExpect(jsonPath("resultData.message").value("이미 존재하는 회원입니다."));

    }

}
