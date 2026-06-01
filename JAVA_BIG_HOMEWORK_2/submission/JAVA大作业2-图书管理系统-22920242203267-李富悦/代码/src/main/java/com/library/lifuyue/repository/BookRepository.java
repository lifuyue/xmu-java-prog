package com.library.lifuyue.repository;

import com.library.lifuyue.model.Book;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private final Path file;

    public BookRepository(Path file) {
        this.file = file;
    }

    public List<Book> load() throws RepositoryException {
        ensureFile();
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split(",", -1);
                if (parts.length != 5) {
                    continue;
                }
                try {
                    books.add(new Book(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                            Integer.parseInt(parts[4].trim())));
                } catch (NumberFormatException ignored) {
                    // Bad rows are skipped so one malformed line does not block the whole system.
                }
            }
        } catch (IOException ex) {
            throw new RepositoryException("读取图书文件失败：" + file, ex);
        }
        return books;
    }

    public void save(List<Book> books) throws RepositoryException {
        ensureFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), StandardCharsets.UTF_8))) {
            for (Book book : books) {
                writer.write(String.join(",",
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        String.valueOf(book.getStock())));
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new RepositoryException("保存图书文件失败：" + file, ex);
        }
    }

    private void ensureFile() throws RepositoryException {
        try {
            Files.createDirectories(file.getParent());
            if (Files.notExists(file)) {
                Files.createFile(file);
            }
        } catch (IOException ex) {
            throw new RepositoryException("创建图书文件失败：" + file, ex);
        }
    }
}
