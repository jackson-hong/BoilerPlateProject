package com.jp.boiler.base.controller.param.user;

import com.jp.boiler.base.controller.param.roles.Role;
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
public class UserParam {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
