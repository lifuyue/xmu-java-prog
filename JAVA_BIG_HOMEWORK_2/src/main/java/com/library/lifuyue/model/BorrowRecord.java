package com.library.lifuyue.model;

import java.time.LocalDate;

public class BorrowRecord {
    private final String recordId;
    private final String readerName;
    private final String isbn;
    private final String bookTitle;
    private final LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean returned;

    public BorrowRecord(String recordId, String readerName, String isbn, String bookTitle,
                        LocalDate borrowDate, LocalDate returnDate, boolean returned) {
        this.recordId = recordId;
        this.readerName = readerName;
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getReaderName() {
        return readerName;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public String getReturnedText() {
        return returned ? "已归还" : "未归还";
    }

    public void markReturned(LocalDate date) {
        this.returnDate = date;
        this.returned = true;
    }
}
