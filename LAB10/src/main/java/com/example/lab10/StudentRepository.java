package com.example.lab10;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private final Path dataFile;
    private final Path photosDir;

    public StudentRepository(Path dataDir) {
        this.dataFile = dataDir.resolve("students.txt");
        this.photosDir = dataDir.resolve("photos");
    }

    public Path dataFile() {
        return dataFile;
    }

    public Path photosDir() {
        return photosDir;
    }

    public List<Student> load() throws IOException {
        ensureStorage();
        List<Student> students = new ArrayList<>();
        for (String line : Files.readAllLines(dataFile, StandardCharsets.UTF_8)) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\\t", -1);
            if (parts.length >= 5) {
                students.add(new Student(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        }
        return students;
    }

    public void save(List<Student> students) throws IOException {
        ensureStorage();
        List<String> lines = students.stream()
                .map(this::serialize)
                .toList();
        Path tmp = dataFile.resolveSibling(dataFile.getFileName() + ".tmp");
        Files.write(tmp, lines, StandardCharsets.UTF_8);
        Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    public String copyPhoto(Path source, String studentId) throws IOException {
        ensureStorage();
        if (source == null || !Files.isRegularFile(source)) {
            throw new IOException("照片文件不存在");
        }
        String name = source.getFileName().toString().toLowerCase();
        if (!name.endsWith(".jpg") && !name.endsWith(".jpeg")) {
            throw new IOException("请选择 .JPG 或 .JPEG 照片文件");
        }
        String extension = name.endsWith(".jpeg") ? ".jpeg" : ".jpg";
        String stamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        Path target = photosDir.resolve(clean(studentId) + "-" + stamp + extension);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return Path.of(dataFile.getParent().getFileName().toString(), "photos", target.getFileName().toString()).toString();
    }

    private void ensureStorage() throws IOException {
        Files.createDirectories(photosDir);
        if (Files.notExists(dataFile)) {
            Files.createFile(dataFile);
        }
    }

    private String serialize(Student student) {
        return String.join("\t",
                safe(student.id()),
                safe(student.name()),
                safe(student.phone()),
                safe(student.email()),
                safe(student.photoPath()));
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ').trim();
    }

    private String clean(String value) {
        return safe(value).replaceAll("[^A-Za-z0-9_-]", "_");
    }
}
