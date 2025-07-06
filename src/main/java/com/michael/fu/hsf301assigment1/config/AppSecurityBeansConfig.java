package com.michael.fu.hsf301assigment1.config;

import com.michael.fu.hsf301assigment1.service.impl.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppSecurityBeansConfig extends BaseConfig{

    private final CustomerService appUserService;

    public AppSecurityBeansConfig(CustomerService appUserService) {
        this.appUserService = appUserService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        logger.info("Khởi tạo Bean UserDetailsService với CustomerService.");
        return appUserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Khởi tạo Bean PasswordEncoder với BCrypt.");
        return new BCryptPasswordEncoder();
    }
}
