package com.michael.fu.hsf301assigment2.fxcontroller.customer;

import com.michael.fu.hsf301assigment2.entity.Car;
import com.michael.fu.hsf301assigment2.entity.CarStatus;
import com.michael.fu.hsf301assigment2.entity.Producer;
import com.michael.fu.hsf301assigment2.service.impl.CarService;
import com.michael.fu.hsf301assigment2.service.impl.ProducerService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class CarBrowseControllerFX {

    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, String> colName;
    @FXML private TableColumn<Car, Integer> colYear;
    @FXML private TableColumn<Car, String> colColor;
    @FXML private TableColumn<Car, Integer> colCapacity;
    @FXML private TableColumn<Car, String> colProducer;
    @FXML private TableColumn<Car, Double> colPrice;
    @FXML private TableColumn<Car, Void> colActions;

    @FXML private TextField searchField;
    @FXML private ComboBox<Producer> producerComboBox;
    @FXML private Button filterButton;

    private final CarService carService;
    private final ProducerService producerService;
    private final ApplicationContext context;

    private ObservableList<Car> allAvailableCars = FXCollections.observableArrayList();

    @Autowired
    public CarBrowseControllerFX(CarService carService, ProducerService producerService, ApplicationContext context) {
        this.carService = carService;
        this.producerService = producerService;
        this.context = context;
    }

    @FXML
    public void initialize() {
        setupColumns();
        loadProducers();
        loadData();
        filterButton.setOnAction(e -> handleFilter());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleFilter());
        producerComboBox.valueProperty().addListener((obs, oldVal, newVal) -> handleFilter());
    }

    private void setupColumns() {
        colName.setCellValueFactory(new PropertyValueFactory<>("carName"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("carModelYear"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colProducer.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getProducer() != null ? cell.getValue().getProducer().getProducerName() : ""));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("rentPrice"));

        // Action column with "Thuê" & "Chi tiết" buttons
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnRent = new Button("Thuê");
            private final Button btnDetail = new Button("Chi tiết");
            {
                btnRent.setOnAction(event -> {
                    Car selectedCar = getTableView().getItems().get(getIndex());
                    onRentCar(selectedCar);
                });
                btnDetail.setOnAction(event -> {
                    Car selectedCar = getTableView().getItems().get(getIndex());
                    showDetail(selectedCar);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(5, btnRent, btnDetail);
                    setGraphic(hBox);
                }
            }
        });
    }

    private void loadProducers() {
        ObservableList<Producer> producers = FXCollections.observableArrayList(producerService.findAll());
        // Thêm mục "Tất cả" vào đầu danh sách
        Producer allOption = new Producer();
        allOption.setProducerId(-1L);
        allOption.setProducerName("Tất cả");
        producers.add(0, allOption);
        producerComboBox.setItems(producers);
        producerComboBox.getSelectionModel().selectFirst();
        producerComboBox.setPromptText("Chọn hãng xe");
        // Custom cell factory để hiển thị tên hãng
        producerComboBox.setCellFactory(cb -> new ListCell<Producer>() {
            @Override
            protected void updateItem(Producer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getProducerName());
            }
        });
        producerComboBox.setButtonCell(new ListCell<Producer>() {
            @Override
            protected void updateItem(Producer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getProducerName());
            }
        });
    }

    private void loadData() {
        allAvailableCars.setAll(carService.findAll().stream().filter(c -> c.getStatus() == CarStatus.AVAILABLE).toList());
        carTable.setItems(allAvailableCars);
    }

    @FXML
    private void handleFilter() {
        String keyword = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        Producer selectedProducer = producerComboBox.getValue();
        ObservableList<Car> filtered = allAvailableCars.filtered(car -> {
            boolean matchesKeyword = car.getCarName().toLowerCase().contains(keyword);
            boolean matchesProducer = (selectedProducer == null || selectedProducer.getProducerId() == -1L)
                || (car.getProducer() != null && car.getProducer().getProducerId().equals(selectedProducer.getProducerId()));
            return matchesKeyword && matchesProducer;
        });
        carTable.setItems(filtered);
    }

    private void showDetail(Car car) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/car-detail.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            CarDetailControllerFX controller = loader.getController();
            controller.setCar(car);
            Stage stage = new Stage();
            stage.setTitle("Chi tiết xe");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onRentCar(Car car) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/rent-car.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            RentCarControllerFX controller = loader.getController();
            controller.setCar(car);
            Stage stage = new Stage();
            stage.setTitle("Thuê xe");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
