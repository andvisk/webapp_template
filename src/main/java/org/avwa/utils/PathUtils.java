package org.avwa.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PathUtils {

    public static String removeTrailingSlash(String string) {
        if (string.endsWith("/"))
            string = string.substring(0, string.length() - 1);
        return string;
    }

    public static String removeLeadingSlash(String string) {
        if (string.startsWith("/"))
            string = string.substring(1);
        return string;
    }

    public static String removeLeadingAndTrailingSlashes(String string) {
        string = removeLeadingSlash(string);
        string = removeTrailingSlash(string);
        return string;
    }

    // parameter set = dirs from servletContext, page = page to check
    public static boolean containsPage(Set<String> set, String page) {
        for (String uri : set) {
            if (uri.endsWith("/" + page)) {
                return true;
            }
        }
        return false;
    }

    public static boolean accessingOneOfThePaths(String targetPath, List<String> listPaths) {
        List<String> targetPathLvls = getPathLevels(targetPath);
        for (String path : listPaths) {
            if (accessingPath(targetPathLvls, path)) {
                return true;
            }
        }
        return false;
    }

    public static boolean accessingPath(String targetPath, String checkPath) {
        return accessingPath(getPathLevels(targetPath), checkPath);
    }

    public static boolean accessingPath(List<String> targetPathLvls, String checkPath) {
        List<String> pathLvls = getPathLevels(checkPath);
        boolean same = true;
        for (int i = 0; i < targetPathLvls.size() && i < pathLvls.size(); i++) {
            if (!targetPathLvls.get(i).equals(pathLvls.get(i))) {
                same = false;
                break;
            }
        }
        if (same)
            return true;
        return false;
    }

    public static List<String> getPathLevels(String path) {
        path = PathUtils.removeLeadingSlash(path);
        path = PathUtils.removeTrailingSlash(path);
        return Arrays.asList(path.split("/"));
    }
}
