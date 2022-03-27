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

        return BasePayload.builder()
                .resultCode("0000")
                .resultMsg("성공")
                .build();
    };
}
