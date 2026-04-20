package com.mipt.ailanakaramchakova.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();
        if (token.length() > 12) {
            String maskedToken =
              token.substring(0, 6) + "..." + token.substring(token.length() - 6);
            log.info("Processing JWT: {}", maskedToken);
        }

        try {
            Jws<Claims> claimsJws = jwtUtils.parseAndValidate(token);
            String username = claimsJws.getPayload().getSubject();
            String roles = claimsJws.getPayload().get("roles", String.class);

            List<SimpleGrantedAuthority> authorities = roles != null
              ? Arrays.stream(roles.split(","))
              .map(String::trim)
              .map(r -> r.equals("READ_PRIVILEGE")
                ? new SimpleGrantedAuthority(r)
                : new SimpleGrantedAuthority("ROLE_" + r))
              .toList()
              : List.of();

            UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception exception) {
            log.error("JWT validation failed: {}", exception.getMessage(), exception);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
              "{\"error\":\"Invalid token\",\"message\":\"" + exception.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
