package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TabPane tabs = new TabPane();

        Tab managementTab = new Tab("学生选课管理", new ManagementPane());
        managementTab.setClosable(false);

        Tab painterTab = new Tab("Painter绘图", new PainterPane());
        painterTab.setClosable(false);

        tabs.getTabs().addAll(managementTab, painterTab);

        Scene scene = new Scene(tabs, 1100, 720);
        primaryStage.setTitle("LAB9 JavaFX 综合实验");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
