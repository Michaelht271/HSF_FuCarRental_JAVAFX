package com.michael.fu.hsf301assigment1.session;

import com.michael.fu.hsf301assigment1.entity.Customer;
import lombok.*;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Data
@Getter
public class CustomerSession {
    private Customer customer;
    private boolean isAuthenticated;

}