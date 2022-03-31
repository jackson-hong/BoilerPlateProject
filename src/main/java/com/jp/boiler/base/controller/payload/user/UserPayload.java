package com.jp.boiler.base.controller.payload.user;

import com.jp.boiler.base.controller.param.roles.Role;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@ToString
public class UserPayload {

    @NotNull
    private String username;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public UserPayload(String username, Role role) {
        this.username = username;
        this.role = role;
    }
}
