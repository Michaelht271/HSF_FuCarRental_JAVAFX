package com.michael.fu.hsf301assigment2.fxcontroller.customer;

import com.michael.fu.hsf301assigment2.entity.CarRental;
import com.michael.fu.hsf301assigment2.entity.CarRentalStatus;
import com.michael.fu.hsf301assigment2.session.CustomerSession;
import com.michael.fu.hsf301assigment2.service.impl.CarRentalService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;

@Controller
public class BookingHistoryControllerFX {

    @FXML private TableView<CarRental> rentalTable;
    @FXML private TableColumn<CarRental, String> colCar;
    @FXML private TableColumn<CarRental, String> colRentDate;
    @FXML private TableColumn<CarRental, String> colReturnDate;
    @FXML private TableColumn<CarRental, Double> colPrice;
    @FXML private TableColumn<CarRental, String> colStatus;
    @FXML private TableColumn<CarRental, Void> colActions;

    private final CarRentalService carRentalService;
    private final CustomerSession customerSession;

    @Autowired
    public BookingHistoryControllerFX(CarRentalService carRentalService, CustomerSession customerSession) {
        this.carRentalService = carRentalService;
        this.customerSession = customerSession;
    }

    @FXML
    public void initialize() {
        setupColumns();
        loadData();
    }

    private void setupColumns() {
        colCar.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getCar().getCarName()));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colRentDate.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getRentDate().format(fmt)));
        colReturnDate.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getReturnDate().format(fmt)));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("rentPrice"));
        colStatus.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getStatus().name()));

        // Optional: màu cho trạng thái
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals(CarRentalStatus.CANCELED.name())) {
                        setStyle("-fx-text-fill:red;");
                    } else if (item.equals(CarRentalStatus.RETURNED.name())) {
                        setStyle("-fx-text-fill:green;");
                    } else {
                        setStyle("-fx-text-fill:orange;");
                    }
                }
            }
        });

        // actions column
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnCancel = new Button("Huỷ");
            private final Button btnDetail = new Button("Chi tiết");
            {
                btnCancel.setOnAction(e -> {
                    CarRental rental = getTableView().getItems().get(getIndex());
                    cancelRental(rental);
                });
                btnDetail.setOnAction(e -> {
                    CarRental rental = getTableView().getItems().get(getIndex());
                    showDetail(rental);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CarRental rental = getTableView().getItems().get(getIndex());
                    if (rental.getStatus() == CarRentalStatus.PENDING) {
                        setGraphic(btnCancel);
                    } else {
                        setGraphic(btnDetail);
                    }
                }
            }
        });
    }

    private void cancelRental(CarRental rental) {
        if (rental.getStatus()!=CarRentalStatus.PENDING) return;
        try {
            carRentalService.updateStatus(rental.getCustomer().getCustomerId(), rental.getCar().getCarId(), "CANCELED");
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showDetail(CarRental rental) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Chi tiết đơn thuê");
        alert.setContentText("Xe: "+rental.getCar().getCarName()+"\nNgày nhận: "+rental.getRentDate()+"\nNgày trả: "+rental.getReturnDate()+"\nGiá: "+rental.getRentPrice()+"\nTrạng thái: "+rental.getStatus());
        alert.showAndWait();
    }

    private void loadData() {
        ObservableList<CarRental> rentals = FXCollections.observableArrayList(
                carRentalService.findByCustomer(customerSession.getCustomer())
        );
        rentalTable.setItems(rentals);
    }
}
