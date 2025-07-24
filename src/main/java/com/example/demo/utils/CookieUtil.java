package com.example.demo.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    private static final boolean COOKIE_SECURE = Boolean.parseBoolean(System.getenv("COOKIE_SECURE"));

    public static Cookie clearCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(COOKIE_SECURE); // Set based on env
        cookie.setAttribute("SameSite", "None");  // Use None for cross-site
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public static Cookie createHttpOnlyCookie(String name, String value, long maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(COOKIE_SECURE); // Set based on env
        cookie.setAttribute("SameSite", "None");
        cookie.setPath("/");
        cookie.setMaxAge((int) (maxAgeSeconds / 1_000L));
        return cookie;
    }
}