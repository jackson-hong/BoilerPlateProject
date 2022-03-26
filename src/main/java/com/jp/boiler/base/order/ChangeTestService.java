package com.jp.boiler.base.order;

import com.jp.boiler.base.domain.order.Order;
import com.jp.boiler.base.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeTestService {

    private final OrderRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeName(String name){
        Order order = orderRepository.findById(1L).get();
    }
}
