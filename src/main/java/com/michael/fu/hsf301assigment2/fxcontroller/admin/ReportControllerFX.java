package com.michael.fu.hsf301assigment2.fxcontroller.admin;

import com.michael.fu.hsf301assigment2.entity.CarRental;
import com.michael.fu.hsf301assigment2.service.impl.ReportService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Component
public class ReportControllerFX {

    Logger logger = LoggerFactory.getLogger(ReportControllerFX.class);

    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private TableView<CarRental> reportTable;

    @FXML private TableColumn<CarRental, String> colCustomer, colCar, colRent, colReturn, colStatus;
    @FXML private TableColumn<CarRental, Integer> colDays;
    @FXML private TableColumn<CarRental, Double> colAmount;

    private final ReportService reportService;

    @Autowired
    public ReportControllerFX(ReportService reportService) {
        this.reportService = reportService;
    }

    @FXML
    public void initialize() {
        reportTypeCombo.setItems(FXCollections.observableArrayList("summary", "detailed", "revenue", "customer"));
        reportTypeCombo.getSelectionModel().select("summary");

        colCustomer.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCustomer() != null ? data.getValue().getCustomer().getCustomerName() : ""));

        colCar.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCar() != null ? data.getValue().getCar().getCarName() : ""));

        colRent.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRentDate() != null ? data.getValue().getRentDate().toString() : ""));

        colReturn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getReturnDate() != null ? data.getValue().getReturnDate().toString() : ""));

        colDays.setCellValueFactory(data -> {
            if (data.getValue().getRentDate() != null && data.getValue().getReturnDate() != null) {
                long days = ChronoUnit.DAYS.between(data.getValue().getRentDate(), data.getValue().getReturnDate());
                return new SimpleIntegerProperty((int) days).asObject();
            }
            return new SimpleIntegerProperty(0).asObject();
        });

        colAmount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getRentPrice()).asObject());

        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().name()));
    }

    @FXML
    public void handleGenerateReport() {
        LocalDate from = startDatePicker.getValue();
        LocalDate to = endDatePicker.getValue();
        logger.info("from: {}, to: {}", from, to);

        if (from == null || to == null || from.isAfter(to)) {
            showAlert("Lỗi", "Vui lòng chọn khoảng thời gian hợp lệ!");
            return;
        }

        try {
            Map<String, Object> reportData = reportService.generateReport(from, to);
            Object rawResults = reportData.get("reportResults");

            if (rawResults instanceof List<?> rawList && !rawList.isEmpty() && rawList.get(0) instanceof CarRental) {
                List<CarRental> results = (List<CarRental>) rawList;
                reportTable.setItems(FXCollections.observableArrayList(results));
                logger.info("Report loaded with {} items.", results.size());
            } else {
                reportTable.setItems(FXCollections.emptyObservableList());
                showAlert("Thông báo", "Không có dữ liệu báo cáo.");
            }

        } catch (Exception e) {
            logger.error("Error generating report", e);
            showAlert("Lỗi", "Không thể tạo báo cáo: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
