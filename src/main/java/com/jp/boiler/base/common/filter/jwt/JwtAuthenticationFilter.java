package com.jp.boiler.base.common.filter.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.boiler.base.domain.auth.PrincipalDetails;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.properties.JwtProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// username
// password 인증처리 Filter
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProperties jwtProperties;

    // login 시도할 경우 동작하는 Filter
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("[Login Requested]");

        ObjectMapper objectMapper = new ObjectMapper();
        try {

            User user = objectMapper.readValue(request.getInputStream(), User.class);

            // 함수 실행시 userName 과 password 를 이용 Token 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());

            // 생성된 Token 에서 userName 과 password 가 일치 하는지 여부를 확인.
            Authentication authentication = getAuthenticationManager().authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            log.info(principalDetails.getUser().getUsername());

            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 인증이 완료 되었을경우 JWT 토큰을 만들어 return 함.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("[Login Request Success]");
       PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        request.setAttribute(jwtProperties.getClaim().getId(), principalDetails.getUser().getId());
        request.setAttribute(jwtProperties.getClaim().getUsername(),principalDetails.getUser().getUsername());

        request.getRequestDispatcher("/jp/api/v1/login/jwt").forward(request,response);
    }
}
