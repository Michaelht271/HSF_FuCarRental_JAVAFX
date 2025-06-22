package com.michael.fu.hsf301assigment1.repository;

import com.michael.fu.hsf301assigment1.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, CarCustomerID> {


    List<CarRental> findByCustomer_CustomerId(Long customerId);

    List<CarRental> findByRentDateBetween(LocalDate startDate, LocalDate endDate);



    CarRental findByCustomerAndCar(Customer existingCustomer, Car car);
}
