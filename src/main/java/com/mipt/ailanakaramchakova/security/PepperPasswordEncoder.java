package com.mipt.ailanakaramchakova.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

public class PepperPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder delegate;
    private final String pepper;

    public PepperPasswordEncoder(PasswordEncoder delegate, String pepper) {
        this.delegate = Objects.requireNonNull(delegate);
        this.pepper = Objects.requireNonNull(pepper);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(rawPassword.toString() + pepper);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword.toString() + pepper, encodedPassword);
    }
}
