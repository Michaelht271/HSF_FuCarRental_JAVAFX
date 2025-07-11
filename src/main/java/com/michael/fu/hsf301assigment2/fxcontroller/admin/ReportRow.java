package com.michael.fu.hsf301assigment2.fxcontroller.admin;

import javafx.beans.property.*;

public class ReportRow {
    private final StringProperty customerName;
    private final StringProperty carName;
    private final StringProperty rentDate;
    private final StringProperty returnDate;
    private final IntegerProperty totalDays;
    private final StringProperty rentPrice;
    private final StringProperty status;

    public ReportRow(String customerName, String carName, String rentDate, String returnDate, int totalDays, String rentPrice, String status) {
        this.customerName = new SimpleStringProperty(customerName);
        this.carName = new SimpleStringProperty(carName);
        this.rentDate = new SimpleStringProperty(rentDate);
        this.returnDate = new SimpleStringProperty(returnDate);
        this.totalDays = new SimpleIntegerProperty(totalDays);
        this.rentPrice = new SimpleStringProperty(rentPrice);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty customerNameProperty() { return customerName; }
    public StringProperty carNameProperty() { return carName; }
    public StringProperty rentDateProperty() { return rentDate; }
    public StringProperty returnDateProperty() { return returnDate; }
    public IntegerProperty totalDaysProperty() { return totalDays; }
    public StringProperty rentPriceProperty() { return rentPrice; }
    public StringProperty statusProperty() { return status; }
}
