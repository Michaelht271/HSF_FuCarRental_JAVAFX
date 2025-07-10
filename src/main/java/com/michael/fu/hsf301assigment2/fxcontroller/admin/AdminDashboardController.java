package com.michael.fu.hsf301assigment2.fxcontroller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.michael.fu.hsf301assigment2.service.impl.CarRentalService;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import com.michael.fu.hsf301assigment2.service.impl.CarService;

import java.time.LocalDate;
import java.time.YearMonth;

@Controller
public class AdminDashboardController {

    @FXML private Label lblTotalCustomers;
    @FXML private Label lblTotalCars;
    @FXML private Label lblMonthlyRentals;
    @FXML private Label lblMonthlyRevenue;

    private final CustomerService customerService;
    private final CarService carService;
    private final CarRentalService carRentalService;
    private final ApplicationContext context;

    @Autowired
    public AdminDashboardController(CustomerService customerService,
                                    CarService carService,
                                    CarRentalService carRentalService,
                                    ApplicationContext context) {
        this.customerService = customerService;
        this.carService = carService;
        this.carRentalService = carRentalService;
        this.context = context;
    }

    @FXML
    public void initialize() {
        lblTotalCustomers.setText(String.valueOf(customerService.countAll()));
        lblTotalCars.setText(String.valueOf(carService.countAll()));

        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        lblMonthlyRentals.setText(String.valueOf(carRentalService.countRentalsBetween(start, end)));
        lblMonthlyRevenue.setText("₫" + carRentalService.sumRevenueBetween(start, end));
    }

    // Handler cho sidebar
    @FXML
    public void goToCustomer() {
        loadFXML("/fxml/admin/customer-list.fxml");
    }

    @FXML
    public void goToCar() {
        loadFXML("/fxml/admin/car-list.fxml");
    }

    @FXML
    public void goToRental() {
        loadFXML("/fxml/admin/rental-list.fxml");
    }

    @FXML
    public void goToReport() {
        loadFXML("/fxml/admin/report.fxml");
    }

    @FXML
    public void goToSettings() {
        loadFXML("/fxml/admin/settings.fxml");
    }

    @FXML
    public void logout() {
        loadFXML("/fxml/login.fxml");
    }
    @FXML
    public void goToDashboard() {
        loadFXML("/fxml/admin/admin-dashboard.fxml");
    }

    private void loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) lblTotalCustomers.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
