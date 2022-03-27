package com.jp.boiler.base.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.common.security.config.SecurityConfig;
import com.jp.boiler.base.controller.channel.ChannelController;
import com.jp.boiler.base.controller.param.JacksonRequest;
import com.jp.boiler.base.manager.ServiceChannelManager;
import lombok.ToString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ChannelController.class)
public class GlobalExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceChannelManager serviceChannelManager;

    @MockBean
    private WebSecurityConfiguration webSecurityConfiguration;

    @MockBean
    private SecurityConfig securityConfig;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("9999 Error가 나는 경우 테스트")
    void globalException9999() throws Exception {
        // Given
        Mockito.when(serviceChannelManager.findJackson(any(JacksonRequest.class))).thenThrow(new BoilerException(ResultCode.RESULT_9999));
        JacksonRequest jacksonRequest = new JacksonRequest("jackson");

        // When & Then
        mockMvc.perform(post("/jp/api/v1/find")
                .content(objectMapper.writeValueAsString(jacksonRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("resultCode").value("9999"))
                .andExpect(jsonPath("resultMessage").value("실패"));
    }
}
