package com.michael.fu.hsf301assigment2.fxcontroller;

import com.michael.fu.hsf301assigment2.entity.Account;
import com.michael.fu.hsf301assigment2.entity.Customer;
import com.michael.fu.hsf301assigment2.entity.Role;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import com.michael.fu.hsf301assigment2.service.impl.EmailService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class RegisterControllerFX {
    private static final Logger logger = LoggerFactory.getLogger(RegisterControllerFX.class);

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField mobileField;
    @FXML private DatePicker birthdayPicker;
    @FXML private TextField identityField;
    @FXML private TextField licenceField;
    @FXML private DatePicker licenceDatePicker;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Label messageLabel;

    private final CustomerService customerService;
    private final EmailService emailService;
    private final ApplicationContext context;
    private final PasswordEncoder passwordEncoder ;

    @Autowired
    public RegisterControllerFX(CustomerService customerService,
                                EmailService emailService,
                                ApplicationContext context,
                                PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.emailService = emailService;
        this.context = context;
        this.passwordEncoder = passwordEncoder;
    }

    @FXML
    public void handleRegister() {
        messageLabel.setText("");
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String mobile = mobileField.getText().trim();
        String pwd = passwordField.getText();
        String confirm = confirmField.getText();

        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty()
                || birthdayPicker.getValue() == null || identityField.getText().isEmpty()
                || licenceField.getText().isEmpty() || licenceDatePicker.getValue() == null
                || pwd.length() < 6 || !pwd.equals(confirm)) {
            messageLabel.setText("Vui lòng điền đúng và đầy đủ thông tin.");
            return;
        }

        Customer existing = customerService.findCustomerByEmail(email);
        String otp = customerService.generateOTP(6);

        if (existing != null) {
            if (existing.getOtpCode() != null && existing.getOtpExpiration().isAfter(LocalDateTime.now())) {
                messageLabel.setText("Bạn đã đăng ký, vui lòng kiểm tra email để xác minh.");
                return;
            }
            // đã có nhưng chưa xác minh hoặc hết hạn → cập nhật OTP mới
            existing.setOtpCode(otp);
            existing.setOtpExpiration(LocalDateTime.now().plusMinutes(5));
            customerService.saveCustomer(existing);
            emailService.sendVerification(email, otp);
            messageLabel.setText("Đã gửi lại mã xác minh. Vui lòng kiểm tra email.");
            return;
        }

        // tạo mới
        Customer c = new Customer();
        c.setCustomerName(name);
        c.setEmail(email);
        c.setMobile(mobile);
        c.setBirthday(birthdayPicker.getValue());
        c.setIdentifyCard(identityField.getText().trim());
        c.setLicenceNumber(licenceField.getText().trim());
        c.setLicenceDate(licenceDatePicker.getValue());
        c.setPassword(passwordEncoder.encode(pwd));
        c.setOtpCode(otp);
        c.setOtpExpiration(LocalDateTime.now().plusMinutes(5));

        Account a = new Account();
        a.setRole(Role.USER);
        a.setAccountName(email);
        a.setCustomer(c);
        c.setAccount(a);

        customerService.saveCustomer(c);
        emailService.sendOtpVerification(email, otp);

        messageLabel.setText("Đăng ký thành công! Vui lòng kiểm tra email để xác minh.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/verify-otp.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            // Sau khi load xong FXML, mới gọi getController
            VerifyControllerFX verifyController = loader.getController();
            verifyController.setEmail(email);

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
           logger.error("Error when loading verify-otp.fxml", e);
        }


    }

    @FXML
    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(context::getBean);
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.getScene().setRoot(loader.load());
        } catch (Exception ex) {
           logger.error("Error when loading login.fxml", ex);
        }
    }
}
