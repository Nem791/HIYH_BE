package com.example.demo.filter;

import com.example.demo.constants.AppConstants;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final boolean securityEnabled;

    // Utility to match URL patterns like /api/auth/**
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    // List of paths to exclude from JWT authentication
    private final List<String> excludedPaths = Arrays.asList(
            "/api/auth/verify-email",
            "/api/auth/signup",
            "/api/auth/resend-verification",
            "/api/auth/pre-signup",
            "/api/auth/logout",
            "/api/auth/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    );


    public JwtAuthFilter(JwtUtil jwtUtil, UserService userService, @Value("${security.enabled:true}") boolean securityEnabled) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.securityEnabled = securityEnabled;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractJwtFromCookie(request);
        // Developer mode logic
        if (!securityEnabled) {
            // Add a dev auth context if there is no token
            if (token == null) {
                CustomUserDetails devUser = userService.loadUserByUsername("dev@example.com");
                UsernamePasswordAuthenticationToken devAuth =
                        new UsernamePasswordAuthenticationToken(
                                devUser, null, devUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(devAuth);
            }
        }

        // Check if the current request path should be excluded from filtering.
        boolean isExcluded = excludedPaths.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));

        // If the path is public, bypass the filter and continue to the next one.
        if (isExcluded) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token != null) {
            try {
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.getAllClaimsFromToken(token).getSubject();
                    CustomUserDetails userDetails = userService.loadUserByUsername(email);

                    String path = request.getServletPath();
                    String method = request.getMethod();
                    boolean noConsentPaths = pathMatcher.match("/api/users/**", path) || pathMatcher.match("/api/auth/me", path);
                    boolean isAllowedMethod = AppConstants.HTTP_METHOD_PUT.equalsIgnoreCase(method) || AppConstants.HTTP_METHOD_GET.equalsIgnoreCase(method);

                    if (Boolean.FALSE.equals(userDetails.getConsented())) {
                        if (!(noConsentPaths && isAllowedMethod)) {
                            throw new UsernameNotFoundException("User has not consent");
                        }
                    }

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException ex) {
                // If token is invalid, clear context to be safe
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (AppConstants.LOGIN_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}