/**
 * 课程父类，保存通用课程信息。
 */
public class Course {
    private final String courseId;
    private final String courseName;
    private final int credits;

    public Course(String courseId, String courseName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public String getCourseType() {
        return "课程";
    }
}
