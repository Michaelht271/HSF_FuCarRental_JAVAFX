package com.michael.fu.hsf301assigment2.fxcontroller.admin;


import com.michael.fu.hsf301assigment2.entity.Car;
import com.michael.fu.hsf301assigment2.entity.Producer;
import com.michael.fu.hsf301assigment2.service.impl.CarService;
import com.michael.fu.hsf301assigment2.service.impl.ProducerService;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

@Controller
public class CarManagementControllerFX {

    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, Long> colId;
    @FXML private TableColumn<Car, String> colName;
    @FXML private TableColumn<Car, Integer> colYear;
    @FXML private TableColumn<Car, String> colColor;
    @FXML private TableColumn<Car, Integer> colCapacity;
    @FXML private TableColumn<Car, String> colProducer;
    @FXML private TableColumn<Car, Double> colPrice;
    @FXML private TableColumn<Car, String> colStatus;
    @FXML private TableColumn<Car, Void> colActions;
    @FXML private TableColumn<Car, String> colDescription;
    private final CarService carService;
    private final ProducerService producerService;
    private final ApplicationContext context;

    @Autowired
    public CarManagementControllerFX(CarService carService,
                                     ProducerService producerService,
                                     ApplicationContext context) {
        this.carService = carService;
        this.producerService = producerService;
        this.context = context;
    }

    @FXML
    public void initialize() {
        setupColumns();
        loadData();
    }

    private void setupColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("carId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("carName"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("carModelYear"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colProducer.setCellValueFactory(cell -> {
            Producer producer = cell.getValue().getProducer();
            return new ReadOnlyStringWrapper(producer != null ? producer.getProducerName() : "");
        });
        colPrice.setCellValueFactory(new PropertyValueFactory<>("rentPrice"));
        colStatus.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getStatus().name()));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");

            {
                btnEdit.setOnAction(event -> editCar(getTableView().getItems().get(getIndex())));
                btnDelete.setOnAction(event -> deleteCar(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(5, btnEdit, btnDelete);
                    setGraphic(hBox);
                }
            }
        });
    }

    public void loadData() {
        ObservableList<Car> cars = FXCollections.observableArrayList(carService.findAll());
        carTable.setItems(cars);
    }

    private void editCar(Car car) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add-car.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            AddCarControllerFX controller = loader.getController();
            controller.setParentController(this);
            controller.setCarToEdit(car);
            controller.populateForm(); // Load dữ liệu vào form
            Stage stage = new Stage();
            stage.setTitle("Sửa thông tin xe"); // hoặc "Thêm xe mới"
            Scene scene = new Scene(root);

// 👇 Kích thước cố định hoặc tối thiểu
            stage.setScene(scene);
            stage.setMinWidth(500);
            stage.setMinHeight(600);
            stage.setWidth(500);
            stage.setHeight(600);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            // chờ user sửa xong
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteCar(Car car) {
        carService.delete(car);
        loadData(); // Refresh lại bảng
    }

    @FXML
    public void onShowAddCar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add-car.fxml"));
            loader.setControllerFactory(context::getBean);

            Parent root = loader.load();

            // Gửi controller chính để callback reload
            AddCarControllerFX addCarController = loader.getController();
            addCarController.setParentController(this); // <-- Truyền về để cập nhật danh sách

            Stage stage = new Stage();
            stage.setTitle("Sửa thông tin xe"); // hoặc "Thêm xe mới"
            Scene scene = new Scene(root);

// 👇 Kích thước cố định hoặc tối thiểu
            stage.setScene(scene);
            stage.setMinWidth(500);
            stage.setMinHeight(600);
            stage.setWidth(500);
            stage.setHeight(600);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
// Đợi đến khi cửa sổ này đóng mới tiếp tục
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
