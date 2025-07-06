package com.michael.fu.hsf301assigment1.config;

import com.michael.fu.hsf301assigment1.entity.Customer;
import com.michael.fu.hsf301assigment1.service.impl.CustomerService;
import com.michael.fu.hsf301assigment1.session.CustomerSession;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final CustomerService customerService;
    private final CustomerSession customerSession;

    public MyAuthenticationSuccessHandler(CustomerService customerService, CustomerSession customerSession) {
        this.customerService = customerService;
        this.customerSession = customerSession;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = authentication.getName();
        Customer customer = customerService.findCustomerByEmail(email);
        if (customer != null) {
            customerSession.setCustomer(customer);
            customerSession.setAuthenticated(true);
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                response.sendRedirect("/api/v1/admin/dashboard");
                return;
            } else if ("ROLE_USER".equals(authority.getAuthority())) {
                response.sendRedirect("/api/v1/customers/home");
                return;
            }
        }
        response.sendRedirect("/api/v1/public/");
    }
}

