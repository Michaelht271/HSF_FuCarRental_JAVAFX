package com.michael.fu.hsf301assigment2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
public class WebMvcConfig extends BaseConfig{

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");

        // Expose Spring beans (like request) to Thymeleaf expressions
        viewResolver.setRedirectContextRelative(Boolean.parseBoolean("requestContext"));
        return viewResolver;
    }
}
