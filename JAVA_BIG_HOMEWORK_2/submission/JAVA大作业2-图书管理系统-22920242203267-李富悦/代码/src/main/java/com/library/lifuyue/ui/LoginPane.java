package com.library.lifuyue.ui;

import com.library.lifuyue.model.AdminUser;
import com.library.lifuyue.model.ReaderUser;
import com.library.lifuyue.service.AuthService;
import com.library.lifuyue.service.LibraryException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class LoginPane extends BorderPane {
    private final AuthService authService;
    private final Consumer<AdminUser> adminLoginHandler;
    private final Consumer<ReaderUser> readerLoginHandler;
    private final TextField adminAccountField = new TextField("admin");
    private final PasswordField adminPasswordField = new PasswordField();
    private final TextField readerNameField = new TextField("李富悦");

    public LoginPane(AuthService authService, Consumer<AdminUser> adminLoginHandler, Consumer<ReaderUser> readerLoginHandler) {
        this.authService = authService;
        this.adminLoginHandler = adminLoginHandler;
        this.readerLoginHandler = readerLoginHandler;
        build();
    }

    private void build() {
        setPadding(new Insets(28));

        Label icon = new Label("📘");
        icon.setStyle("-fx-font-size: 36px;");
        Label title = new Label("图书管理系统");
        title.getStyleClass().add("page-title");
        HBox header = new HBox(12, icon, title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 24, 0));
        setTop(header);

        VBox adminPanel = buildAdminPanel();
        VBox readerPanel = buildReaderPanel();
        HBox content = new HBox(24, adminPanel, readerPanel);
        content.setAlignment(Pos.CENTER);
        HBox.setHgrow(adminPanel, Priority.ALWAYS);
        HBox.setHgrow(readerPanel, Priority.ALWAYS);
        setCenter(content);

        Label footer = new Label("欢迎使用图书管理系统");
        footer.getStyleClass().add("status-text");
        BorderPane.setAlignment(footer, Pos.CENTER);
        BorderPane.setMargin(footer, new Insets(20, 0, 0, 0));
        setBottom(footer);
    }

    private VBox buildAdminPanel() {
        Label title = new Label("管理员登录");
        title.getStyleClass().add("section-title");
        adminPasswordField.setText("admin123");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.add(new Label("账号"), 0, 0);
        form.add(adminAccountField, 1, 0);
        form.add(new Label("密码"), 0, 1);
        form.add(adminPasswordField, 1, 1);
        form.add(new CheckBox("记住账号"), 1, 2);

        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(event -> loginAdmin());
        adminPasswordField.setOnAction(event -> loginAdmin());

        VBox panel = new VBox(16, title, form, loginButton);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(22));
        panel.setPrefWidth(360);
        return panel;
    }

    private VBox buildReaderPanel() {
        Label title = new Label("普通读者入口");
        title.getStyleClass().add("section-title");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.add(new Label("姓名"), 0, 0);
        form.add(readerNameField, 1, 0);

        Button enterButton = new Button("进入系统");
        enterButton.getStyleClass().add("primary-button");
        enterButton.setMaxWidth(Double.MAX_VALUE);
        enterButton.setOnAction(event -> enterReader());
        readerNameField.setOnAction(event -> enterReader());

        Label hint = new Label("首次输入姓名会自动创建读者账号");
        hint.getStyleClass().add("status-text");

        VBox panel = new VBox(16, title, form, enterButton, hint);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(22));
        panel.setPrefWidth(360);
        return panel;
    }

    private void loginAdmin() {
        try {
            adminLoginHandler.accept(authService.loginAdmin(adminAccountField.getText(), adminPasswordField.getText()));
        } catch (LibraryException ex) {
            AlertUtils.warn(ex.getMessage());
        }
    }

    private void enterReader() {
        try {
            readerLoginHandler.accept(authService.enterReader(readerNameField.getText()));
        } catch (LibraryException ex) {
            AlertUtils.warn(ex.getMessage());
        }
    }
}
