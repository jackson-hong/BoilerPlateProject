package com.jp.boiler.base.controller;

import com.jp.boiler.base.controller.param.JacksonRequest;
import com.jp.boiler.base.controller.param.OrderRequestParam;
import com.jp.boiler.base.controller.payload.BasePayload;
import com.jp.boiler.base.controller.payload.OrderRequestPayload;
import com.jp.boiler.base.domain.order.Order;
import com.jp.boiler.base.manager.ServiceChannelManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/jp/api/v1/order")
@Api(tags = "Order Controller")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final ServiceChannelManager serviceChannelManager;

    @ApiOperation("주문 생성")
    @PostMapping
    public ResponseData<OrderRequestPayload> save(@RequestBody @Valid @ApiParam("주문 입력 정보") OrderRequestParam orderRequestParam){
        return ResponseData.success(serviceChannelManager.saveOrder(orderRequestParam));
    }

}
