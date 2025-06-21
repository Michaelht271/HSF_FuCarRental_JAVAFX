package com.michael.fu.hsf301assigment1.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Review")
@NoArgsConstructor
@Getter
@Setter
public class Review {

    @EmbeddedId
    private CarCustomerID id;
    @ManyToOne
    @MapsId("carId")
    @JoinColumn(name = "carId")
    private Car car;

    @ManyToOne
    @MapsId("customerId")            // ĐÚNG tên thuộc tính trong CarCustomerID
    @JoinColumn(name = "customerId")
    private Customer customer;

    private double reviewStar;

    @Column(columnDefinition = "NVARCHAR(250)")
    private String comment;

}