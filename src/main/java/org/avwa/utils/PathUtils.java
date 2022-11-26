package org.avwa.utils;

import java.util.Set;

public class PathUtils {

    public static String removeTrailingSlash(String string) {
        if (string.endsWith("/"))
            string = string.substring(0, string.length() - 1);
        return string;
    }

    public static boolean containsPage(Set<String> set, String page) {
        for (String uri : set) {
            if (uri.endsWith("/" + page)) {
                return true;
            }
        }
        return false;
    }
}
