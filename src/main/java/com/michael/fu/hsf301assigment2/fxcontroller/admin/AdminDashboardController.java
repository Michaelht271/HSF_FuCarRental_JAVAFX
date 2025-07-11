package com.michael.fu.hsf301assigment2.fxcontroller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);
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
        loadFXML("/fxml/admin/customer-management.fxml");
    }

    @FXML
    public void goToCar() {
        loadFXML("/fxml/admin/car-management.fxml");
    }

    @FXML
    public void goToRental() {
        loadFXML("/fxml/admin/rental-management.fxml");
    }

    @FXML
    public void goToReport() {
        loadFXML("/fxml/admin/report-view.fxml");
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



    @FXML private BorderPane mainLayout; // Gắn với fx:id trong FXML

    private void loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setControllerFactory(context::getBean);
            Parent view = loader.load();
            mainLayout.setCenter(view);
        } catch (Exception e) {
            logger.error("Can not load FXML: {} {} ", path, e.getMessage());
        }
    }

}
