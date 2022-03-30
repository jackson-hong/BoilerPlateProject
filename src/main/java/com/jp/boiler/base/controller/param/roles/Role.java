package com.jp.boiler.base.controller.param.roles;

import lombok.Getter;

public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_USER("ROLE_USER")
    ;

    @Getter
    private final String role;

    Role(String role) {
        this.role = role;
    }

}
