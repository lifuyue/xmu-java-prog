package com.example.lab10;

public record Student(String id, String name, String phone, String email, String photoPath) {
    public String photoFileName() {
        if (photoPath == null || photoPath.isBlank()) {
            return "";
        }
        int index = Math.max(photoPath.lastIndexOf('/'), photoPath.lastIndexOf('\\'));
        return index >= 0 ? photoPath.substring(index + 1) : photoPath;
    }
}
