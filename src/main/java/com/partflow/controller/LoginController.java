package com.partflow.controller;

import com.partflow.config.ApplicationContextProvider;
import com.partflow.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean success = userService.validateLogin(username, password);

        if (success) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
                loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

                Parent root = loader.load();

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("PartFlow");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Error loading main application.");
            }
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }

}
