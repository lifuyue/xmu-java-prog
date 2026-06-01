package com.library.lifuyue.model;

public class ReaderUser extends User {
    public ReaderUser(String username, String displayName) {
        super(username, "", displayName, UserRole.READER);
    }

    @Override
    public String permissionDescription() {
        return "可查询图书、借书、还书和查看本人借阅记录";
    }
}
