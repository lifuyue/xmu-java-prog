package com.library.lifuyue.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

final class AlertUtils {
    private AlertUtils() {
    }

    static void info(String message) {
        show(Alert.AlertType.INFORMATION, "操作成功", message);
    }

    static void warn(String message) {
        show(Alert.AlertType.WARNING, "提示", message);
    }

    static void error(String message) {
        show(Alert.AlertType.ERROR, "错误", message);
    }

    static boolean confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认操作");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
