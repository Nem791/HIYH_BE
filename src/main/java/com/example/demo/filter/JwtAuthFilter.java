package com.example.demo.filter;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final boolean securityEnabled;

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

        if (token != null) {
            try {
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.getAllClaimsFromToken(token).getSubject();
                    CustomUserDetails userDetails = userService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException ex) {
                // Let global handler take care of logging/response
                SecurityContextHolder.clearContext();
            }
        }

        // If no authentication yet AND we're in dev mode, set a fixed dev user!
        if (!securityEnabled && token == null) {
            CustomUserDetails devUser = userService.loadUserByUsername("test@example.com"); // your dev user
            UsernamePasswordAuthenticationToken devAuth =
                    new UsernamePasswordAuthenticationToken(
                            devUser, null, devUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(devAuth);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
