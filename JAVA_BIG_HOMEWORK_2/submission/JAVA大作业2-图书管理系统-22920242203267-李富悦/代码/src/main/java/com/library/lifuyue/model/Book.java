package com.library.lifuyue.model;

public class Book {
    private final String isbn;
    private String title;
    private String author;
    private String publisher;
    private int stock;

    public Book(String isbn, String title, String author, String publisher, int stock) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void decreaseStock() {
        stock--;
    }

    public void increaseStock() {
        stock++;
    }
}
