package com.michael.fu.hsf301assigment2.repository;

import com.michael.fu.hsf301assigment2.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    Customer findByCustomerId(Long id);
}
