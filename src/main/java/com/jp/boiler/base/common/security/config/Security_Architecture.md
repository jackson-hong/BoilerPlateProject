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

***JwtAuthenticationFilter***
1. /login 요청시 username,password body 에 받음.
2. PrincipalDetailsService @Override 메소드 호출
3. PrincipalDetails 를 세션에 저장. -> 세션에 담는 이유 권한관리를 할 방법이 없음.
4. Jwt 토큰을 만들어 응답함.