package com.jp.boiler.base.order;

import kr.jackson.domain.order.Order;
import kr.jackson.domain.order.OrderRepository;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Order order){
        orderRepository.save(order);
        System.out.println("INNER TRANS ENDS");
//        TransactionStatus status = manager.getTransaction();
    }

    @Transactional
    public void saveTest(){

        Order order1 = orderRepository.findById(1L).get();

        changeTestService.changeName("jackson");

        changeTestService.changeName("test");




        throw new RuntimeException("NO!");

    }

}
