package com.zeketian.plugin.pojogenerator.utils;

import java.util.regex.Pattern;

/**
 * @author zeke
 * @description 校验器
 * @date created in 2022/10/7 23:45
 */
public class Validator {

    private static final String REGEX_PACKAGE = "^[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*$";

    public static boolean isPackage(String packageName) {
        return Pattern.matches(REGEX_PACKAGE, packageName);
    }
}
