package com.michael.fu.hsf301assigment2.fxcontroller.customer;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class CustomerHomeControllerFX {

    @FXML private StackPane contentPane;

    private final ApplicationContext context;

    @Autowired
    public CustomerHomeControllerFX(ApplicationContext context) {
        this.context = context;
    }

    @FXML
    public void initialize() {
        showHome();
    }

    @FXML
    public void showHome() {
        loadContent("/fxml/customer/home.fxml");
    }

    public void showCars() {
        loadContent("/fxml/customer/car-browse.fxml");
    }

    @FXML
    public void showBookingHistory() {
        loadContent("/fxml/customer/booking-history.fxml"); // chưa có
    }

    @FXML
    public void showProfile() {
        loadContent("/fxml/customer/profile.fxml"); // chưa có
    }

    @FXML
    public void logout() {
        // close stage or go back to login
        Stage stage = (Stage) contentPane.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(context::getBean);
            Scene newScene = new Scene(loader.load());
            stage.setScene(newScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            // If the loaded view is the home page, attach action handler to Browse button
            if ("/fxml/customer/home.fxml".equals(fxmlPath)) {
                Node browseNode = root.lookup("#btnBrowse");
                if (browseNode instanceof Button button) {
                    button.setOnAction(e -> showCars());
                }
            }
            contentPane.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
