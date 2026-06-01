package com.library.lifuyue.model;

public class AdminUser extends User {
    public AdminUser(String username, String password, String displayName) {
        super(username, password, displayName, UserRole.ADMIN);
    }

    @Override
    public String permissionDescription() {
        return "可管理图书、查询图书和查看所有未归还记录";
    }
}
