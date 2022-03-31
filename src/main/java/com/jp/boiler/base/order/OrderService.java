package com.jp.boiler.base.order;

import com.jp.boiler.base.domain.order.Order;
import com.jp.boiler.base.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PlatformTransactionManager manager;
    private final ChangeTestService changeTestService;

    public Order save(Order order){
        return orderRepository.save(order);
    }

}
