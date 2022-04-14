package com.jp.boiler.base.domain.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserQueryRepositoryTest {

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Test
    @DisplayName("쿼리 Dsl 작동테스트")
    void queryRepositoryTest(){
        List<User> users = userQueryRepository.findAllByUser();
        log.info(users.size()+"");
        Assertions.assertNotNull(users);
    }

}