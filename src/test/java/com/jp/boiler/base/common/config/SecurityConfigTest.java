package com.jp.boiler.base.common.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.boiler.base.common.exception.BoilerException;
import com.jp.boiler.base.common.security.config.SecurityConfig;
import com.jp.boiler.base.controller.channel.ChannelController;
import com.jp.boiler.base.controller.param.JacksonRequest;
import com.jp.boiler.base.controller.payload.BasePayload;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.manager.ServiceChannelManager;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.CorsFilter;

import java.util.Date;

import static com.jp.boiler.base.common.utils.jwt.JwtClaimUtil.extractDecodedToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SecurityConfigTest {

    private final String USER_ID = "yhy1045@naver.com";
    private final User user = User.builder()
            .id(1)
            .username(USER_ID)
            .role("USER")
            .password("$2a$10$aRWuKJprJ11TFV04HO2gIeK3cCZLLJhimRdtl3Q3wftyePyjfZ3Oa")
            .build();

    @Nested
    @AutoConfigureMockMvc
    @Import({SecurityConfig.class, CorsFilter.class})
    public class CaseTokenVarified{

        private final int REFRESH_TOKEN_VALID_TIME = 1000; // 1초

        private final String token = JWT.create()
                .withSubject("boilerPlateToken") // 제목
                .withExpiresAt( new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME)) // 토큰 유효기간 설정
                .sign(Algorithm.HMAC512("boiler"));

        @Test
        @DisplayName("Token 시간 만료 테스트")
        @Order(2)
        void tokenExpirationTest(){

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assertions.assertThrows(BoilerException.class, () -> {
                extractDecodedToken(token);
            });

        }

        @Test
        @DisplayName("Token 유효성 검증 테스트")
        @Order(1)
        void tokenVerifyTest(){
            DecodedJWT decodedToken = extractDecodedToken(token);

            Assertions.assertTrue(verifyToken(decodedToken));
        }

        private boolean verifyToken(DecodedJWT decodedToken){
            return decodedToken.getSubject().equals("boilerPlateToken");
        }

    }


    @Nested
    @AutoConfigureMockMvc
    @WebMvcTest(controllers = ChannelController.class)
    @Import({SecurityConfig.class, CorsFilter.class})
    public class CaseNonAuthorized{

        @MockBean
        private UserRepository userRepository;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        ServiceChannelManager serviceChannelManager;

        @Test
        @DisplayName("Token 없이 요청하는 경우")
        void requestWithoutToken() throws Exception {
            // Given
            JacksonRequest jacksonRequest = new JacksonRequest("jackson");

            // When & Then
            mockMvc.perform(post("/jp/api/v1/find")
                    .content(objectMapper.writeValueAsString(jacksonRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @AutoConfigureMockMvc
    @WebMvcTest(controllers = ChannelController.class)
    @Import({SecurityConfig.class, CorsFilter.class})
    public class CaseAuthorized{

        @MockBean
        private UserRepository userRepository;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        ServiceChannelManager serviceChannelManager;

        @Test
        @DisplayName("Token와 함께 요청하는 경우")
        void requestWithToken() throws Exception {
            // Given
            JacksonRequest jacksonRequest = new JacksonRequest("jackson");
            BasePayload basePayload = BasePayload.builder()
                    .resultCode("0000")
                    .resultMsg("성공")
                    .build();


            Mockito.when(serviceChannelManager.findJackson(any(JacksonRequest.class))).thenReturn(basePayload);
            Mockito.when(userRepository.findByUsername(anyString())).thenReturn(user);

            String token = JWT.create()
                    .withClaim("username",USER_ID)
                    .sign(Algorithm.HMAC512("boiler"));

            // When & Then
            mockMvc.perform(post("/jp/api/v1/find")
                    .header("Authorization", "Bearer "+ token)
                    .content(objectMapper.writeValueAsString(jacksonRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}
