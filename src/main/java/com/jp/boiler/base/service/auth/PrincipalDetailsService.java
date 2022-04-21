package com.jp.boiler.base.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.common.exception.BoilerException;
import com.jp.boiler.base.controller.param.roles.Role;
import com.jp.boiler.base.controller.param.user.UserParam;
import com.jp.boiler.base.controller.payload.user.UserPayload;
import com.jp.boiler.base.domain.auth.PrincipalDetails;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.properties.JwtProperties;
import com.jp.boiler.base.service.auth.verifier.SignUpDataVerifier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

// login 요청시 즉시 동작.
@Service
@AllArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SignUpDataVerifier signUpDataVerifier;

    private final JwtProperties jwtProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[USER_DETAILS] Security login Auth process start");
        User user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }

    public UserPayload userDataHandler(UserParam userParam){
        User user = new ModelMapper().map(userParam,User.class);
        signUpDataVerifier.validateUserParam(userParam);
        user.encodePwd(bCryptPasswordEncoder);
        user = saveUser(user);
        return userPayloadBuilder(user);
    }

    private User saveUser(User user){
        return userRepository.save(user);
    }

    private UserPayload userPayloadBuilder(User user){
        return UserPayload.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public UserPayload buildLoginSuccessResData(HttpServletResponse response, HttpServletRequest request){

        String jwtToken = JWT.create()
                .withSubject(jwtProperties.getSubject()) // 제목
                .withExpiresAt( new Date(System.currentTimeMillis() + jwtProperties.getRefreshTime() )) // 토큰 유효기간 설정
                .withClaim(jwtProperties.getClaim().getId(), (Long)request.getAttribute(jwtProperties.getClaim().getId()) ) // key value 값으로 저장하고자 하는 값 원하는 값을 넣으면 된다.
                .withClaim(jwtProperties.getClaim().getUsername(),(String) request.getAttribute(jwtProperties.getClaim().getUsername()))
                .sign(Algorithm.HMAC512(jwtProperties.getAlgorithm())); // 원하는 암호화 알고리즘 설정 개발단계에서 리터럴로 boiler 로 설정함.

        response.addHeader(jwtProperties.getCoreHeader(),jwtProperties.getHeaderTypeWithBlankSpace()+jwtToken);
        return UserPayload.builder()
                .username((String) request.getAttribute(jwtProperties.getClaim().getUsername()))
                .role(Role.ROLE_ADMIN)
                .build();
    }


}
