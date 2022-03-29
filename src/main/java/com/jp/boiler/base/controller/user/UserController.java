package com.jp.boiler.base.controller.user;

import com.jp.boiler.base.controller.ResponseData;
import com.jp.boiler.base.controller.param.UserParam;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.service.auth.PrincipalDetailsService;
import lombok.AllArgsConstructor;
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
    public ResponseData<User> join(@RequestBody @Validated UserParam userParam){
        return ResponseData.success(principalDetailsService.userDataHandler(userParam));
    }

}
