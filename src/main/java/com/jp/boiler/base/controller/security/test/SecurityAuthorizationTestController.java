package com.jp.boiler.base.controller.security.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/jp/api/v1/test")
@RestController
public class SecurityAuthorizationTestController {

    @GetMapping("user")
    public String authUserTest(){
        return "사용자 권한 접근";
    }

    @GetMapping("manager")
    public String authManagerTest(){
        return "메니저 권한 접근";
    }

    @GetMapping("admin")
    public String adminManagerTest(){
        return "관리자 권한 접근";
    }
}
