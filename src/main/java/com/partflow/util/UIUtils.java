package com.partflow.util;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public final class UIUtils {

    private UIUtils() {}

    public static void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean confirm(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait().filter(button -> button.getButtonData().isDefaultButton()).isPresent();
    }

    public static void withBusy(Scene scene, Runnable action) {
        if (scene == null) {
            action.run();
            return;
        }
        try {
            scene.setCursor(Cursor.WAIT);
            action.run();
        } finally {
            scene.setCursor(Cursor.DEFAULT);
        }
    }
}


