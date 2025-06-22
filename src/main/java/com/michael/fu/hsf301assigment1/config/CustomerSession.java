package com.michael.fu.hsf301assigment1.config;

import com.michael.fu.hsf301assigment1.entity.Customer;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Data
public class CustomerSession {
    private Customer customer;
    private boolean isAuthenticated;


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}