package com.jp.boiler.base.common.filter.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jp.boiler.base.domain.auth.PrincipalDetails;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.properties.JwtProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jp.boiler.base.common.utils.jwt.JwtClaimUtil.extractClaimByKey;
import static com.jp.boiler.base.common.utils.jwt.JwtClaimUtil.extractDecodedToken;

// 시큐리티 필터중 BasicAuthenticationFilter-> 권한 또는 인증 관리 Filter
// 권한이나 인증이 필요한 주소를 요청했을 경우 하기 작성된 필터를 경우함.
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Getter
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserRepository userRepository,
                                  JwtProperties jwtProperties
                                  ) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }

    // TODO 토큰 시간 만료처리작업.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String jwtHeader = request.getHeader(jwtProperties.getCoreHeader());

        if(hasToken(jwtHeader)){

            String token = extractToken(jwtHeader);

            DecodedJWT decodedToken = extractDecodedToken(token);

            // 정상 서명
            if(isValidToken(decodedToken)){

                String username = extractClaimByKey(decodedToken,jwtProperties.getClaim().getUsername());

                User userEntity = userRepository.findByUsername(username);

                // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                // 비밀번호는 null 을 설정함 현재는 로그인 요청이 아님 username 이 null 이 아니기 때문에 정상적 서명이 이뤄졌다는것으로 간주함.
                // 서명이 정상일 경우 Authentication 객체를 만들어준다.
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null, principalDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication); // -> 강제로 시큐리티 세션에 접근하여 authentication 객체를 저장함

            }
        }

        chain.doFilter(request,response);
    }

    private String extractToken(String rawBearerToken){
        return rawBearerToken.replace(jwtProperties.getHeaderTypeWithBlankSpace(),"");
    }

    private boolean hasToken(String jwtHeader){
        return jwtHeader != null && jwtHeader.startsWith(jwtProperties.getHeaderType());
    }

    private boolean isValidToken(DecodedJWT decodedToken){
        return decodedToken.getSubject().equals(jwtProperties.getSubject());
    }
}
