package com.michael.fu.hsf301assigment2.fxcontroller.admin;

import com.michael.fu.hsf301assigment2.entity.Customer;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDate;

@Controller
public class CustomerManagementControllerFX {

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Long> colId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colBirthday;
    @FXML private TableColumn<Customer, String> colIdentify;
    @FXML private TableColumn<Customer, String> colLicense;
    @FXML private TableColumn<Customer, String> colLicenseDate;
    @FXML private TableColumn<Customer, Void> colActions;

    private final CustomerService customerService;
    private final ApplicationContext context;

    @Autowired
    public CustomerManagementControllerFX(CustomerService customerService, ApplicationContext context) {
        this.customerService = customerService;
        this.context = context;
    }

    @FXML
    public void initialize() {
        setupTable();
        loadCustomers();
    }

    private void setupTable() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleLongProperty(c.getValue().getCustomerId()).asObject());
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCustomerName()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        colPhone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMobile()));
        colBirthday.setCellValueFactory(c -> {
            LocalDate date = c.getValue().getBirthday();
            return new SimpleStringProperty(date != null ? date.toString() : "");
        });

        colLicenseDate.setCellValueFactory(c -> {
            LocalDate date = c.getValue().getLicenceDate();
            return new SimpleStringProperty(date != null ? date.toString() : "");
        });

        colIdentify.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIdentifyCard()));
        colLicense.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLicenceNumber()));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");

            {
                btnEdit.setOnAction(e -> showEditModal(getTableView().getItems().get(getIndex())));
                btnDelete.setOnAction(e -> {
                    Customer c = getTableView().getItems().get(getIndex());
                    customerService.deleteById(c.getCustomerId());
                    loadCustomers();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnEdit, btnDelete);
                    setGraphic(box);
                }
            }
        });
    }

    public void loadCustomers() {
        ObservableList<Customer> list = FXCollections.observableArrayList(customerService.findAll());
        customerTable.setItems(list);
    }

    @FXML
    public void onShowAddCustomer() {
        showEditModal(null);
    }

    private void showEditModal(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add-customer.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            AddCustomerControllerFX controller = loader.getController();
            controller.setParentController(this);
            controller.setCustomerToEdit(customer);

            Stage stage = new Stage();
            stage.setTitle(customer == null ? "Thêm khách hàng" : "Cập nhật khách hàng");
            Scene scene = new Scene(root);

// 👇 Kích thước cố định hoặc tối thiểu
            stage.setScene(scene);
            stage.setMinWidth(500);
            stage.setMinHeight(600);
            stage.setWidth(500);
            stage.setHeight(600);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
