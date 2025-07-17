package com.partflow.controller;

import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

@Component
public class SidebarController {

    private MainController mainController;

    // Setter method to link MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML public void goToDashboard()  { mainController.loadContent("Dashboard.fxml"); }
    @FXML public void goToInventory()  { mainController.loadContent("Inventory.fxml"); }
    @FXML public void goToSales()      { mainController.loadContent("Sales.fxml"); }
    @FXML public void goToVendors()    { mainController.loadContent("Vendors.fxml"); }
    @FXML public void goToReports()    { mainController.loadContent("Reports.fxml"); }
    @FXML public void goToRestocking() { mainController.loadContent("Restocking.fxml"); }
}
