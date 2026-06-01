package com.library.lifuyue.ui;

import com.library.lifuyue.model.Book;
import com.library.lifuyue.model.ReaderUser;
import com.library.lifuyue.service.LibraryException;
import com.library.lifuyue.service.LibrarySystem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BorrowPane extends VBox {
    private final LibrarySystem librarySystem;
    private final ReaderUser reader;
    private final Runnable refreshHandler;
    private final TextField isbnField = new TextField();
    private final Label isbnValue = new Label("-");
    private final Label titleValue = new Label("-");
    private final Label authorValue = new Label("-");
    private final Label publisherValue = new Label("-");
    private final Label stockValue = new Label("-");
    private final Label statusValue = new Label("-");

    public BorrowPane(LibrarySystem librarySystem, ReaderUser reader, Runnable refreshHandler) {
        this.librarySystem = librarySystem;
        this.reader = reader;
        this.refreshHandler = refreshHandler;
        build();
    }

    public void setIsbn(String isbn) {
        isbnField.setText(isbn);
        loadBookInfo();
    }

    private void build() {
        setSpacing(16);
        setPadding(new Insets(16));

        Label title = new Label("借阅 / 归还");
        title.getStyleClass().add("section-title");

        HBox searchRow = new HBox(10, new Label("ISBN"), isbnField, actionButton("获取图书信息", "primary-button", this::loadBookInfo));
        searchRow.setAlignment(Pos.CENTER_LEFT);
        isbnField.setPromptText("输入 ISBN");
        isbnField.setPrefWidth(260);
        isbnField.setOnAction(event -> loadBookInfo());

        GridPane info = new GridPane();
        info.getStyleClass().add("panel");
        info.setPadding(new Insets(16));
        info.setHgap(12);
        info.setVgap(12);
        addInfoRow(info, 0, "ISBN", isbnValue);
        addInfoRow(info, 1, "书名", titleValue);
        addInfoRow(info, 2, "作者", authorValue);
        addInfoRow(info, 3, "出版社", publisherValue);
        addInfoRow(info, 4, "库存", stockValue);
        addInfoRow(info, 5, "可借状态", statusValue);

        Button borrowButton = actionButton("借书", "success-button", this::borrowBook);
        Button returnButton = actionButton("还书", "warning-button", this::returnBook);
        HBox actions = new HBox(12, borrowButton, returnButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(title, searchRow, info, actions);
    }

    private void addInfoRow(GridPane grid, int row, String label, Label value) {
        Label name = new Label(label);
        name.getStyleClass().add("status-text");
        grid.add(name, 0, row);
        grid.add(value, 1, row);
    }

    private Button actionButton(String text, String styleClass, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setOnAction(event -> action.run());
        return button;
    }

    private void loadBookInfo() {
        Book book = librarySystem.findBook(isbnField.getText());
        if (book == null) {
            isbnValue.setText("-");
            titleValue.setText("-");
            authorValue.setText("-");
            publisherValue.setText("-");
            stockValue.setText("-");
            statusValue.setText("未找到图书");
            return;
        }
        isbnValue.setText(book.getIsbn());
        titleValue.setText(book.getTitle());
        authorValue.setText(book.getAuthor());
        publisherValue.setText(book.getPublisher());
        stockValue.setText(book.getStock() + " 册");
        statusValue.setText(book.getStock() > 0 ? "可借" : "库存为 0");
    }

    private void borrowBook() {
        try {
            librarySystem.borrowBook(isbnField.getText(), reader.getDisplayName());
            loadBookInfo();
            refreshHandler.run();
            AlertUtils.info("借书成功");
        } catch (LibraryException ex) {
            AlertUtils.warn(ex.getMessage());
        }
    }

    private void returnBook() {
        try {
            librarySystem.returnBook(isbnField.getText(), reader.getDisplayName());
            loadBookInfo();
            refreshHandler.run();
            AlertUtils.info("还书成功");
        } catch (LibraryException ex) {
            AlertUtils.warn(ex.getMessage());
        }
    }
}
