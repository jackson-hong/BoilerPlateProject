package com.jp.boiler.base.common.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.boiler.base.common.exception.BoilerException;
import com.jp.boiler.base.common.security.config.SecurityConfig;
import com.jp.boiler.base.controller.channel.ChannelController;
import com.jp.boiler.base.controller.param.JacksonRequest;
import com.jp.boiler.base.controller.param.roles.Role;
import com.jp.boiler.base.controller.payload.BasePayload;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.manager.ServiceChannelManager;
import com.jp.boiler.base.properties.JwtProperties;
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
            .role(Role.ROLE_USER)
            .password("$2a$10$aRWuKJprJ11TFV04HO2gIeK3cCZLLJhimRdtl3Q3wftyePyjfZ3Oa")
            .build();

    @Nested
    @AutoConfigureMockMvc
    @Import({SecurityConfig.class, CorsFilter.class, JwtProperties.class})
    public class CaseTokenVarified{

        private final int REFRESH_TOKEN_VALID_TIME = 1000; // 1???

        private final String token = JWT.create()
                .withSubject("boilerPlateToken") // ??????
                .withExpiresAt( new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME)) // ?????? ???????????? ??????
                .sign(Algorithm.HMAC512("boiler"));

        @Test
        @DisplayName("Token ?????? ?????? ?????????")
        @Order(2)
        @Disabled //?????? ???????????? ????????? ????????????
        void tokenExpirationTest(){

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assertions.assertThrows(BoilerException.class, () -> {
                extractDecodedToken(token);
            });

        }

        @Test
        @DisplayName("Token ????????? ?????? ?????????")
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
    @Import({SecurityConfig.class, CorsFilter.class, JwtProperties.class})
    public class CaseNonAuthorized{

        @MockBean
        private UserRepository userRepository;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        ServiceChannelManager serviceChannelManager;

        @Test
        @DisplayName("Token ?????? ???????????? ??????")
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
    @Import({SecurityConfig.class, CorsFilter.class, JwtProperties.class})
    public class CaseAuthorized{

        @MockBean
        private UserRepository userRepository;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        ServiceChannelManager serviceChannelManager;

        @Autowired
        JwtProperties jwtProperties;

        @Test
        @DisplayName("Token??? ?????? ???????????? ??????")
        void requestWithToken() throws Exception {
            // Given
            JacksonRequest jacksonRequest = new JacksonRequest("jackson");
            BasePayload basePayload = BasePayload.builder()
                    .resultCode("0000")
                    .resultMsg("??????")
                    .build();


            Mockito.when(serviceChannelManager.findJackson(any(JacksonRequest.class))).thenReturn(basePayload);
            Mockito.when(userRepository.findByUsername(anyString())).thenReturn(user);

            String token = JWT.create()
                    .withSubject(jwtProperties.getSubject())
                    .withClaim(jwtProperties.getClaim().getUsername(),USER_ID)
                    .sign(Algorithm.HMAC512(jwtProperties.getAlgorithm()));

            // When & Then
            mockMvc.perform(post("/user")
                    .header(jwtProperties.getCoreHeader(), jwtProperties.getHeaderTypeWithBlankSpace()+ token)
                    .content(objectMapper.writeValueAsString(jacksonRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }
}
