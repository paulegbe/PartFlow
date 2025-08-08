package com.partflow.controller;

import com.partflow.config.ApplicationContextProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class SidebarController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void goToDashboard() {
        if (mainController != null) {
            mainController.loadContent("Dashboard.fxml");
        }
    }

    @FXML
    public void goToInventory() {
        if (mainController != null) {
            mainController.loadContent("Inventory.fxml");
        }
    }

    @FXML
    public void goToSales() {
        if (mainController != null) {
            mainController.loadContent("Sales.fxml");
        }
    }

    @FXML
    public void goToRestocking() {
        if (mainController != null) {
            mainController.loadContent("Restocking.fxml");
        }
    }

    @FXML
    public void goToVendors() {
        if (mainController != null) {
            mainController.loadContent("Vendors.fxml");
        }
    }

    @FXML
    public void goToReports() {
        if (mainController != null) {
            mainController.loadContent("Reports.fxml");
        }
    }

    @FXML
    public void handleLogout() {
        if (mainController == null || mainController.getRootPane() == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent loginRoot = loader.load();

            Stage stage = (Stage) mainController.getRootPane().getScene().getWindow();
            stage.setTitle("PartFlow - Login");
            stage.setScene(new Scene(loginRoot));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}