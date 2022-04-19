package com.jp.boiler.base.controller.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.boiler.base.common.exception.BoilerException;
import com.jp.boiler.base.controller.param.roles.Role;
import com.jp.boiler.base.controller.param.user.UserParam;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.properties.JwtProperties;
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
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

import static com.jp.boiler.base.common.utils.jwt.JwtClaimUtil.extractClaimByKey;
import static com.jp.boiler.base.common.utils.jwt.JwtClaimUtil.extractDecodedToken;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
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

    @Autowired
    JwtProperties jwtProperties;

    @Test
    @DisplayName("회원가입 테스트")
    @Transactional
    void signInTest() throws Exception {

        UserParam userParam = UserParam.builder()
                .username("tryCatch@gmail.com")
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
                .andExpect(jsonPath("resultData.username").value("tryCatch@gmail.com"));

        User user = userRepository.findByUsername("tryCatch@gmail.com");
        Assertions.assertEquals("ADMIN",user.getModId());

    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    @Transactional
    void conflictSignInTest() throws Exception {

        UserParam userParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .role(Role.ROLE_ADMIN)
                .build();

        UserParam userParam1 = UserParam.builder()
                .username("tryCatch@gmail.com")
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
                .andExpect(jsonPath("resultData.username").value("tryCatch@gmail.com"));

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(userParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect((result) ->
                        assertTrue(result.getResolvedException().getClass().isAssignableFrom(BoilerException.class))
                )
                .andExpect(jsonPath("resultData.message").value("이미 존재하는 회원입니다."))
                .andReturn();


    }

    @Test
    @DisplayName("로그인 성공 테스트")
    @Transactional
    void loginTest() throws Exception {

        UserParam joinParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .role(Role.ROLE_ADMIN)
                .build();

        mockMvc.perform(post("/jp/api/v1/join")
                .content(objectMapper.writeValueAsString(joinParam))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("resultData.username").value("tryCatch@gmail.com"))
                .andExpect(status().isOk())
                .andReturn();

        UserParam loginParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        HttpServletResponse response = loginResult.getResponse();
        // token 받아옴.
        String token = response.getHeader(jwtProperties.getCoreHeader());
        token = extractToken(token);
        DecodedJWT decodedToken = extractDecodedToken(token);
        String username = extractClaimByKey(decodedToken,jwtProperties.getClaim().getUsername());

        Assertions.assertNotNull(token);
        Assertions.assertEquals(username,"tryCatch@gmail.com");
    }

    @Test
    @DisplayName("권한별 url 접근 테스트[user]")
    @Transactional
    void accessByAuthorizeUserTest() throws Exception {

        UserParam joinParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .role(Role.ROLE_USER)
                .build();

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(joinParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("resultData.username").value("tryCatch@gmail.com"))
                .andExpect(status().isOk())
                .andReturn();

        UserParam loginParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        HttpServletResponse response = loginResult.getResponse();
        // token 받아옴.
        String token = response.getHeader(jwtProperties.getCoreHeader());

        mockMvc.perform(post("/user-test").header(jwtProperties.getCoreHeader(),token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();


    }

    @Test
    @DisplayName("권한별 url 접근 테스트[manager]")
    @Transactional
    void accessByAuthorizeManagerTest() throws Exception {

        UserParam joinParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .role(Role.ROLE_MANAGER)
                .build();

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(joinParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("resultData.username").value("tryCatch@gmail.com"))
                .andExpect(status().isOk())
                .andReturn();

        UserParam loginParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        HttpServletResponse response = loginResult.getResponse();
        // token 받아옴.
        String token = response.getHeader(jwtProperties.getCoreHeader());

        mockMvc.perform(post("/manager-test").header(jwtProperties.getCoreHeader(),token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    @DisplayName("권한별 url 접근 테스트[admin]")
    @Transactional
    void accessByAuthorizeAdminTest() throws Exception {

        UserParam joinParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .role(Role.ROLE_ADMIN)
                .build();

        mockMvc.perform(post("/jp/api/v1/join")
                        .content(objectMapper.writeValueAsString(joinParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("resultData.username").value("tryCatch@gmail.com"))
                .andExpect(status().isOk())
                .andReturn();

        UserParam loginParam = UserParam.builder()
                .username("tryCatch@gmail.com")
                .password("trycatcH12!@")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginParam))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        HttpServletResponse response = loginResult.getResponse();
        // token 받아옴.
        String token = response.getHeader(jwtProperties.getCoreHeader());

        mockMvc.perform(post("/admin-test").header(jwtProperties.getCoreHeader(),token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private String extractToken(String rawBearerToken){
        return rawBearerToken.replace(jwtProperties.getHeaderTypeWithBlankSpace(),"");
    }

}
