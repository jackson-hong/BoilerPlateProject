package com.jp.boiler.base.common.config;

import com.jp.boiler.base.config.JpaConfig;
import com.jp.boiler.base.domain.order.Order;
import com.jp.boiler.base.domain.order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class JpaConfigTest {

    @Nested
    @Import(JpaConfig.class)
    @DataJpaTest
    @Transactional
    public class InsertTestWithUser{
        @Autowired
        OrderRepository orderRepository;

        @MockBean
        AuditorAware<String> auditorProvider;

        private final String userId = "jackson@gmail.com";

        @Test
        @DisplayName("SAVE시 USER가 있는 경우 REG_ID를 확인한다.")
        void insertTest(){

            Mockito.when(auditorProvider.getCurrentAuditor()).thenReturn(Optional.of(userId));

            Order order = Order.builder()
                    .name("jackson")
                    .phoneNum("01012341234")
                    .build();

            // WHEN
            Order orderAfterSaved = orderRepository.save(order);

            // THEN
            Order orderFoundById = orderRepository.findById(order.getOrderId()).get();
            assertThat(orderAfterSaved).isEqualTo(orderFoundById);
            assertThat(orderAfterSaved.getRegId()).isEqualTo(userId);
        }
    }

    @Nested
    @Import(JpaConfig.class)
    @DataJpaTest
    @Transactional
    public class InsertTestWithoutUser{
        @Autowired
        OrderRepository orderRepository;

        @Test
        @DisplayName("SAVE시 USER가 없는 경우 REG_ID를 확인한다.")
        void insertTestWithoutUser(){

            Order order = Order.builder()
                    .name("jackson")
                    .phoneNum("01012341234")
                    .build();

            // WHEN
            Order orderAfterSaved = orderRepository.save(order);

            // THEN
            Order orderFoundById = orderRepository.findById(order.getOrderId()).get();
            assertThat(orderAfterSaved).isEqualTo(orderFoundById);
            assertThat(orderAfterSaved.getRegId()).isEqualTo("ADMIN");
        }
    }
}
