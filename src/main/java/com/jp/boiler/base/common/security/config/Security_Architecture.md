# Security_Architecture

***http.csrf().disable()***
> 브라우저 서비스가 아니기 때문에 불필요한 인증정보를 저장하지 않는다. 대신 jwt 토큰으로 request 의 유효성과 안정성을 확보.

***http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)***
> stateless 프로토콜 선언. 인증정보를 서버에서 저장하지 않음.

***formLogin().disable()***
> form Login 방식 사용하지 않음.

***httpBasic().disable()***
> Header 부분에 Authorization , id, pwd 를 담아서 매번 요청하는 인증방식을 사용하지 않음.  https 여도 보안상의 이슈가 있음.
> disable 선언후 Authorization 에 토큰을 담아 인증을 하는 방식을 채택, disable 선언함.

***http.addFilterBefore(new AuthorizationFilter(), SecurityContextPersistenceFilter.class)***
> Security 가 개입을 시작하는 필터 이전에 AuthorizationFilter 를 최우선으로 실행하도록 설정.

---

# Login Process

1. /login [POST] url 요청시 SpringSecurity 자체 로그인 UsernamePasswordAuthenticationFilter 에서 유효성 검증 시작.

2. JwtAuthenticationFilter -> UsernamePasswordAuthenticationFilter 상속하여 JwtAuthenticationFilter 에서 로그인 처리를 대신함.

3. UsernamePasswordAuthenticationToken 생성 후 위 토큰 값을 AuthenticationManager 에 인증 요청. 
(시큐리티 기본 내장 인증 로직이나 오버라이드 시행하여 직접 구현)
```java
Authentication authentication = authenticationManager.authenticate(authenticationToken);
```
4. 위 코드 실행시 PrincipalDetailsService -> loadUserByUsername 메소드 실행하여 사용자가 존재 하는지 여부 확인(비밀번호 인증처리 또한 시큐리티 내장 로직에서 진행)

5. 유저가 존재할 경우 PrincipalDetails 객체에 User 정보 주입.

6. 유저 정보가 주입된 PrincipalDetails 객체가 Authentication 객체의 세션 영역에 저장되어 로그인 완료됨.
> 위에 저장되는 세션은 formLogin 방식때 생성되는 세션과 속성이 비슷하나 인증후 생성된 Authentication session 으로 인증여부를 판단하지 않음. 
> 기존에 생성된 Session 을 참조하거나 사용하지 않음.
[Reference](https://www.inflearn.com/questions/34886)

7. 로그인이 정상적으로 처리 되었을 경우 successfulAuthentication 메소드에서 Jwt 발급 후처리 작업.

8. jwtToken 값 헤더에 담아 클라이언트로 Response.

# Authorization Process

1. Header 에 Authorization 키로 담긴 토큰이 존재 하는지 유효성 확인. (없을 경우 다음 필터로 흘려보냄. 권한이 없는 url 요청일 경우 AccessDenied)

2. JWT 헤시값 해제 후 Claim key 값으로 username 이 정상적으로 담겼는지 여부를 확인.

3. username 이 담겨있을경우 정상적인 인증 절차를 거쳐 발급된 토큰이라는것으로 간주함 (추후 username 과 다른 키값으로 변경 가능)

4. username 을 이용 Authentication 에 principalDetails 객체와 인증 정보를 파라미터로 넘겨 Authentication 세션 생성
> 유저권한 확인 임시 session 생성.


