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
@ToString
public class CarRental {

    @EmbeddedId
    private CarCustomerID id;

    @ManyToOne
    @MapsId("carId")
    @JoinColumn(name = "carId")
    private Car car;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customerId")
    private Customer customer;

    @Temporal(TemporalType.DATE)
    @Column(name = "RentDate")// tốt hơn nên thêm
    private LocalDate rentDate;


    @Temporal(TemporalType.DATE)
    @Column(name = "ReturnDate")
    private LocalDate returnDate;

    @Column(name="RentPrice", columnDefinition = "DECIMAL(18,2)")
    private double rentPrice;

    @Enumerated(EnumType.STRING)
    @Column(name="Status", columnDefinition = "NVARCHAR(10)", nullable = false)
    private CarRentalStatus status;

}
