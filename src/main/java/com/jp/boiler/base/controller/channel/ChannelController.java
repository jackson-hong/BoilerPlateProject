package com.jp.boiler.base.controller.channel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.jackson.controller.param.JacksonRequest;
import kr.jackson.controller.payload.BasePayload;
import kr.jackson.manager.ServiceChannelManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/jck/api/v1")
@Api(tags = "general controller")
@RequiredArgsConstructor
@Slf4j
public class ChannelController {

    private final ServiceChannelManager serviceChannelManager;

    @ApiOperation("JCK-101 잭슨 조회하기")
    @PostMapping("/find")
    public BasePayload methodOne(@RequestBody @Valid @ApiParam("조회 정보") JacksonRequest jacksonRequest){
        log.info("JCK-101");
        return serviceChannelManager.findJackson(jacksonRequest);
    }

}
