package com.library.lifuyue.ui;

import com.library.lifuyue.model.Book;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.util.Collection;

public class BookTablePane extends BorderPane {
    private final ObservableList<Book> data = FXCollections.observableArrayList();
    private final TableView<Book> table = new TableView<>(data);

    public BookTablePane() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.getColumns().add(isbnColumn());
        table.getColumns().add(textColumn("书名", Book::getTitle, 190));
        table.getColumns().add(textColumn("作者", Book::getAuthor, 120));
        table.getColumns().add(textColumn("出版社", Book::getPublisher, 170));
        table.getColumns().add(stockColumn());
        setCenter(table);
    }

    public void setBooks(Collection<Book> books) {
        data.setAll(books);
    }

    public Book getSelectedBook() {
        return table.getSelectionModel().getSelectedItem();
    }

    public TableView<Book> getTable() {
        return table;
    }

    private TableColumn<Book, String> isbnColumn() {
        return textColumn("ISBN", Book::getIsbn, 150);
    }

    private TableColumn<Book, String> textColumn(String title, TextGetter getter, double width) {
        TableColumn<Book, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(getter.get(cell.getValue())));
        column.setPrefWidth(width);
        return column;
    }

    private TableColumn<Book, Number> stockColumn() {
        TableColumn<Book, Number> column = new TableColumn<>("库存");
        column.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getStock()));
        column.setPrefWidth(70);
        return column;
    }

    private interface TextGetter {
        String get(Book book);
    }
}
