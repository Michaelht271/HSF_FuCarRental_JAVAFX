package com.michael.fu.hsf301assigment2.repository;

import com.michael.fu.hsf301assigment2.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, Long> {


    List<CarRental> findByCustomer_CustomerId(Long customerId);

    List<CarRental> findByRentDateBetween(LocalDate startDate, LocalDate endDate);



    CarRental findByCustomerAndCar(Customer existingCustomer, Car car);

    boolean existsByCarAndStatusInAndReturnDateAfter(Car existingCar, List<CarRentalStatus> pending, LocalDate pickup);

    long countByRentDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT SUM(c.rentPrice) FROM CarRental c WHERE c.rentDate BETWEEN :start AND :end")
    BigDecimal sumRentPriceBetween(LocalDate start, LocalDate end);

}
