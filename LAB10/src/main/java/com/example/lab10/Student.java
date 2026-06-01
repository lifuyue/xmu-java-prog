package com.example.lab10;

/**
 * 学生顺序文件中的一条记录，record 适合这种只保存字段的数据对象。
 */
public record Student(String id, String name, String phone, String email, String photoPath) {
    public String photoFileName() {
        if (photoPath == null || photoPath.isBlank()) {
            return "";
        }
        // 表格中只显示文件名，避免完整路径太长影响阅读。
        int index = Math.max(photoPath.lastIndexOf('/'), photoPath.lastIndexOf('\\'));
        return index >= 0 ? photoPath.substring(index + 1) : photoPath;
    }
}
