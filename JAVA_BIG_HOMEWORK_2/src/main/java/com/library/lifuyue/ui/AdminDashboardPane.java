package com.library.lifuyue.ui;

import com.library.lifuyue.model.AdminUser;
import com.library.lifuyue.model.Book;
import com.library.lifuyue.service.LibraryException;
import com.library.lifuyue.service.LibrarySystem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AdminDashboardPane extends BorderPane {
    private final LibrarySystem librarySystem;
    private final AdminUser adminUser;
    private final Runnable logoutHandler;
    private final BookTablePane managementTable = new BookTablePane();
    private final BookTablePane queryTable = new BookTablePane();
    private final BorrowRecordTablePane recordTable = new BorrowRecordTablePane();
    private final TextField isbnField = new TextField();
    private final TextField titleField = new TextField();
    private final TextField authorField = new TextField();
    private final TextField publisherField = new TextField();
    private final TextField stockField = new TextField();
    private final TextField queryTitleField = new TextField();
    private final TextField queryAuthorField = new TextField();
    private final TextField queryIsbnField = new TextField();
    private final TextField recordReaderField = new TextField();
    private final TextField recordIsbnField = new TextField();
    private final Label bookCountLabel = new Label();
    private final Label activeRecordLabel = new Label();
    private final Label userLabel = new Label();
    private final int initialTabIndex;

    public AdminDashboardPane(LibrarySystem librarySystem, AdminUser adminUser, Runnable logoutHandler) {
        this(librarySystem, adminUser, logoutHandler, 0);
    }

    public AdminDashboardPane(LibrarySystem librarySystem, AdminUser adminUser, Runnable logoutHandler, int initialTabIndex) {
        this.librarySystem = librarySystem;
        this.adminUser = adminUser;
        this.logoutHandler = logoutHandler;
        this.initialTabIndex = initialTabIndex;
        build();
        refreshAll();
    }

    private void build() {
        setTop(header());

        TabPane tabs = new TabPane();
        tabs.getTabs().add(tab("图书管理", bookManagementTab()));
        tabs.getTabs().add(tab("图书查询", queryTab()));
        tabs.getTabs().add(tab("借阅记录", recordsTab()));
        tabs.getTabs().add(tab("系统信息", systemInfoTab()));
        tabs.getSelectionModel().select(Math.max(0, Math.min(initialTabIndex, tabs.getTabs().size() - 1)));
        setCenter(tabs);
    }

    private HBox header() {
        Label title = new Label("图书管理系统 - 管理员模式");
        title.getStyleClass().add("page-title");
        Label user = new Label("当前用户：admin");
        user.getStyleClass().add("status-text");
        Button logout = new Button("退出");
        logout.setOnAction(event -> logoutHandler.run());
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(16, title, spacer, user, logout);
        header.getStyleClass().add("app-header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 18, 14, 18));
        return header;
    }

    private Tab tab(String title, javafx.scene.Node content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private BorderPane bookManagementTab() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.setCenter(managementTable);
        BorderPane.setMargin(managementTable, new Insets(0, 16, 0, 0));

        managementTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldValue, book) -> {
            if (book != null) {
                fillBookForm(book);
            }
        });

        VBox form = new VBox(12, sectionTitle("图书信息"), bookForm(), managementActions());
        form.getStyleClass().add("panel");
        form.setPadding(new Insets(16));
        form.setPrefWidth(330);
        root.setRight(form);
        return root;
    }

    private GridPane bookForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addFormRow(grid, 0, "ISBN", isbnField);
        addFormRow(grid, 1, "书名", titleField);
        addFormRow(grid, 2, "作者", authorField);
        addFormRow(grid, 3, "出版社", publisherField);
        addFormRow(grid, 4, "库存", stockField);
        return grid;
    }

    private HBox managementActions() {
        Button add = styledButton("添加图书", "primary-button");
        Button update = styledButton("修改图书", "primary-button");
        Button delete = styledButton("删除图书", "danger-button");
        Button clear = new Button("清空");
        add.setOnAction(event -> addBook());
        update.setOnAction(event -> updateBook());
        delete.setOnAction(event -> deleteBook());
        clear.setOnAction(event -> clearBookForm());
        HBox actions = new HBox(8, add, update, delete, clear);
        actions.setAlignment(Pos.CENTER_LEFT);
        return actions;
    }

    private VBox queryTab() {
        queryTitleField.setPromptText("书名关键词");
        queryAuthorField.setPromptText("作者");
        queryIsbnField.setPromptText("ISBN");
        Button search = styledButton("查询", "primary-button");
        Button reset = new Button("重置");
        search.setOnAction(event -> applyBookQuery());
        reset.setOnAction(event -> {
            queryTitleField.clear();
            queryAuthorField.clear();
            queryIsbnField.clear();
            queryTable.setBooks(librarySystem.getAllBooks());
        });

        HBox filters = new HBox(10,
                new Label("书名"), queryTitleField,
                new Label("作者"), queryAuthorField,
                new Label("ISBN"), queryIsbnField,
                search, reset);
        filters.setAlignment(Pos.CENTER_LEFT);
        VBox root = new VBox(12, filters, queryTable);
        root.setPadding(new Insets(16));
        VBox.setVgrow(queryTable, Priority.ALWAYS);
        return root;
    }

    private VBox recordsTab() {
        recordReaderField.setPromptText("读者姓名");
        recordIsbnField.setPromptText("ISBN");
        Button search = styledButton("查询", "primary-button");
        Button reset = new Button("重置");
        search.setOnAction(event -> recordTable.setRecords(librarySystem.searchActiveRecords(recordReaderField.getText(), recordIsbnField.getText())));
        reset.setOnAction(event -> {
            recordReaderField.clear();
            recordIsbnField.clear();
            recordTable.setRecords(librarySystem.getActiveRecords());
        });
        HBox filters = new HBox(10,
                new Label("读者姓名"), recordReaderField,
                new Label("ISBN"), recordIsbnField,
                search, reset);
        filters.setAlignment(Pos.CENTER_LEFT);
        VBox root = new VBox(12, filters, recordTable);
        root.setPadding(new Insets(16));
        VBox.setVgrow(recordTable, Priority.ALWAYS);
        return root;
    }

    private VBox systemInfoTab() {
        userLabel.setText(adminUser.getDisplayName() + "（" + adminUser.permissionDescription() + "）");

        HBox metrics = new HBox(16, metric("图书总数", bookCountLabel), metric("未还记录", activeRecordLabel), metric("当前用户", userLabel));
        metrics.setAlignment(Pos.CENTER_LEFT);

        VBox paths = new VBox(8,
                sectionTitle("数据文件"),
                new Label("books.txt: " + librarySystem.getDataDir().resolve("books.txt").toAbsolutePath()),
                new Label("records.txt: " + librarySystem.getDataDir().resolve("records.txt").toAbsolutePath()),
                new Label("users.txt: " + librarySystem.getDataDir().resolve("users.txt").toAbsolutePath()),
                new Label("系统版本：1.0.0"));
        paths.getStyleClass().add("panel");
        paths.setPadding(new Insets(16));

        VBox root = new VBox(18, metrics, paths);
        root.setPadding(new Insets(16));
        return root;
    }

    private VBox metric(String title, Label valueLabel) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("status-text");
        valueLabel.getStyleClass().add("metric-value");
        VBox box = new VBox(8, titleLabel, valueLabel);
        box.getStyleClass().add("metric");
        box.setPadding(new Insets(18));
        box.setPrefWidth(220);
        return box;
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private Button styledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    private void addFormRow(GridPane grid, int row, String label, TextField field) {
        grid.add(new Label(label), 0, row);
        field.setMaxWidth(Double.MAX_VALUE);
        grid.add(field, 1, row);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private void fillBookForm(Book book) {
        isbnField.setText(book.getIsbn());
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        publisherField.setText(book.getPublisher());
        stockField.setText(String.valueOf(book.getStock()));
    }

    private void clearBookForm() {
        isbnField.clear();
        titleField.clear();
        authorField.clear();
        publisherField.clear();
        stockField.clear();
        managementTable.getTable().getSelectionModel().clearSelection();
    }

    private void addBook() {
        try {
            librarySystem.addBook(isbnField.getText(), titleField.getText(), authorField.getText(),
                    publisherField.getText(), stockField.getText());
            refreshAll();
            AlertUtils.info("添加图书成功");
        } catch (LibraryException ex) {
            AlertUtils.warn(ex.getMessage());
        }
    }

    private void updateBook() {
        try {
            librarySystem.updateBook(isbnField.getText(), titleField.getText(), authorField.getText(),
                    publisherField.getText(), stockField.getText());
            refreshAll();
            AlertUtils.info("修改图书成功");
        } catch (LibraryException ex) {
            AlertUtils.warn(ex.getMessage());
        }
    }

    private void deleteBook() {
        if (!AlertUtils.confirm("确定要删除 ISBN 为 " + isbnField.getText() + " 的图书吗？")) {
            return;
        }
        try {
            librarySystem.deleteBook(isbnField.getText());
            clearBookForm();
            refreshAll();
            AlertUtils.info("删除图书成功");
        } catch (LibraryException ex) {
            AlertUtils.warn(ex.getMessage());
        }
    }

    private void applyBookQuery() {
        queryTable.setBooks(librarySystem.searchBooks(queryTitleField.getText(), queryAuthorField.getText(), queryIsbnField.getText()));
        if (queryTable.getTable().getItems().isEmpty()) {
            AlertUtils.warn("没有找到符合条件的图书");
        }
    }

    private void refreshAll() {
        managementTable.setBooks(librarySystem.getAllBooks());
        queryTable.setBooks(librarySystem.getAllBooks());
        recordTable.setRecords(librarySystem.getActiveRecords());
        bookCountLabel.setText(String.valueOf(librarySystem.getBookCount()));
        activeRecordLabel.setText(String.valueOf(librarySystem.getActiveRecordCount()));
    }
}
