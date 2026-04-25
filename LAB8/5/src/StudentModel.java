import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentModel {
    private final List<Student> students = new ArrayList<>();

    public StudentModel() {
        students.add(new Student("22920242203267", "李富悦", "软件工程", 92));
        students.add(new Student("22920242203001", "张三", "软件工程", 86));
        students.add(new Student("22920242203002", "王五", "计算机科学", 78));
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public void addStudent(Student student) {
        if (findById(student.getId()) != null) {
            throw new IllegalArgumentException("学号已存在。");
        }
        students.add(student);
    }

    public void updateStudent(int index, Student student) {
        if (index < 0 || index >= students.size()) {
            throw new IllegalArgumentException("请选择要修改的学生。");
        }
        Student existing = findById(student.getId());
        if (existing != null && existing != students.get(index)) {
            throw new IllegalArgumentException("学号已存在。");
        }
        students.set(index, student);
    }

    public void removeStudent(int index) {
        if (index < 0 || index >= students.size()) {
            throw new IllegalArgumentException("请选择要删除的学生。");
        }
        students.remove(index);
    }

    public Student findById(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public double averageScore() {
        if (students.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (Student student : students) {
            total += student.getScore();
        }
        return total * 1.0 / students.size();
    }
}
