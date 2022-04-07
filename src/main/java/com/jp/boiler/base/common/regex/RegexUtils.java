package com.jp.boiler.base.common.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    public static boolean isPatternMatched(String pattern, String needToVerifyValue){
        Pattern passPattern = Pattern.compile(pattern);
        Matcher matcher = passPattern.matcher(needToVerifyValue);
        return matcher.find();
    }
}
