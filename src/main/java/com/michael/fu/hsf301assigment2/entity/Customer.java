package com.michael.fu.hsf301assigment2.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Customer")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerId")
    private Long customerId;

    @Column(name = "CustomerName", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String customerName;

    @Column(name = "Mobile", columnDefinition = "NVARCHAR(11)", nullable = false)
    private String mobile;

    @Column(name = "Birthday", columnDefinition = "DATETIME")
    private LocalDate birthday;

    @Column(name = "IdentifyCard", columnDefinition = "NVARCHAR(20)")
    private String identifyCard;

    @Column(name = "LinceNumber", columnDefinition = "NVARCHAR(20)")
    private String licenceNumber;

    @Column(name = "Email", columnDefinition = "NVARCHAR(50)")
    private String email;
    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "otp_expiration")
    private LocalDateTime otpExpiration;

    @Column(name = "LicenceDate")
    private LocalDate licenceDate;
    @Column(name = "Password", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String password;

    private String generationToken;
    private boolean isEmailVerified;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AccountId")
    private Account account;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarRental> carRentals = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    public void addCarRental(CarRental carRental) {
        carRentals.add(carRental);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }


    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }
}
