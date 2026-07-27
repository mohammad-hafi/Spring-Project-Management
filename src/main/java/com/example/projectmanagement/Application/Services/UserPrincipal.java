package com.example.projectmanagement.Application.Services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public record UserPrincipal(Long id, String email, Set<String> permissions) implements UserDetails {
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream().map(SimpleGrantedAuthority::new).toList();
    }
    @Override public String getPassword() { return ""; }
    @Override public String getUsername() { return email; }
}
