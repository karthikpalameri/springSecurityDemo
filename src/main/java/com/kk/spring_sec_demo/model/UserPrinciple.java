package com.kk.spring_sec_demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrinciple implements UserDetails {
    private final User user;

    public UserPrinciple(User user) {
        this.user = user;
    }

    /**
     * Return a collection of {@link GrantedAuthority}, where each represents a user permission.
     * For simplicity, all users are given the {@code USER} authority.
     *
     * @return a collection of authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    /**
     * Returns the password used to authenticate the user, prefixed with {noop} if plaintext.
     *
     * @return the password used to authenticate the user
     */
    @Override
    public String getPassword() {
//        return "{noop}" + user.getPassword(); // Prefix with {noop} if password is plaintext
        return user.getPassword();
//        return "{bcrypt}" + user.getPassword(); //no need of this if encoder is properly configured
    }

    /**
     * Return the username used to identify the user.
     *
     * @return the username used to identify the user
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return true;
    }
}
