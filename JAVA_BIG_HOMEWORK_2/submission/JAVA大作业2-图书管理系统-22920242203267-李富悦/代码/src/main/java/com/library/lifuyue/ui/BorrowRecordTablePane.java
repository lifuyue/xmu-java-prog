package com.library.lifuyue.ui;

import com.library.lifuyue.model.BorrowRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.util.Collection;

public class BorrowRecordTablePane extends BorderPane {
    private final ObservableList<BorrowRecord> data = FXCollections.observableArrayList();
    private final TableView<BorrowRecord> table = new TableView<>(data);

    public BorrowRecordTablePane() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.getColumns().add(textColumn("记录编号", BorrowRecord::getRecordId, 130));
        table.getColumns().add(textColumn("读者", BorrowRecord::getReaderName, 110));
        table.getColumns().add(textColumn("ISBN", BorrowRecord::getIsbn, 145));
        table.getColumns().add(textColumn("书名", BorrowRecord::getBookTitle, 170));
        table.getColumns().add(textColumn("借阅日期", record -> record.getBorrowDate().toString(), 110));
        table.getColumns().add(textColumn("归还状态", BorrowRecord::getReturnedText, 90));
        setCenter(table);
    }

    public void setRecords(Collection<BorrowRecord> records) {
        data.setAll(records);
    }

    private TableColumn<BorrowRecord, String> textColumn(String title, TextGetter getter, double width) {
        TableColumn<BorrowRecord, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(getter.get(cell.getValue())));
        column.setPrefWidth(width);
        return column;
    }

    private interface TextGetter {
        String get(BorrowRecord record);
    }
}
