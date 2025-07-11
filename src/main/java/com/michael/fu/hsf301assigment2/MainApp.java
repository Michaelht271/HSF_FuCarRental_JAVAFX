package com.michael.fu.hsf301assigment2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        // Bật Spring Boot với chính MainApp.class
        context = new SpringApplicationBuilder(MainApp.class).run();


    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        // Cho phép FXMLLoader lấy controller từ Spring Context
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        stage.setScene(new Scene(root, 800, 1000));
        stage.setTitle("Đăng nhập hệ thống");
        stage.show();
    }

    @Override
    public void stop() {
        // Đóng context khi ứng dụng JavaFX kết thúc
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
