package com.mipt.ailanakaramchakova.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProfileAndDocsController {

    @GetMapping("/profile")
    public String profile(Authentication authentication) {
        return "Welcome, " + authentication.getName();
    }

    @GetMapping("/docs")
    public String docs(Authentication authentication) {
        return "Secret documentation for " + authentication.getName();
    }
}
