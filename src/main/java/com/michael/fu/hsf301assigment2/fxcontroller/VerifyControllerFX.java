package com.michael.fu.hsf301assigment2.fxcontroller;

import com.michael.fu.hsf301assigment2.entity.Customer;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class VerifyControllerFX {

    @FXML private TextField otpField;
    @FXML private Label messageLabel;

    private final CustomerService customerService;
    private final ApplicationContext context;

    private String email;

    @Autowired
    public VerifyControllerFX(CustomerService customerService, ApplicationContext context) {
        this.customerService = customerService;
        this.context = context;
    }

    public void setEmail(String email) {
        this.email = email;
        System.out.println("Set email = " + email);
    }

    @FXML
    public void handleVerify() {
        if (email == null || email.isEmpty()) {
            messageLabel.setText("Không tìm thấy email.");
            return;
        }

        String otp = otpField.getText().trim();
        if (otp.isEmpty()) {
            messageLabel.setText("Vui lòng nhập mã OTP.");
            return;
        }

        Customer customer = customerService.findCustomerByEmail(email);

        if (customer == null) {
            messageLabel.setText("Không tìm thấy tài khoản.");
        } else if (customer.getOtpCode() == null || !otp.equals(customer.getOtpCode())) {
            messageLabel.setText("Mã xác minh không đúng.");
        } else if (customer.getOtpExpiration() == null || customer.getOtpExpiration().isBefore(LocalDateTime.now())) {
            messageLabel.setText("Mã OTP đã hết hạn.");
        } else {
            customer.setIsEmailVerified(true);
            customer.setOtpCode(null);
            customer.setOtpExpiration(null);
            customerService.saveCustomer(customer);
            messageLabel.setText("Xác minh thành công! Vui lòng đăng nhập.");
        }
    }

    @FXML
    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) otpField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("Lỗi khi chuyển đến trang đăng nhập.");
        }
    }
}
