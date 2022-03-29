package com.jp.boiler.base.controller.user;

import com.jp.boiler.base.controller.param.UserParam;
import com.jp.boiler.base.domain.auth.PrincipalDetails;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;

import com.jp.boiler.base.service.auth.PrincipalDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jp/api/v1")
@AllArgsConstructor
// TODO : 추후 회원가입 테스트 완료후 mvc 로 분리할것.
public class UserController {

    private final PrincipalDetailsService principalDetailsService;

    @PostMapping("join")
    public String join(@RequestBody @Validated UserParam userParam){
      /*  user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);*/


        return "회원가입 완료";
    }

}
