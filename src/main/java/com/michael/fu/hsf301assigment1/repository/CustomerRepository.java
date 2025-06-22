package com.michael.fu.hsf301assigment1.repository;

import com.michael.fu.hsf301assigment1.entity.CarRental;
import com.michael.fu.hsf301assigment1.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

}
