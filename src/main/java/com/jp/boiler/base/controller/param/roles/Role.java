package com.jp.boiler.base.controller.param.roles;

import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.common.exception.BoilerException;
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

    public static Role getRoleTypeByString(String role){
        switch (role){
            case "ROLE_ADMIN" :return ROLE_ADMIN;
            case "ROLE_MANAGER" : return ROLE_MANAGER;
            case "ROLE_USER" : return ROLE_USER;
            default: throw new BoilerException(ResultCode.RESULT_4000);

        }
    }
}
