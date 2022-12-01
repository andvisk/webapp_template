package org.avwa.utils;

import java.util.Random;

public class StringUtils {

    public static String getRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
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
