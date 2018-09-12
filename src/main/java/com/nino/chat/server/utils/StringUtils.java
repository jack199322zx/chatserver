package com.nino.chat.server.utils;

/**
 * @author ss
 * @date 2018/9/11 15:45
 */
public class StringUtils {

    public static boolean isNotEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }
}
