package com.library.lifuyue.service;

import com.library.lifuyue.model.AdminUser;
import com.library.lifuyue.model.Book;
import com.library.lifuyue.model.BorrowRecord;
import com.library.lifuyue.model.ReaderUser;
import com.library.lifuyue.model.User;
import com.library.lifuyue.model.UserRole;
import com.library.lifuyue.repository.BookRepository;
import com.library.lifuyue.repository.BorrowRecordRepository;
import com.library.lifuyue.repository.RepositoryException;
import com.library.lifuyue.repository.UserRepository;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibrarySystem {
    private final Path dataDir;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final Map<String, Book> books = new LinkedHashMap<>();
    private final List<BorrowRecord> records = new ArrayList<>();
    private final Map<String, User> users = new HashMap<>();

    public LibrarySystem(Path dataDir) {
        this.dataDir = dataDir;
        this.bookRepository = new BookRepository(dataDir.resolve("books.txt"));
        this.recordRepository = new BorrowRecordRepository(dataDir.resolve("records.txt"));
        this.userRepository = new UserRepository(dataDir.resolve("users.txt"));
    }

    public void load() throws LibraryException {
        try {
            books.clear();
            for (Book book : bookRepository.load()) {
                books.put(book.getIsbn(), book);
            }
            records.clear();
            records.addAll(recordRepository.load());
            users.clear();
            for (User user : userRepository.load()) {
                users.put(user.getUsername(), user);
            }
            ensureDefaultData();
        } catch (RepositoryException ex) {
            throw new LibraryException(ex.getMessage(), ex);
        }
    }

    public void saveAll() throws LibraryException {
        try {
            bookRepository.save(getAllBooks());
            recordRepository.save(new ArrayList<>(records));
            userRepository.save(getAllUsers());
        } catch (RepositoryException ex) {
            throw new LibraryException(ex.getMessage(), ex);
        }
    }

    public Path getDataDir() {
        return dataDir;
    }

    public User findUser(String username) {
        return users.get(username);
    }

    public ReaderUser findOrCreateReader(String displayName) throws LibraryException {
        for (User user : users.values()) {
            if (user.getRole() == UserRole.READER && user.getDisplayName().equals(displayName)) {
                return (ReaderUser) user;
            }
        }
        String username = nextReaderUsername(displayName);
        ReaderUser reader = new ReaderUser(username, displayName);
        users.put(username, reader);
        saveAll();
        return reader;
    }

    public List<User> getAllUsers() {
        return users.values().stream()
                .sorted(Comparator.comparing(User::getRole).thenComparing(User::getUsername))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public Book findBook(String isbn) {
        return books.get(normalize(isbn));
    }

    public void addBook(String isbn, String title, String author, String publisher, String stockText) throws LibraryException {
        String cleanIsbn = requireText(isbn, "请输入 ISBN");
        if (books.containsKey(cleanIsbn)) {
            throw new LibraryException("ISBN 已存在，不能重复添加");
        }
        Book book = new Book(
                cleanIsbn,
                requireText(title, "请输入书名"),
                requireText(author, "请输入作者"),
                requireText(publisher, "请输入出版社"),
                parseStock(stockText));
        books.put(book.getIsbn(), book);
        saveAll();
    }

    public void updateBook(String isbn, String title, String author, String publisher, String stockText) throws LibraryException {
        Book book = requireBook(isbn);
        book.setTitle(requireText(title, "请输入书名"));
        book.setAuthor(requireText(author, "请输入作者"));
        book.setPublisher(requireText(publisher, "请输入出版社"));
        book.setStock(parseStock(stockText));
        saveAll();
    }

    public void deleteBook(String isbn) throws LibraryException {
        Book book = requireBook(isbn);
        boolean hasActiveRecord = records.stream()
                .anyMatch(record -> !record.isReturned() && record.getIsbn().equals(book.getIsbn()));
        if (hasActiveRecord) {
            throw new LibraryException("该图书存在未归还记录，不能删除");
        }
        books.remove(book.getIsbn());
        saveAll();
    }

    public List<Book> searchBooks(String titleKeyword, String author, String isbn) {
        String titleFilter = normalizeLower(titleKeyword);
        String authorFilter = normalizeLower(author);
        String isbnFilter = normalize(isbn);
        return books.values().stream()
                .filter(book -> titleFilter.isEmpty() || book.getTitle().toLowerCase(Locale.ROOT).contains(titleFilter))
                .filter(book -> authorFilter.isEmpty() || book.getAuthor().toLowerCase(Locale.ROOT).contains(authorFilter))
                .filter(book -> isbnFilter.isEmpty() || book.getIsbn().equals(isbnFilter))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public BorrowRecord borrowBook(String isbn, String readerName) throws LibraryException {
        Book book = requireBook(isbn);
        String reader = requireText(readerName, "请输入读者姓名");
        if (book.getStock() <= 0) {
            throw new LibraryException("库存为 0，不能借阅");
        }
        boolean duplicate = records.stream()
                .anyMatch(record -> !record.isReturned()
                        && record.getIsbn().equals(book.getIsbn())
                        && record.getReaderName().equals(reader));
        if (duplicate) {
            throw new LibraryException("同一本书同一读者不可重复借阅");
        }
        BorrowRecord record = new BorrowRecord(nextRecordId(), reader, book.getIsbn(), book.getTitle(), LocalDate.now(), null, false);
        book.decreaseStock();
        records.add(record);
        saveAll();
        return record;
    }

    public BorrowRecord returnBook(String isbn, String readerName) throws LibraryException {
        Book book = requireBook(isbn);
        String reader = requireText(readerName, "请输入读者姓名");
        Optional<BorrowRecord> match = records.stream()
                .filter(record -> !record.isReturned()
                        && record.getIsbn().equals(book.getIsbn())
                        && record.getReaderName().equals(reader))
                .findFirst();
        if (match.isEmpty()) {
            throw new LibraryException("没有找到该读者的未归还记录");
        }
        BorrowRecord record = match.get();
        record.markReturned(LocalDate.now());
        book.increaseStock();
        saveAll();
        return record;
    }

    public List<BorrowRecord> getActiveRecords() {
        return records.stream()
                .filter(record -> !record.isReturned())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<BorrowRecord> getActiveRecordsForReader(String readerName) {
        return getActiveRecords().stream()
                .filter(record -> record.getReaderName().equals(readerName))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<BorrowRecord> searchActiveRecords(String readerName, String isbn) {
        String readerFilter = normalizeLower(readerName);
        String isbnFilter = normalize(isbn);
        return getActiveRecords().stream()
                .filter(record -> readerFilter.isEmpty() || record.getReaderName().toLowerCase(Locale.ROOT).contains(readerFilter))
                .filter(record -> isbnFilter.isEmpty() || record.getIsbn().equals(isbnFilter))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int getBookCount() {
        return books.size();
    }

    public int getActiveRecordCount() {
        return getActiveRecords().size();
    }

    private void ensureDefaultData() throws LibraryException {
        if (users.values().stream().noneMatch(User::isAdmin)) {
            users.put("admin", new AdminUser("admin", "admin123", "管理员"));
        }
        if (books.isEmpty()) {
            books.put("9787111636663", new Book("9787111636663", "Java程序设计", "张明", "清华大学出版社", 8));
            books.put("9787302561234", new Book("9787302561234", "数据结构", "李华", "高等教育出版社", 5));
            books.put("9787115479983", new Book("9787115479983", "Java Web开发实战", "王磊", "人民邮电出版社", 3));
            books.put("9787121400001", new Book("9787121400001", "计算机网络", "谢希仁", "电子工业出版社", 0));
        }
        saveAll();
    }

    private Book requireBook(String isbn) throws LibraryException {
        String cleanIsbn = requireText(isbn, "请输入 ISBN");
        Book book = books.get(cleanIsbn);
        if (book == null) {
            throw new LibraryException("没有找到 ISBN 为 " + cleanIsbn + " 的图书");
        }
        return book;
    }

    private String requireText(String value, String message) throws LibraryException {
        if (value == null || value.trim().isEmpty()) {
            throw new LibraryException(message);
        }
        String trimmed = value.trim();
        if (trimmed.contains(",")) {
            throw new LibraryException("输入内容不能包含英文逗号");
        }
        return trimmed;
    }

    private int parseStock(String value) throws LibraryException {
        try {
            int stock = Integer.parseInt(requireText(value, "请输入库存数量"));
            if (stock < 0) {
                throw new LibraryException("库存数量不能小于 0");
            }
            return stock;
        } catch (NumberFormatException ex) {
            throw new LibraryException("库存数量必须是整数");
        }
    }

    private String nextRecordId() {
        return "R" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + String.format("%04d", records.size() + 1);
    }

    private String nextReaderUsername(String displayName) {
        String base = "reader-" + Integer.toHexString(displayName.hashCode()).replace("-", "");
        String candidate = base;
        int index = 1;
        while (users.containsKey(candidate)) {
            candidate = base + "-" + index;
            index++;
        }
        return candidate;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeLower(String value) {
        return normalize(value).toLowerCase(Locale.ROOT);
    }
}
