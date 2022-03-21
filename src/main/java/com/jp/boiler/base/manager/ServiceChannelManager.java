package com.jp.boiler.base.manager;

import com.jp.boiler.base.controller.param.JacksonRequest;
import com.jp.boiler.base.controller.payload.BasePayload;
import com.jp.boiler.base.domain.order.Order;
import com.jp.boiler.base.domain.order.OrderRepository;
import com.jp.boiler.base.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ServiceChannelManager {


    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public BasePayload findJackson(JacksonRequest request){
        log.info("find Jackson executed");

        Order order1 = Order.builder().orderId(1L).name("1").build();
        Order order2 = Order.builder().orderId(2L).name("1").build();
        Order order3 = Order.builder().orderId(3L).name("1").build();

        List<Order> orderList = Arrays.asList(order1, order2, order3);

        orderRepository.save(order1);

        try{

            orderService.saveTest();
        }catch (RuntimeException runtimeException){
            System.out.println("catch!");
        }

        Order afterTest = orderRepository.findById(1L).get();

        System.out.println(afterTest.getName());

        return BasePayload.builder()
                .resultCode("0000")
                .resultMsg("성공")
                .build();
    };
}
