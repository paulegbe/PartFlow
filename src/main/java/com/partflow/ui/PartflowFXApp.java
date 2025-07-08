package com.partflow.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class PartflowFXApp extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init(){
        springContext = new SpringApplicationBuilder(PartflowApplication.class).run();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();

        primaryStage.setTitle("PartFlow Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    @Override
    public void stop(){
        springContext.close();
    }
}
