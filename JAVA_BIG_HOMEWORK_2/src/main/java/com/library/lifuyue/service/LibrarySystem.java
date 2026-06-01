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

/**
 * 图书管理系统的业务核心类。
 *
 * 设计目的：让 JavaFX 界面只负责显示和按钮事件，把库存、借阅、删除校验、
 * 文件保存等业务规则集中在这里。答辩时如果老师问“系统怎么工作”，可以从
 * load -> UI 调用业务方法 -> saveAll 这条主线说明。
 */
public class LibrarySystem {
    private final Path dataDir;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository recordRepository;
    private final UserRepository userRepository;
    // ISBN 是图书的唯一标识，用 Map 可以快速按 ISBN 找到图书。
    private final Map<String, Book> books = new LinkedHashMap<>();
    // 借阅记录需要保留历史顺序，所以使用 List 保存。
    private final List<BorrowRecord> records = new ArrayList<>();
    // 用户账号唯一，用 Map 支持管理员登录时快速查找账号。
    private final Map<String, User> users = new HashMap<>();

    public LibrarySystem(Path dataDir) {
        this.dataDir = dataDir;
        this.bookRepository = new BookRepository(dataDir.resolve("books.txt"));
        this.recordRepository = new BorrowRecordRepository(dataDir.resolve("records.txt"));
        this.userRepository = new UserRepository(dataDir.resolve("users.txt"));
    }

    public void load() throws LibraryException {
        try {
            // 程序启动时一次性把三个文本文件读入内存，界面后续都操作内存对象。
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
            // 每次增删改、借书、还书后调用，保证程序重启后数据不丢失。
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
        // 普通读者不需要密码。若姓名已经存在，直接复用；否则创建新读者并保存。
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
        // ISBN 作为图书唯一键，添加前必须先检查重复。
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
        // 修改必须基于已经存在的 ISBN。界面选中表格行后会把 ISBN 自动填入表单。
        Book book = requireBook(isbn);
        book.setTitle(requireText(title, "请输入书名"));
        book.setAuthor(requireText(author, "请输入作者"));
        book.setPublisher(requireText(publisher, "请输入出版社"));
        book.setStock(parseStock(stockText));
        saveAll();
    }

    public void deleteBook(String isbn) throws LibraryException {
        Book book = requireBook(isbn);
        // 有未归还记录时不允许删除图书，否则借阅记录会指向一条不存在的图书。
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
        // 书名和作者用包含查询，ISBN 用精确匹配，满足作业要求的三种查询方式。
        return books.values().stream()
                .filter(book -> titleFilter.isEmpty() || book.getTitle().toLowerCase(Locale.ROOT).contains(titleFilter))
                .filter(book -> authorFilter.isEmpty() || book.getAuthor().toLowerCase(Locale.ROOT).contains(authorFilter))
                .filter(book -> isbnFilter.isEmpty() || book.getIsbn().equals(isbnFilter))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public BorrowRecord borrowBook(String isbn, String readerName) throws LibraryException {
        Book book = requireBook(isbn);
        String reader = requireText(readerName, "请输入读者姓名");
        // 规则 1：库存为 0 时不能借阅。
        if (book.getStock() <= 0) {
            throw new LibraryException("库存为 0，不能借阅");
        }
        // 规则 2：同一本书同一读者只能有一条未归还记录。
        boolean duplicate = records.stream()
                .anyMatch(record -> !record.isReturned()
                        && record.getIsbn().equals(book.getIsbn())
                        && record.getReaderName().equals(reader));
        if (duplicate) {
            throw new LibraryException("同一本书同一读者不可重复借阅");
        }
        BorrowRecord record = new BorrowRecord(nextRecordId(), reader, book.getIsbn(), book.getTitle(), LocalDate.now(), null, false);
        // 借书成功后，库存减少，同时新增一条未归还记录。
        book.decreaseStock();
        records.add(record);
        saveAll();
        return record;
    }

    public BorrowRecord returnBook(String isbn, String readerName) throws LibraryException {
        Book book = requireBook(isbn);
        String reader = requireText(readerName, "请输入读者姓名");
        // 还书时只查找“当前读者 + 当前 ISBN + 未归还”的记录。
        Optional<BorrowRecord> match = records.stream()
                .filter(record -> !record.isReturned()
                        && record.getIsbn().equals(book.getIsbn())
                        && record.getReaderName().equals(reader))
                .findFirst();
        if (match.isEmpty()) {
            throw new LibraryException("没有找到该读者的未归还记录");
        }
        BorrowRecord record = match.get();
        // 找到记录后标记归还日期，并把库存加回去。
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
