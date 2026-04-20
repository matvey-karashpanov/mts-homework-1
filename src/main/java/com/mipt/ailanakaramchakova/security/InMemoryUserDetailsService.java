package com.mipt.ailanakaramchakova.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final Map<String, UserDetails> userStore;

    public InMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userStore = Map.of(
          "user",
          User.builder().username("user").password(passwordEncoder.encode("password")).roles("USER")
            .build(),
          "reader", User.builder().username("reader").password(passwordEncoder.encode("password"))
            .authorities(
              new SimpleGrantedAuthority("ROLE_USER"),
              new SimpleGrantedAuthority("READ_PRIVILEGE")
            ).build()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userStore.get(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }

    public boolean checkPassword(String username, String rawPassword) {
        UserDetails userDetails = userStore.get(username);
        if (userDetails == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, userDetails.getPassword());
    }
}
