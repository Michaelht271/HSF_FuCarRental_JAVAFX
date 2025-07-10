package com.michael.fu.hsf301assigment2.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarRentalDTO {
    @NotBlank(message = "Email không được để trống")
    private String customerEmail;

    @NotNull(message = "ID xe không được để trống")
    private Long carId;

    @NotBlank(message = "Ngày nhận xe không được để trống")
    private String pickupDate;

    @NotBlank(message = "Ngày trả xe không được để trống")
    private String returnDate;

    @NotBlank(message = "Giá trị không được để trống")
    private double rentPrice;
}
