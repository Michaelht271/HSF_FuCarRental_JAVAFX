package com.michael.fu.hsf301assigment2.fxcontroller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.michael.fu.hsf301assigment2.session.CustomerSession;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;

@Controller
public class LoginControllerFX {
    Logger logger = LoggerFactory.getLogger(LoginControllerFX.class);
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final ApplicationContext context;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final CustomerSession customerSession;

    @Autowired
    public LoginControllerFX(AuthenticationManager authenticationManager,
                             ApplicationContext context,
                             CustomerService customerService,
                             CustomerSession customerSession) {
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.customerSession = customerSession;
        this.context = context;

    }

    @FXML
    public void handleLogin() {
        logger.info("Login: username={}, password={}", usernameField.getText(), passwordField.getText());
        String email = usernameField.getText();
        String password = passwordField.getText();

        try {
            logger.info("Login: email={}, password={}", email, password);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            logger.info("Login: authentication={}", authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("Login: userDetails={}", userDetails);
            customerSession.setCustomer(customerService.findCustomerByEmail(email));

            if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                openAdminDashboard();
            } else {
                openCustomerDashboard();
            }

        } catch (AuthenticationException ex) {
            errorLabel.setText("Sai email hoặc mật khẩu!");
            errorLabel.setVisible(true);
        }
    }

    private void openAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/admin-dashboard.fxml"));
            loader.setControllerFactory(context::getBean);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(loader.load());
            stage.setTitle("Trang quản trị");
        } catch (Exception e) {
            logger.error("Error when loading admin-dashboard.fxml", e);
        }
    }



    @FXML
    public void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            loader.setControllerFactory(context::getBean);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(loader.load());
        } catch (Exception e) {
            logger.error("Error when loading register.fxml", e);
        }
    }
    private void openCustomerDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerHome.fxml"));
            loader.setControllerFactory(context::getBean);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(loader.load());
        } catch (Exception e) {
            logger.error("Error when loading CustomerHome.fxml", e);
        }
    }

}
