package com.library.lifuyue.repository;

import com.library.lifuyue.model.BorrowRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordRepository {
    private final Path file;

    public BorrowRecordRepository(Path file) {
        this.file = file;
    }

    public List<BorrowRecord> load() throws RepositoryException {
        ensureFile();
        List<BorrowRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split(",", -1);
                if (parts.length != 7) {
                    continue;
                }
                try {
                    LocalDate borrowDate = LocalDate.parse(parts[4].trim());
                    LocalDate returnDate = parts[5].trim().isEmpty() ? null : LocalDate.parse(parts[5].trim());
                    records.add(new BorrowRecord(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            borrowDate,
                            returnDate,
                            Boolean.parseBoolean(parts[6].trim())));
                } catch (DateTimeParseException ignored) {
                    // Bad rows are skipped so valid records can still be loaded.
                }
            }
        } catch (IOException ex) {
            throw new RepositoryException("读取借阅记录文件失败：" + file, ex);
        }
        return records;
    }

    public void save(List<BorrowRecord> records) throws RepositoryException {
        ensureFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), StandardCharsets.UTF_8))) {
            for (BorrowRecord record : records) {
                writer.write(String.join(",",
                        record.getRecordId(),
                        record.getReaderName(),
                        record.getIsbn(),
                        record.getBookTitle(),
                        record.getBorrowDate().toString(),
                        record.getReturnDate() == null ? "" : record.getReturnDate().toString(),
                        String.valueOf(record.isReturned())));
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new RepositoryException("保存借阅记录文件失败：" + file, ex);
        }
    }

    private void ensureFile() throws RepositoryException {
        try {
            Files.createDirectories(file.getParent());
            if (Files.notExists(file)) {
                Files.createFile(file);
            }
        } catch (IOException ex) {
            throw new RepositoryException("创建借阅记录文件失败：" + file, ex);
        }
    }
}
