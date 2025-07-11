package com.michael.fu.hsf301assigment2.fxcontroller.admin;

import com.michael.fu.hsf301assigment2.entity.Customer;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
public class AddCustomerControllerFX {

    @FXML private Label formTitle;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private DatePicker birthdayPicker;
    @FXML private TextField identifyField;
    @FXML private TextField licenseField;
    @FXML private DatePicker licenseDatePicker;

    private final CustomerService customerService;
    private Customer customerToEdit;
    private CustomerManagementControllerFX parentController;

    @Autowired
    public AddCustomerControllerFX(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setParentController(CustomerManagementControllerFX controller) {
        this.parentController = controller;
    }

    public void setCustomerToEdit(Customer customer) {
        this.customerToEdit = customer;
        if (customer != null) {
            formTitle.setText("Cập nhật khách hàng");
            nameField.setText(customer.getCustomerName());
            emailField.setText(customer.getEmail());
            phoneField.setText(customer.getMobile());
            birthdayPicker.setValue(customer.getBirthday());
            identifyField.setText(customer.getIdentifyCard());
            licenseField.setText(customer.getLicenceNumber());
            licenseDatePicker.setValue(customer.getLicenceDate());
        }
    }

    @FXML
    public void onSave() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        LocalDate birthday = birthdayPicker.getValue();
        String identify = identifyField.getText();
        String license = licenseField.getText();
        LocalDate licenseDate = licenseDatePicker.getValue();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || birthday == null ||
                identify.isEmpty() || license.isEmpty() || licenseDate == null) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (customerToEdit == null) {
            customerToEdit = new Customer();
            // Có thể đặt mặc định mật khẩu hoặc role tại đây nếu cần
        }

        customerToEdit.setCustomerName(name);
        customerToEdit.setEmail(email);
        customerToEdit.setMobile(phone);
        customerToEdit.setBirthday(birthday);
        customerToEdit.setIdentifyCard(identify);
        customerToEdit.setLicenceNumber(license);
        customerToEdit.setLicenceDate(licenseDate);

        if (customerToEdit.getCustomerId() == null) {
            customerService.saveCustomerByAdmin(customerToEdit);
        } else {
            customerService.update(customerToEdit);
        }

        parentController.loadCustomers();
        closeStage();
    }

    @FXML
    public void onCancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
