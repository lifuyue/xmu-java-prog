package com.library.lifuyue.ui;

import com.library.lifuyue.model.Book;
import com.library.lifuyue.model.ReaderUser;
import com.library.lifuyue.service.LibrarySystem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ReaderDashboardPane extends BorderPane {
    private final LibrarySystem librarySystem;
    private final ReaderUser reader;
    private final Runnable logoutHandler;
    private final BookTablePane queryTable = new BookTablePane();
    private final BorrowRecordTablePane myRecordTable = new BorrowRecordTablePane();
    private final TextField queryTitleField = new TextField();
    private final TextField queryAuthorField = new TextField();
    private final TextField queryIsbnField = new TextField();
    private final int initialTabIndex;
    private BorrowPane borrowPane;

    public ReaderDashboardPane(LibrarySystem librarySystem, ReaderUser reader, Runnable logoutHandler) {
        this(librarySystem, reader, logoutHandler, 0);
    }

    public ReaderDashboardPane(LibrarySystem librarySystem, ReaderUser reader, Runnable logoutHandler, int initialTabIndex) {
        this.librarySystem = librarySystem;
        this.reader = reader;
        this.logoutHandler = logoutHandler;
        this.initialTabIndex = initialTabIndex;
        build();
        refreshAll();
    }

    private void build() {
        setTop(header());

        borrowPane = new BorrowPane(librarySystem, reader, this::refreshAll);
        queryTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldValue, book) -> {
            if (book != null) {
                borrowPane.setIsbn(book.getIsbn());
            }
        });

        TabPane tabs = new TabPane();
        tabs.getTabs().add(tab("图书查询", queryTab()));
        tabs.getTabs().add(tab("借阅归还", borrowPane));
        tabs.getTabs().add(tab("我的借阅", myRecordsTab()));
        tabs.getSelectionModel().select(Math.max(0, Math.min(initialTabIndex, tabs.getTabs().size() - 1)));
        setCenter(tabs);
    }

    private HBox header() {
        Label title = new Label("图书管理系统 - 读者模式");
        title.getStyleClass().add("page-title");
        Label user = new Label("当前用户：" + reader.getDisplayName());
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

        Label hint = new Label("选择表格中的图书后，可切换到“借阅归还”页签直接借书或还书。");
        hint.getStyleClass().add("status-text");

        VBox root = new VBox(12, filters, queryTable, hint);
        root.setPadding(new Insets(16));
        VBox.setVgrow(queryTable, Priority.ALWAYS);
        return root;
    }

    private VBox myRecordsTab() {
        Label title = new Label("我当前的未还图书");
        title.getStyleClass().add("section-title");
        Label hint = new Label("温馨提示：请及时归还图书。");
        hint.getStyleClass().add("status-text");
        VBox root = new VBox(12, title, myRecordTable, hint);
        root.setPadding(new Insets(16));
        VBox.setVgrow(myRecordTable, Priority.ALWAYS);
        return root;
    }

    private Button styledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    private void applyBookQuery() {
        queryTable.setBooks(librarySystem.searchBooks(queryTitleField.getText(), queryAuthorField.getText(), queryIsbnField.getText()));
        if (queryTable.getTable().getItems().isEmpty()) {
            AlertUtils.warn("没有找到符合条件的图书");
        }
    }

    private void refreshAll() {
        queryTable.setBooks(librarySystem.getAllBooks());
        myRecordTable.setRecords(librarySystem.getActiveRecordsForReader(reader.getDisplayName()));
        Book selected = queryTable.getSelectedBook();
        if (selected != null) {
            borrowPane.setIsbn(selected.getIsbn());
        } else if (initialTabIndex == 1) {
            borrowPane.setIsbn("9787111636663");
        }
    }
}
