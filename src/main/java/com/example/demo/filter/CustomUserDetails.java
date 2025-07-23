package com.example.demo.filter;

import com.example.demo.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user; // Your User entity

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public String getId() {
        return user.getId();
    }
    public Boolean getConsented() {
        return user.getConsented();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

}
