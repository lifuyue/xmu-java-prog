package com.library.lifuyue.repository;

import com.library.lifuyue.model.AdminUser;
import com.library.lifuyue.model.ReaderUser;
import com.library.lifuyue.model.User;
import com.library.lifuyue.model.UserRole;

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

public class UserRepository {
    private final Path file;

    public UserRepository(Path file) {
        this.file = file;
    }

    public List<User> load() throws RepositoryException {
        ensureFile();
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] parts = trimmed.split(",", -1);
                if (parts.length != 4) {
                    continue;
                }
                try {
                    UserRole role = UserRole.valueOf(parts[2].trim());
                    if (role == UserRole.ADMIN) {
                        users.add(new AdminUser(parts[0].trim(), parts[1].trim(), parts[3].trim()));
                    } else {
                        users.add(new ReaderUser(parts[0].trim(), parts[3].trim()));
                    }
                } catch (IllegalArgumentException ignored) {
                    // Unknown roles are skipped.
                }
            }
        } catch (IOException ex) {
            throw new RepositoryException("读取用户文件失败：" + file, ex);
        }
        return users;
    }

    public void save(List<User> users) throws RepositoryException {
        ensureFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), StandardCharsets.UTF_8))) {
            for (User user : users) {
                writer.write(String.join(",",
                        user.getUsername(),
                        user.getPassword(),
                        user.getRole().name(),
                        user.getDisplayName()));
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new RepositoryException("保存用户文件失败：" + file, ex);
        }
    }

    private void ensureFile() throws RepositoryException {
        try {
            Files.createDirectories(file.getParent());
            if (Files.notExists(file)) {
                Files.createFile(file);
            }
        } catch (IOException ex) {
            throw new RepositoryException("创建用户文件失败：" + file, ex);
        }
    }
}
