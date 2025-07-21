package com.example.demo.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie clearCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set true in prod
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public static Cookie createHttpOnlyCookie(String name, String value, long maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int)(maxAgeSeconds / 1_000L));
        return cookie;
    }
}
