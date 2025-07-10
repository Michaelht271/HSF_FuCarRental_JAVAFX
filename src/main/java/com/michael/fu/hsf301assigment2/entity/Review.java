package com.michael.fu.hsf301assigment2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Review")
@NoArgsConstructor
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID")
    private long reviewId;
    @ManyToOne

    @JoinColumn(name = "carId")
    private Car car;

    @ManyToOne
               // ĐÚNG tên thuộc tính trong CarCustomerID
    @JoinColumn(name = "customerId")
    private Customer customer;

    private double reviewStar;

    @Column(columnDefinition = "NVARCHAR(250)")
    private String comment;

}