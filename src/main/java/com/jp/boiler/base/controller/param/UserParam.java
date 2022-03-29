package com.jp.boiler.base.controller.param;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
@Getter
@ToString
@NoArgsConstructor
public class UserParam {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String role;




}
