package com.michael.fu.hsf301assigment2.fxcontroller.admin;

import com.michael.fu.hsf301assigment2.entity.Car;
import com.michael.fu.hsf301assigment2.entity.CarStatus;
import com.michael.fu.hsf301assigment2.entity.Producer;
import com.michael.fu.hsf301assigment2.service.impl.CarService;
import com.michael.fu.hsf301assigment2.service.impl.ProducerService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddCarControllerFX {

    @FXML private TextField carNameField;
    @FXML private TextField yearField;
    @FXML private TextField colorField;
    @FXML private TextField capacityField;
    @FXML private TextField priceField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<Producer> producerComboBox;
    @FXML private ComboBox<CarStatus> statusComboBox;
    @FXML private Label formTitle;

    private final CarService carService;
    private final ProducerService producerService;

    // Controller cha để gọi lại loadData sau khi thêm xe
    private CarManagementControllerFX parentController;
    private Car carToEdit;

    public void setCarToEdit(Car carToEdit) {
        this.carToEdit = carToEdit;
    }

    @Autowired
    public AddCarControllerFX(CarService carService, ProducerService producerService) {
        this.carService = carService;
        this.producerService = producerService;
    }

    public void setParentController(CarManagementControllerFX parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        // Load danh sách hãng xe
        producerComboBox.setItems(FXCollections.observableArrayList(producerService.findAll()));

        producerComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Producer producer) {
                return producer != null ? producer.getProducerName() : "";
            }

            @Override
            public Producer fromString(String string) {
                return null; // Không dùng
            }
        });

        // Load danh sách trạng thái
        statusComboBox.setItems(FXCollections.observableArrayList(CarStatus.values()));
    }

    @FXML
    public void onSave() {
        try {
            if (validateInput()) {
                Car car = (carToEdit != null) ? carToEdit : new Car();
                car.setCarName(carNameField.getText());
                car.setCarModelYear(Integer.parseInt(yearField.getText()));
                car.setColor(colorField.getText());
                car.setCapacity(Integer.parseInt(capacityField.getText()));
                car.setRentPrice(Double.parseDouble(priceField.getText()));
                car.setDescription(descriptionArea.getText());
                car.setProducer(producerComboBox.getValue());
                car.setStatus(statusComboBox.getValue());

                carService.save(car);

                // Gọi lại loadData() từ controller cha nếu có
                if (parentController != null) {
                    parentController.loadData();
                }

                closeWindow();
            }
        } catch (NumberFormatException e) {
            showError("Số chỗ, năm sản xuất và giá phải là số hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Đã xảy ra lỗi khi lưu xe: " + e.getMessage());
        }
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private boolean validateInput() {
        if (carNameField.getText().isEmpty()
                || yearField.getText().isEmpty()
                || colorField.getText().isEmpty()
                || capacityField.getText().isEmpty()
                || priceField.getText().isEmpty()
                || producerComboBox.getValue() == null
                || statusComboBox.getValue() == null) {
            showError("Vui lòng điền đầy đủ thông tin.");
            return false;
        }

        try {
            Integer.parseInt(yearField.getText());
            Integer.parseInt(capacityField.getText());
            Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            showError("Năm sản xuất, số chỗ và giá thuê phải là số.");
            return false;
        }

        return true;
    }

    private void closeWindow() {
        Stage stage = (Stage) carNameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void populateForm() {
        if (carToEdit != null) {
            formTitle.setText("Chỉnh sửa xe");
            carNameField.setText(carToEdit.getCarName());
            yearField.setText(String.valueOf(carToEdit.getCarModelYear()));
            colorField.setText(carToEdit.getColor());
            capacityField.setText(String.valueOf(carToEdit.getCapacity()));
            priceField.setText(String.valueOf(carToEdit.getRentPrice()));
            descriptionArea.setText(carToEdit.getDescription());
            producerComboBox.setValue(carToEdit.getProducer());
            statusComboBox.setValue(carToEdit.getStatus());
        } else {
            formTitle.setText("Thêm xe mới");
        }
    }


}
