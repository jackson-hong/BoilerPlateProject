package com.jp.boiler.base.controller.user;

import com.jp.boiler.base.controller.param.OrderRequestParam;
import com.jp.boiler.base.domain.order.OrderRepository;
import com.jp.boiler.base.manager.ServiceChannelManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CorsFilter;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @MockBean
    ServiceChannelManager serviceChannelManager;

    @Test
    @DisplayName("주문 생성 API 컨트롤러 테스트")
    void saveOrderTest(){
        // GIVEN
        OrderRequestParam orderRequestParam = OrderRequestParam.builder()
                .name("홍지운")
                .phoneNum("010-7474-9884")
                .build();

    }

}
