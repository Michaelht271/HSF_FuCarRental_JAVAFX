package com.michael.fu.hsf301assigment2.fxcontroller.customer;

import com.michael.fu.hsf301assigment2.entity.Car;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

@Controller
public class CarDetailControllerFX {

    @FXML private Label lblName;
    @FXML private Label lblYear;
    @FXML private Label lblColor;
    @FXML private Label lblCapacity;
    @FXML private Label lblProducer;
    @FXML private Label lblPrice;

    private Car car;

    public void setCar(Car car) {
        this.car = car;
        showInfo();
    }

    private void showInfo() {
        if (car == null) return;
        lblName.setText(car.getCarName());
        lblYear.setText(String.valueOf(car.getCarModelYear()));
        lblColor.setText(car.getColor());
        lblCapacity.setText(String.valueOf(car.getCapacity()));
        lblProducer.setText(car.getProducer() != null ? car.getProducer().getProducerName() : "");
        lblPrice.setText(String.format("%,.0f", car.getRentPrice()));
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) lblName.getScene().getWindow();
        stage.close();
    }
}
