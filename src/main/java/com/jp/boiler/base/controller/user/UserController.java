package com.jp.boiler.base.controller.user;

import com.jp.boiler.base.common.annotation.ApiCode;
import com.jp.boiler.base.controller.ResponseData;
import com.jp.boiler.base.controller.param.user.UserParam;
import com.jp.boiler.base.controller.payload.user.UserPayload;
import com.jp.boiler.base.service.auth.PrincipalDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Tag(name = "[유저] 회원 컨트롤러")
@RequestMapping("/jp/api/v1")
@AllArgsConstructor
@Slf4j
// TODO : 추후 회원가입 테스트 완료후 mvc 로 분리할것.
public class UserController {

    private final PrincipalDetailsService principalDetailsService;

    @PostMapping("/join")
    @ApiCode("USER-001")
    public ResponseData<UserPayload> signUp(@RequestBody @Validated UserParam userParam){
        return ResponseData.success(principalDetailsService.userDataHandler(userParam));
    }

    @PostMapping("/login/jwt")
    @ApiIgnore
    public ResponseData<UserPayload> loginSuccessHandler(HttpServletResponse response, HttpServletRequest request){
        return ResponseData.success(principalDetailsService.buildLoginSuccessResData(response,request));
    }
    
}
