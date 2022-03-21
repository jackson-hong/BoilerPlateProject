package com.jp.boiler.base.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel(description = "Response Data")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData {
}
