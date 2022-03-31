package com.jp.boiler.base.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.boiler.base.controller.param.user.UserParam;
import com.jp.boiler.base.controller.param.roles.Role;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.service.auth.PrincipalDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    PrincipalDetailsService principalDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 가입 테스트")
    void joinTest() throws Exception {
        User user = User.builder()
                .username("test")
                .password("1234")
                .role(Role.ROLE_USER)
                .build();

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                ;

        Assertions.assertNotNull(userRepository.findByUsername("test"));
    }

    @Test
    @DisplayName("파라미터 검증 테스트")
    void isParameterValidTest() throws Exception {

        Mockito.when(principalDetailsService.userDataHandler(any(UserParam.class))).thenReturn(null);
        UserParam userParam = UserParam.builder()
                .username("test")
                .role(Role.ROLE_ADMIN)
                .build();

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(userParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect((result) ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(MethodArgumentNotValidException.class))
                )
        ;
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signInTest() throws Exception {

        UserParam userParam = UserParam.builder()
                .username("test")
                .password("12345")
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


    }


}