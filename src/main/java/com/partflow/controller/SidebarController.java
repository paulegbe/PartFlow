package com.partflow.controller;

import javafx.fxml.FXML;
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
}