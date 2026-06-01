package com.library.lifuyue;

import com.library.lifuyue.model.AdminUser;
import com.library.lifuyue.model.ReaderUser;
import com.library.lifuyue.service.AuthService;
import com.library.lifuyue.service.LibraryException;
import com.library.lifuyue.service.LibrarySystem;
import com.library.lifuyue.ui.AdminDashboardPane;
import com.library.lifuyue.ui.LoginPane;
import com.library.lifuyue.ui.ReaderDashboardPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class MainApp extends Application {
    private static final double WIDTH = 1180;
    private static final double HEIGHT = 760;

    private Stage stage;
    private LibrarySystem librarySystem;
    private AuthService authService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws LibraryException {
        stage = primaryStage;
        librarySystem = new LibrarySystem(Paths.get("data"));
        librarySystem.load();
        authService = new AuthService(librarySystem);

        stage.setTitle("图书管理系统");
        stage.setAlwaysOnTop(System.getProperty("library.demo.view") != null);
        stage.setMinWidth(980);
        stage.setMinHeight(640);
        stage.setOnCloseRequest(event -> {
            try {
                librarySystem.saveAll();
            } catch (LibraryException ex) {
                event.consume();
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("保存失败");
                alert.setHeaderText(null);
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        showInitialView();
        stage.show();
        stage.toFront();
        stage.requestFocus();
    }

    private void showInitialView() {
        String demoView = System.getProperty("library.demo.view", "login");
        switch (demoView) {
            case "admin-management" -> showAdminDashboard((AdminUser) librarySystem.findUser("admin"), 0);
            case "admin-query" -> showAdminDashboard((AdminUser) librarySystem.findUser("admin"), 1);
            case "admin-records" -> showAdminDashboard((AdminUser) librarySystem.findUser("admin"), 2);
            case "admin-info" -> showAdminDashboard((AdminUser) librarySystem.findUser("admin"), 3);
            case "reader-query" -> showReaderDashboard(new ReaderUser("reader-lifuyue", "李富悦"), 0);
            case "reader-borrow" -> showReaderDashboard(new ReaderUser("reader-lifuyue", "李富悦"), 1);
            case "reader-records" -> showReaderDashboard(new ReaderUser("reader-lifuyue", "李富悦"), 2);
            default -> showLogin();
        }
    }

    private void showLogin() {
        LoginPane loginPane = new LoginPane(authService, this::showAdminDashboard, this::showReaderDashboard);
        setScene(loginPane);
        stage.setTitle("图书管理系统 - 登录");
    }

    private void showAdminDashboard(AdminUser adminUser) {
        showAdminDashboard(adminUser, 0);
    }

    private void showAdminDashboard(AdminUser adminUser, int initialTabIndex) {
        setScene(new AdminDashboardPane(librarySystem, adminUser, this::showLogin, initialTabIndex));
        stage.setTitle("图书管理系统 - 管理员模式");
    }

    private void showReaderDashboard(ReaderUser readerUser) {
        showReaderDashboard(readerUser, 0);
    }

    private void showReaderDashboard(ReaderUser readerUser, int initialTabIndex) {
        setScene(new ReaderDashboardPane(librarySystem, readerUser, this::showLogin, initialTabIndex));
        stage.setTitle("图书管理系统 - 读者模式");
    }

    private void setScene(javafx.scene.Parent root) {
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        String stylesheet = MainApp.class.getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
    }
}
