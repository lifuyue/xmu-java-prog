package com.example.lab10;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * LAB10 JavaFX 入口：组装文件解析和学生顺序文件管理两个功能页。
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        Path labDir = resolveLabDirectory();
        StudentRepository repository = new StudentRepository(labDir.resolve("data"));
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
            // 截图模式仍然启动真实窗口，再由 ScreenshotSession 自动切换状态并截图。
            Platform.runLater(() -> new ScreenshotSession(stage, tabPane, fileAnalyzerPane,
                    studentManagerPane, labDir.resolve("screenshots")).run());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Path resolveLabDirectory() {
        Path current = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
        if (Files.exists(current.resolve("pom.xml"))
                && Files.exists(current.resolve("src/main/java/com/example/lab10/MainApp.java"))) {
            return current;
        }
        Path lab10 = current.resolve("LAB10");
        if (Files.exists(lab10.resolve("pom.xml"))) {
            return lab10;
        }
        return current;
    }
}
