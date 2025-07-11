package com.michael.fu.hsf301assigment2.fxcontroller.admin;


import com.michael.fu.hsf301assigment2.entity.CarRental;
import com.michael.fu.hsf301assigment2.service.impl.CarRentalService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class RentalManagementControllerFX {

    @FXML private TableView<CarRental> rentalTable;
    @FXML private TableColumn<CarRental, Number> colIndex;
    @FXML private TableColumn<CarRental, String> colCustomerName;
    @FXML private TableColumn<CarRental, String> colCustomerPhone;
    @FXML private TableColumn<CarRental, String> colCarName;
    @FXML private TableColumn<CarRental, String> colRentDate;
    @FXML private TableColumn<CarRental, String> colReturnDate;
    @FXML private TableColumn<CarRental, Number> colRentPrice;
    @FXML private TableColumn<CarRental, String> colStatus;
    @FXML private TableColumn<CarRental, Void> colActions;
    @FXML private ComboBox<String> statusFilter;
    @FXML private TextField searchField;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;

    private List<CarRental> allRentals;

    private final CarRentalService carRentalService;
    @Autowired
    public RentalManagementControllerFX(CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }
    @FXML
    public void initialize() {
        allRentals = carRentalService.findAll();
        ObservableList<CarRental> data = FXCollections.observableArrayList(allRentals);

        rentalTable.setItems(data);

        colIndex.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(rentalTable.getItems().indexOf(cell.getValue()) + 1));
        colCustomerName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomer().getCustomerName()));
        colCustomerPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCustomer().getMobile()));
        colCarName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCar().getCarName()));
        colRentDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colReturnDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colRentPrice.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getRentPrice()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().name()));
        statusFilter.getItems().addAll("Tất cả", "PENDING", "BOOKED", "PICKED_UP", "RETURNED", "CANCELED");
        statusFilter.setValue("Tất cả");
        // Thao tác
        colActions.setCellFactory(getActionCellFactory());
    }

    private Callback<TableColumn<CarRental, Void>, TableCell<CarRental, Void>> getActionCellFactory() {
        return column -> new TableCell<>() {
            private final Button btnApprove = new Button("Duyệt");
            private final Button btnCancel = new Button("Hủy");
            private final Button btnPickUp = new Button("Đã nhận xe");
            private final Button btnReturn = new Button("Đã trả xe");

            {
                btnApprove.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                btnCancel.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnPickUp.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
                btnReturn.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

                btnApprove.setOnAction(e -> handleAction("BOOKED"));
                btnCancel.setOnAction(e -> handleAction("CANCELED"));
                btnPickUp.setOnAction(e -> handleAction("PICKED_UP"));
                btnReturn.setOnAction(e -> handleAction("RETURNED"));
            }

            private void handleAction(String newStatus) {
                CarRental rental = getTableView().getItems().get(getIndex());
                carRentalService.updateStatus(rental.getCustomer().getCustomerId(), rental.getCar().getCarId(), newStatus);
                getTableView().setItems(FXCollections.observableArrayList(carRentalService.findAll()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CarRental rental = getTableView().getItems().get(getIndex());
                    HBox actions = new HBox(5);

                    switch (rental.getStatus().name()) {
                        case "PENDING" -> actions.getChildren().addAll(btnApprove, btnCancel);
                        case "BOOKED" -> actions.getChildren().addAll(btnPickUp, btnCancel);
                        case "PICKED_UP" -> actions.getChildren().addAll(btnReturn);
                        case "RETURNED", "CANCELED" -> actions.getChildren().clear();
                        default -> actions.getChildren().clear();
                    }

                    setGraphic(actions);
                }
            }
        };
    }
    @FXML
    public void applyFilter() {
        String keyword = searchField.getText().toLowerCase().trim();
        String selectedStatus = statusFilter.getValue();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        List<CarRental> filtered = allRentals.stream()
                .filter(rental -> {
                    boolean matchStatus = selectedStatus == null || selectedStatus.equals("Tất cả")
                            || rental.getStatus().name().equalsIgnoreCase(selectedStatus);

                    boolean matchKeyword = keyword.isEmpty()
                            || rental.getCustomer().getCustomerName().toLowerCase().contains(keyword)
                            || rental.getCustomer().getMobile().toLowerCase().contains(keyword);

                    boolean matchFromDate = fromDate == null || !rental.getRentDate().isBefore(fromDate);
                    boolean matchToDate = toDate == null || !rental.getReturnDate().isAfter(toDate);

                    return matchStatus && matchKeyword && matchFromDate && matchToDate;
                })
                .toList();

        rentalTable.setItems(FXCollections.observableArrayList(filtered));
    }

}
