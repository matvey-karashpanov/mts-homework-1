package com.mipt.ailanakaramchakova;

import com.mipt.ailanakaramchakova.security.JwtProperties;
import com.mipt.ailanakaramchakova.security.PasswordProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, PasswordProperties.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
