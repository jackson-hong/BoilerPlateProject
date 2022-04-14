package com.jp.boiler.base.controller;

import com.jp.boiler.base.controller.param.OrderRequestParam;
import com.jp.boiler.base.controller.payload.OrderRequestPayload;
import com.jp.boiler.base.manager.ServiceChannelManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/jp/api/v1/order")
@Tag(name = "Order Controller")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final ServiceChannelManager serviceChannelManager;

    @Operation(description = "주문 생성")
    @PostMapping
    public ResponseData<OrderRequestPayload> save(@RequestBody @Valid @Parameter(name = "주문 입력 정보") OrderRequestParam orderRequestParam){
        return ResponseData.success(serviceChannelManager.saveOrder(orderRequestParam));
    }

}
