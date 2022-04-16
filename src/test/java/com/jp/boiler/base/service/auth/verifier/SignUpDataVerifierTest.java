package com.jp.boiler.base.service.auth.verifier;

import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.common.exception.BoilerException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jp.boiler.base.common.code.ResultCode.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class SignUpDataVerifierTest {

    @Autowired
    private SignUpDataVerifier signUpDataVerifier;

    private static final Set<String> MATCHED_PASSWORDS = new HashSet<>();
    private static final List<String> SAME_ALPH_AND_SYMBOL_VIOLATION_PASSWORDS = new ArrayList<>();
    private static final List<String> BLANK_VIOLATION_PASSWORDS = new ArrayList<>();
    private static final String ANOTHER_SYMBOL_CASE = "iansystemA!212*5️⃣";

    static {
        // 정상 통과패턴
        MATCHED_PASSWORDS.add("abcdefgHiJK12!@");
        MATCHED_PASSWORDS.add("asefgHiJK12!@");
        MATCHED_PASSWORDS.add("tryCatch12#@");
        MATCHED_PASSWORDS.add("iansystemA!212*");

        // 문자 또는 알파벳이 연속으로 등장하는 패턴
        SAME_ALPH_AND_SYMBOL_VIOLATION_PASSWORDS.add("abcdefffHiJK12!@");
        SAME_ALPH_AND_SYMBOL_VIOLATION_PASSWORDS.add("aaafgHiJK12!@");
        SAME_ALPH_AND_SYMBOL_VIOLATION_PASSWORDS.add("tryCatccc12#@");

        // 공백 문자열이 포함된 패턴
        BLANK_VIOLATION_PASSWORDS.add("abcdef HiJK12!@");
        BLANK_VIOLATION_PASSWORDS.add("as fgHiJK12!@");
        BLANK_VIOLATION_PASSWORDS.add("tryCatc 12#@");
        BLANK_VIOLATION_PASSWORDS.add("ians stemA!212*");
        // 다른문자열 유입 케이스
    }

    @Test
    @DisplayName("비밀번호 유효성테스트")
    void password_check_test(){

        BoilerException symbolViolationCase1 = assertThrows(BoilerException.class,() ->
                signUpDataVerifier.isPasswordAppropriate(SAME_ALPH_AND_SYMBOL_VIOLATION_PASSWORDS.get(0)));

        BoilerException symbolViolationCase2 = assertThrows(BoilerException.class,() ->
                signUpDataVerifier.isPasswordAppropriate(SAME_ALPH_AND_SYMBOL_VIOLATION_PASSWORDS.get(1)));

        BoilerException symbolViolationCase3 = assertThrows(BoilerException.class,() ->
                signUpDataVerifier.isPasswordAppropriate(SAME_ALPH_AND_SYMBOL_VIOLATION_PASSWORDS.get(2)));

        ///////////////

        BoilerException blankViolationCase1 = assertThrows(BoilerException.class,() ->
                signUpDataVerifier.isPasswordAppropriate(BLANK_VIOLATION_PASSWORDS.get(0)));

        BoilerException blankViolationCase2 = assertThrows(BoilerException.class,() ->
                signUpDataVerifier.isPasswordAppropriate(BLANK_VIOLATION_PASSWORDS.get(1)));

        BoilerException blankViolationCase3 = assertThrows(BoilerException.class,() ->
                signUpDataVerifier.isPasswordAppropriate(BLANK_VIOLATION_PASSWORDS.get(2)));

        BoilerException blankViolationCase4 = assertThrows(BoilerException.class,() ->
                signUpDataVerifier.isPasswordAppropriate(BLANK_VIOLATION_PASSWORDS.get(3)));

        BoilerException anotherSymbolCase1 = assertThrows(BoilerException.class,() ->
                signUpDataVerifier.isPasswordAppropriate(ANOTHER_SYMBOL_CASE));

        Assertions.assertAll(
                // 정상 패스워드 인증 테스트
                () -> assertDoesNotThrow(()->{
                    MATCHED_PASSWORDS.forEach(password -> {
                        signUpDataVerifier.isPasswordAppropriate(password);
                    });
                },"패스워드 정상 인증 케이스"),

                () -> assertEquals(RESULT_1002,symbolViolationCase1.getResultCode(),"반복되는 문자열 케이스1"),
                () -> assertEquals(RESULT_1002,symbolViolationCase2.getResultCode(),"반복되는 문자열 케이스2"),
                () -> assertEquals(RESULT_1002,symbolViolationCase3.getResultCode(),"반복되는 문자열 케이스3"),

                () -> assertEquals(RESULT_1001,blankViolationCase1.getResultCode(),"공백문자 케이스1"),
                () -> assertEquals(RESULT_1001,blankViolationCase2.getResultCode(),"공백문자 케이스2"),
                () -> assertEquals(RESULT_1001,blankViolationCase3.getResultCode(),"공백문자 케이스3"),
                () -> assertEquals(RESULT_1001,blankViolationCase4.getResultCode(),"공백문자 케이스4"),

                () -> assertEquals(RESULT_1001,anotherSymbolCase1.getResultCode(),"다른 문자열 유입케이스")
        );
    }
}