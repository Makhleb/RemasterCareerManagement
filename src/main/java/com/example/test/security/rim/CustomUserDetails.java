package com.example.test.security.rim;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchConnectionDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Created on 2024-12-30 by 구경림
 */
@Getter
@Builder
public class CustomUserDetails implements UserDetails {
    private String username; // userId 또는 companyId
    private String password;
    private String role;    // ROLE_USER, ROLE_COMPANY, ROLE_ADMIN
    private boolean isActive;
    private String name;    // 사용자 이름 또는 기업명 추가
    
    public CustomUserDetails(String username, String password, String role, boolean isActive, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.name = name;
    }

    @Builder
    public CustomUserDetails(String username, String password, String role, boolean isActive, String name, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
} 