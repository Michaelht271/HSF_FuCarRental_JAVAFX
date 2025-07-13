package com.michael.fu.hsf301assigment2.fxcontroller.customer;

import com.michael.fu.hsf301assigment2.dto.CarRentalDTO;
import com.michael.fu.hsf301assigment2.entity.Car;
import com.michael.fu.hsf301assigment2.service.impl.CarRentalService;
import com.michael.fu.hsf301assigment2.session.CustomerSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import javafx.beans.value.ChangeListener;


@Controller
@RequiredArgsConstructor
public class RentCarControllerFX {

    private final CarRentalService carRentalService;
    private final CustomerSession customerSession;

    @FXML private Label lblCar;
    @FXML private Label lblPrice;
    @FXML private DatePicker dpPickup;
    @FXML private DatePicker dpReturn;
    @FXML private Label lblDays;
    @FXML private Label lblTotal;

    private Car car;

    public void setCar(Car car) {
        this.car = car;
        lblCar.setText(car.getCarName());
        lblPrice.setText(String.format("%,.0f", car.getRentPrice()));
        // Đặt ngày nhận mặc định là hôm nay, không cho chọn quá khứ
        dpPickup.setValue(LocalDate.now());
        dpPickup.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });
        dpReturn.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                LocalDate minDate = dpPickup.getValue() != null ? dpPickup.getValue() : LocalDate.now();
                setDisable(empty || item.isBefore(minDate));
            }
        });
        // Lắng nghe thay đổi ngày để cập nhật tổng kết và cập nhật logic ngày trả
        ChangeListener<Object> summaryListener = (obs, oldVal, newVal) -> {
            updateSummary();
            dpReturn.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    LocalDate minDate = dpPickup.getValue() != null ? dpPickup.getValue() : LocalDate.now();
                    setDisable(empty || item.isBefore(minDate));
                }
            });
        };
        dpPickup.valueProperty().addListener(summaryListener);
        dpReturn.valueProperty().addListener(summaryListener);
    }

    private void updateSummary() {
        LocalDate pickup = dpPickup.getValue();
        LocalDate ret = dpReturn.getValue();
        if (pickup != null && ret != null && !pickup.isAfter(ret)) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(pickup, ret) + 1;
            lblDays.setText(days + " ngày");
            double total = days * car.getRentPrice();
            lblTotal.setText(String.format("%,.0f VNĐ", total));
        } else {
            lblDays.setText("0 ngày");
            lblTotal.setText("0 VNĐ");
        }
    }

    @FXML
    private void confirmRent() {
        if (dpPickup.getValue() == null || dpReturn.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn ngày nhận và trả.");
            return;
        }
        if (dpPickup.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Ngày nhận không được ở quá khứ.");
            return;
        }
        if (dpPickup.getValue().isAfter(dpReturn.getValue())) {
            showAlert(Alert.AlertType.ERROR, "Ngày nhận phải trước hoặc bằng ngày trả.");
            return;
        }
        try {
            CarRentalDTO dto = new CarRentalDTO();
            dto.setCustomerEmail(customerSession.getCustomer().getEmail());
            dto.setCarId(car.getCarId());
            dto.setPickupDate(dpPickup.getValue().toString());
            dto.setReturnDate(dpReturn.getValue().toString());
                        carRentalService.rentailCar(dto);
            showAlert(Alert.AlertType.INFORMATION, "Đặt xe thành công! Vui lòng chờ xác nhận.");
            close();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) lblCar.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
