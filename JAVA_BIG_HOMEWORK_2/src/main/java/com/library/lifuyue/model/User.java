package com.library.lifuyue.model;

public abstract class User {
    private final String username;
    private final String password;
    private final String displayName;
    private final UserRole role;

    protected User(String username, String password, String displayName, UserRole role) {
        this.username = username;
        this.password = password == null ? "" : password;
        this.displayName = displayName;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public abstract String permissionDescription();
}
