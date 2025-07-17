package com.partflow;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class PartflowFXApp extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(PartflowApplication.class)
            .headless(false)  // Important for JavaFX
            .run();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            primaryStage.setTitle("PartFlow LoginController");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }
}