package com.jp.boiler.base.controller.payload;


import com.jp.boiler.base.controller.param.OrderRequestParam;
import com.jp.boiler.base.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestPayload {

    private String orderId;
    private String name;
    private String phoneNum;

    public static OrderRequestPayload fromOrder(Order order){
        return OrderRequestPayload.builder()
                .orderId(String.valueOf(order.getOrderId()))
                .phoneNum(order.getPhoneNum())
                .name(order.getName())
                .build();
    }
}
