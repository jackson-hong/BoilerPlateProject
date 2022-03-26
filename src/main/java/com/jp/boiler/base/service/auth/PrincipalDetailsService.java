package com.jp.boiler.base.service.auth;

import com.jp.boiler.base.domain.auth.PrincipalDetails;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// login 요청시 즉시 동작.
@Service
@AllArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[USER_DETAILS] Security login Auth process start");
        User user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }
}
