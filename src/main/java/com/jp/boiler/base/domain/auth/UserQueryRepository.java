package com.jp.boiler.base.domain.auth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jp.boiler.base.domain.auth.QUser.user;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<User> findAllByUser(){

        return jpaQueryFactory.selectFrom(user)
                .fetch();

    }
}
