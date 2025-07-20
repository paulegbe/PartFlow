package com.partflow.controller;

import com.partflow.config.ApplicationContextProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    @FXML private BorderPane rootPane;
    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        loadSidebar();         // Inject sidebar on left
        loadContent("Dashboard.fxml"); // Load initial content
    }

    private void loadSidebar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Sidebar.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent sidebar = loader.load();

            SidebarController sidebarController = loader.getController();
            sidebarController.setMainController(this); // Give sidebar access to content loader

            rootPane.setLeft(sidebar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent content = loader.load();
            contentArea.getChildren().setAll(content);
        } catch (Exception e) {
            System.err.println("Failed to load content: " + fxmlFile);
            e.printStackTrace();
        }
    }

    public BorderPane getRootPane() {
        return rootPane;
    }
}
