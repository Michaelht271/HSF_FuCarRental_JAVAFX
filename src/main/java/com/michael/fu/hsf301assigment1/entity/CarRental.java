package com.michael.fu.hsf301assigment1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity // ← BẮT BUỘC PHẢI CÓ
@Table(name ="CarRental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CarRental {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "CarRentalId")
    private long carRentalId;

    @ManyToOne
    @JoinColumn(name = "carId")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @Column(name = "RentDate", nullable = false)
    private LocalDate rentDate;

    @Column(name = "ReturnDate", nullable = false)
    private LocalDate returnDate;


    @Column(name="RentPrice", columnDefinition = "DECIMAL(18,2)")
    private double rentPrice;

    @Enumerated(EnumType.STRING)
    @Column(name="Status", columnDefinition = "NVARCHAR(10)", nullable = false)
    private CarRentalStatus status;

}
