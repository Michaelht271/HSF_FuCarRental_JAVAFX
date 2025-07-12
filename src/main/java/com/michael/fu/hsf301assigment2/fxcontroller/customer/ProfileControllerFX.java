package com.michael.fu.hsf301assigment2.fxcontroller.customer;

import com.michael.fu.hsf301assigment2.entity.Customer;
import com.michael.fu.hsf301assigment2.session.CustomerSession;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProfileControllerFX {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private Button saveBtn;

    private final CustomerSession customerSession;
    private final CustomerService customerService;

    @Autowired
    public ProfileControllerFX(CustomerSession customerSession, CustomerService customerService) {
        this.customerSession = customerSession;
        this.customerService = customerService;
    }

    @FXML
    public void initialize() {
        populateFields();
    }

    private void populateFields() {
        Customer c = customerSession.getCustomer();
        if (c == null) return;
        nameField.setText(c.getCustomerName());
        emailField.setText(c.getEmail());
        phoneField.setText(c.getMobile());
        addressField.setText(c.getIdentifyCard());
    }

    @FXML
    public void enableEdit() {
        nameField.setEditable(true);
        phoneField.setEditable(true);
        addressField.setEditable(true);
        saveBtn.setVisible(true);
    }

    @FXML
    public void saveProfile() {
        Customer c = customerSession.getCustomer();
        if (c == null) return;
        c.setCustomerName(nameField.getText());
        c.setMobile(phoneField.getText());
        c.setIdentifyCard(addressField.getText());
        customerService.save(c);

        nameField.setEditable(false);
        phoneField.setEditable(false);
        addressField.setEditable(false);
        saveBtn.setVisible(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cập nhật thành công!");
        alert.showAndWait();
    }

    @FXML
    public void showBookingHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/booking-history.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Lịch sử giao dịch");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Không thể mở lịch sử giao dịch: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
