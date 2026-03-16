/**
 * 学生类。
 */
public class Student {
    private final String studentId;
    private final String name;
    private final String className;
    private final String phone;

    public Student(String studentId, String name, String className) {
        this(studentId, name, className, "");
    }

    public Student(String studentId, String name, String className, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.className = className;
        this.phone = phone == null ? "" : phone.trim();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getPhone() {
        return phone;
    }
}
