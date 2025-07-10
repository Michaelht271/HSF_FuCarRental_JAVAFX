package com.michael.fu.hsf301assigment2.session;

import com.michael.fu.hsf301assigment2.entity.Customer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Data
@Getter
@Setter
public class CustomerSession {
    private Customer customer;
    private boolean isAuthenticated;

    public void clear() {
        this.customer = null;
        this.isAuthenticated = false;
    }
}
