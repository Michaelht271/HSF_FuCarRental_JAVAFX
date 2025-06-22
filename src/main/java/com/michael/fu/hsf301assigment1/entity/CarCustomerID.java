package com.michael.fu.hsf301assigment1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class CarCustomerID implements Serializable {
    @Column(name="CarID")
    private Long carId;
    @Column(name="CustomerId")
    private Long customerId;
}
