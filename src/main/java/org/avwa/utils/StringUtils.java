package org.avwa.utils;

import java.security.SecureRandom;

public class StringUtils {

    public static String getRandomString(int len) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static Boolean getAsBoolean(String value) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return false;
        } else {
            switch (value) {
                case "true":
                    return true;
                case "false":
                    return false;
                default:
                    return null;
            }
        }
    }

}
