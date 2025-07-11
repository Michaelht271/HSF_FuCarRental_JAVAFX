package com.michael.fu.hsf301assigment2.dto;

import com.michael.fu.hsf301assigment2.entity.Car;
import com.michael.fu.hsf301assigment2.entity.Customer;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
public class ReportResultDTO {
    private LocalDate rentDate;
    private LocalDate returnDate;
    private int totalDays;
    private double rentPrice;
    private String status;
    private Customer customer;
    private Car car;
    // getter/setter
}
