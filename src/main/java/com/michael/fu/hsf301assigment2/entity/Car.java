package com.michael.fu.hsf301assigment2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name ="Car")
@NoArgsConstructor
@Getter
@Setter

public class Car {
    //(CarID, CarName, CarModelYear, Color, Capacity, Description, ImportDate, ProducerID, RentPrice, Status)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CarID")
    private Long carId;

    @Column(name = "CarName", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String carName;

    @Column(name = "CarModelYear", columnDefinition = "INT")
    private Integer carModelYear;

    @Column(name="Color", columnDefinition = "NVARCHAR(20)")
    private String color;

    @Column(name="Capacity", columnDefinition = "INT")
    private Integer capacity;

    @Column(name="CarDescription", columnDefinition = "NVARCHAR(200)")
    private String description;

    @Column(name="ImportDate", columnDefinition = "DATETIME")
    private LocalDate importDate;

    @Column(name="RentPrice", columnDefinition = "INT")
    private double rentPrice;

    @Enumerated(EnumType.STRING)
    @Column(name="Status", columnDefinition = "NVARCHAR(10)", nullable = false)
    private CarStatus status;

    @ManyToOne
    @JoinColumn(name = "ProducerID")
    private Producer producer;

    // Relationships
    @OneToMany(mappedBy = "car",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarRental> carRentals = new ArrayList<>();

    @OneToMany(mappedBy ="car",cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private List<Review> reviews = new ArrayList<>();


    public void addCarRental(CarRental carRental) {
        if (carRentals == null) {
            carRentals = new ArrayList<>();
        }
        carRentals.add(carRental);
        carRental.setCar(this); // nếu bạn chưa gọi ở ngoài
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setCar(this);
    }



}
