package com.jp.boiler.base.service.auth.verifier;

import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.common.exception.BoilerException;
import com.jp.boiler.base.common.regex.RegexUtils;
import com.jp.boiler.base.controller.param.user.UserParam;
import com.jp.boiler.base.domain.auth.User;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.properties.PasswordRegularExpressionProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static com.jp.boiler.base.common.code.ResultCode.*;
import static com.jp.boiler.base.common.regex.RegexUtils.isPatternMatched;

@Slf4j
@RequiredArgsConstructor
@Component
public class SignUpDataVerifier {

    private final UserRepository userRepository;

    /*private static final int MIN = 8;
    private static  final int MAX = 20;
    // 영어, 숫자, 특수문자 포함 8 ~ 20자
    private static final String PASSWORD_RULE_REGX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&.])[A-Za-z\\d$@!%*#?&.]{"+MIN+","+MAX+"}$";
    // 같은 알파벳 또는 숫자가 3개 이상 겹칠 수 없음
    private static final String SAME_ALPHABET_NUMBER_PATTERN = "(\\w)\\1\\1";
    // 같은 문자
    private static final String SAME_SYMBOL_PATTERN = "([$@!%*#?&.])\\1\\1";*/
    private final PasswordRegularExpressionProperties regularExpressionProperties;

    public void validateUserParam(UserParam userParam){
        isIdExist(userParam.getUsername());
        isPasswordAppropriate(userParam.getPassword());
    }

    // 향후 회원 존재 여부 확인 API 사용 가능.
    public boolean isIdExist(String username){
        User order = userRepository.findByUsername(username);
        if(!ObjectUtils.isEmpty(order)) throw new BoilerException(ResultCode.RESULT_2000);
        return true;
    }

    public void isPasswordAppropriate(String password){
        checkPasswordMatched(password);
        sameAlphabetAndNumberExist(password);
        sameSymbolExist(password);
    }

    private void checkPasswordMatched(String password){
        if(!isPatternMatched(regularExpressionProperties.getRuleRegex(), password)) throw new BoilerException(RESULT_1001);
    }

    private void sameAlphabetAndNumberExist(String password){
        if(isPatternMatched(regularExpressionProperties.getSameAlphabetPattern(), password)) throw new BoilerException(RESULT_1002);
    }

    private void sameSymbolExist(String password){
        if(isPatternMatched(regularExpressionProperties.getSameSymbolPattern(),password)) throw new BoilerException(RESULT_1002);
    }

}
