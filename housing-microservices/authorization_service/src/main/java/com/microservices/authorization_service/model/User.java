package com.microservices.authorization_service.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private String username;
    private String password;
    private boolean enabled;
    private int role;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = null;
        switch (this.role) {
            case 0:
                role = "ADMIN";
                break;
            case 1:
                role = "OWNER";
                break;
            case 2:
                role = "TENANT";
                break;
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
