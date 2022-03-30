package com.jp.boiler.base.controller.param;

import com.jp.boiler.base.domain.order.Order;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestParam {

    private String name;
    private String phoneNum;

    public Order toEntityOrder(){
        return Order.builder()
                .name(this.name)
                .phoneNum(this.phoneNum)
                .build();
    }

}
