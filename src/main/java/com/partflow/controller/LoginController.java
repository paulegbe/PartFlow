package com.partflow.controller;

import com.partflow.model.User;
import com.partflow.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessageLabel;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigurableApplicationContext springContext;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userService.findByUsername(username);

        if (user != null && userService.checkPassword(user, password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Parent root = loader.load();

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setTitle("PartFlow - Inventory Management System");
                stage.setScene(new Scene(root));
                stage.setMinWidth(1200);
                stage.setMinHeight(700);
                stage.setResizable(true);
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error loading main application.");
            }
        } else {
            errorMessageLabel.setText("Invalid username or password.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}