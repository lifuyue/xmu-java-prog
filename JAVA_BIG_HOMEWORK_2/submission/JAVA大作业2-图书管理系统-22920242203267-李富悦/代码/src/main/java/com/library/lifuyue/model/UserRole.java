package com.library.lifuyue.model;

public enum UserRole {
    ADMIN("管理员"),
    READER("普通读者");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
