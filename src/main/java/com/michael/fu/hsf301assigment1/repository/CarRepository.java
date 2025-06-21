package com.michael.fu.hsf301assigment1.repository;

import com.michael.fu.hsf301assigment1.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
