import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 学生父类，保存通用信息和已选课程。
 */
public class Student {
    private final String studentId;
    private final String name;
    private final String className;
    private final List<Course> selectedCourses = new ArrayList<>();

    public Student(String studentId, String name, String className) {
        this.studentId = studentId;
        this.name = name;
        this.className = className;
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

    public List<Course> getSelectedCourses() {
        return Collections.unmodifiableList(selectedCourses);
    }

    public boolean hasCourse(String courseId) {
        for (Course course : selectedCourses) {
            if (course.getCourseId().equals(courseId)) {
                return true;
            }
        }
        return false;
    }

    public void addCourse(Course course) {
        if (course == null || hasCourse(course.getCourseId())) {
            return;
        }
        selectedCourses.add(course);
    }

    public String getStudentType() {
        return "学生";
    }
}
