package com.jp.boiler.base.controller.param.user;

import com.jp.boiler.base.controller.param.roles.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Slf4j
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원가입 Request")
public class UserParam {

    @NotNull
    @Schema(description = "유저 아이디", defaultValue = "yhy1011@trycatch.kr")
    private String username;

    @NotNull
    @Schema(description = "비밀번호", defaultValue = "abcdeFGH12!@")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "권한")
    private Role role;

}
