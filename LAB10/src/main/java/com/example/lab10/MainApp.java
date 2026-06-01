package com.example.lab10;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.nio.file.Path;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        StudentRepository repository = new StudentRepository(Path.of(System.getProperty("user.dir"), "data"));
        FileAnalyzerPane fileAnalyzerPane = new FileAnalyzerPane();
        StudentManagerPane studentManagerPane = new StudentManagerPane(repository);

        TabPane tabPane = new TabPane();
        Tab fileTab = new Tab("路径解析", fileAnalyzerPane);
        Tab studentTab = new Tab("学生顺序文件管理", studentManagerPane);
        fileTab.setClosable(false);
        studentTab.setClosable(false);
        tabPane.getTabs().addAll(fileTab, studentTab);

        Scene scene = new Scene(tabPane, 1180, 760);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("LAB10 文件处理");
        stage.setScene(scene);
        stage.setX(80);
        stage.setY(80);
        stage.setWidth(1180);
        stage.setHeight(760);
        stage.show();

        if (getParameters().getRaw().contains("--screenshots")) {
            Platform.runLater(() -> new ScreenshotSession(stage, tabPane, fileAnalyzerPane,
                    studentManagerPane, Path.of(System.getProperty("user.dir"), "screenshots")).run());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
