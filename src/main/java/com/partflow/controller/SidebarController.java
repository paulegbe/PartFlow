package com.partflow.controller;

import com.partflow.config.ApplicationContextProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SidebarController {

    private MainController mainController;
    @FXML
    private ToggleGroup navGroup;


    // Setter method to link MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void printSelectedButton() {
        Toggle selected = navGroup.getSelectedToggle();
        if (selected != null) {
            ToggleButton btn = (ToggleButton) selected;
            System.out.println("Selected: " + btn.getText());
        }
    }

    @FXML
    public void goToDashboard() {
        mainController.loadContent("Dashboard.fxml");
    }

    @FXML
    public void goToInventory() {
        mainController.loadContent("Inventory.fxml");
    }

    @FXML
    public void goToSales() {
        mainController.loadContent("Sales.fxml");
    }

    @FXML
    public void goToVendors() {
        mainController.loadContent("Vendors.fxml");
    }

    @FXML
    public void goToReports() {
        mainController.loadContent("Reports.fxml");
    }

    @FXML
    public void goToRestocking() {
        mainController.loadContent("Restocking.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent loginView = loader.load();

            //close current window and open Login
            Stage currentStage = (Stage) mainController.getRootPane().getScene().getWindow();
            navGroup.selectToggle(null);
            currentStage.setScene(new Scene(loginView));
            //currentStage.setTitle("PartFlow Login");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}