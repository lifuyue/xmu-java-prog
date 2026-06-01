package com.library.lifuyue.service;

import com.library.lifuyue.model.AdminUser;
import com.library.lifuyue.model.ReaderUser;
import com.library.lifuyue.model.User;
import com.library.lifuyue.model.UserRole;

public class AuthService {
    private final LibrarySystem librarySystem;

    public AuthService(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem;
    }

    public AdminUser loginAdmin(String username, String password) throws LibraryException {
        String account = requireText(username, "请输入管理员账号");
        String secret = requireText(password, "请输入管理员密码");
        User user = librarySystem.findUser(account);
        if (user == null || user.getRole() != UserRole.ADMIN || !user.getPassword().equals(secret)) {
            throw new LibraryException("管理员账号或密码错误");
        }
        return (AdminUser) user;
    }

    public ReaderUser enterReader(String displayName) throws LibraryException {
        String name = requireText(displayName, "请输入读者姓名");
        return librarySystem.findOrCreateReader(name);
    }

    private String requireText(String value, String message) throws LibraryException {
        if (value == null || value.trim().isEmpty()) {
            throw new LibraryException(message);
        }
        if (value.contains(",")) {
            throw new LibraryException("输入内容不能包含英文逗号");
        }
        return value.trim();
    }
}
