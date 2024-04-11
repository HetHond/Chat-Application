package com.hethond.chatbackend.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    USER,
    ADMIN;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + name());
    }
}
