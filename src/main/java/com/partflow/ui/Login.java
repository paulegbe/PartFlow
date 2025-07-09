package com.partflow.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Login {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleLogin() throws IOException {
        if ("419".equals(usernameField.getText()) && "419".equals(passwordField.getText())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
                Parent dashboardRoot = loader.load();

                //get the current stage and set a new scene
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(dashboardRoot));
                stage.setTitle("PartFlow Dashboard");


            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Error loading dashboard");
            }

        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }
}
