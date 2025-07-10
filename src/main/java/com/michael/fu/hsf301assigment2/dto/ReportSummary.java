package com.michael.fu.hsf301assigment2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummary {
    private long totalTransactions;
    private double totalRevenue;
    private double completionRate; // in percentage, 0 - 100
    private String mostRentedCar;
}