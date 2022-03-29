package com.jp.boiler.base.service.auth;

import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.common.exception.BoilerException;
import com.jp.boiler.base.controller.param.UserParam;
import com.jp.boiler.base.domain.auth.PrincipalDetails;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

// login 요청시 즉시 동작.
@Service
@AllArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[USER_DETAILS] Security login Auth process start");
        User user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }

    public User userDataHandler(UserParam userParam){
        User user = new ModelMapper().map(userParam,User.class);
        userValidation(user.getUsername());
        user.encodePwd(bCryptPasswordEncoder);
        return saveUser(user);
    }

    private void userValidation(String username){
        User order = userRepository.findByUsername(username);
        if(!ObjectUtils.isEmpty(order)) throw new BoilerException(ResultCode.RESULT_2000);
    }

    private User saveUser(User user){
        return userRepository.save(user);
    }

}
