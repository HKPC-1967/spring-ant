package org.hkpc.dtd.common.utils;

import org.springframework.util.AntPathMatcher;

import java.util.List;

public class CompareUtil {

    /**
     * noneMatch
     * <p>
     * In the context of AntPathMatcher, the difference between ** and * is as follows:
     * * matches zero or more characters within a single directory level.
     * ** matches zero or more directories in a path.
     */
    public static boolean noneMatch(List<String> urls, String targetUrl){
        return urls.stream().noneMatch(pattern -> new AntPathMatcher().match(pattern, targetUrl));
    }

    /**
     * anyMatch
     * <p>
     * In the context of AntPathMatcher, the difference between ** and * is as follows:
     * * matches zero or more characters within a single directory level.
     * ** matches zero or more directories in a path.
     */
    public static boolean anyMatch(List<String> urls, String targetUrl){
        return urls.stream().anyMatch(pattern -> new AntPathMatcher().match(pattern, targetUrl));
    }

}
