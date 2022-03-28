package com.jp.boiler.base.common.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.common.exception.BoilerException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtClaimUtil {

    public static String extractClaimByKey(DecodedJWT decodedJWT, String key){
        return decodedJWT
                .getClaim(key)
                .asString();
    }

    public static DecodedJWT extractDecodedToken(String token){
        try{
        return  JWT.require(Algorithm.HMAC512("boiler"))
                .build()
                .verify(token);
        } catch (TokenExpiredException exception){
            log.info("[TOKEN EXPIRED] {}", exception.getMessage());
            throw new BoilerException(ResultCode.RESULT_4001);
        }
    }
}
