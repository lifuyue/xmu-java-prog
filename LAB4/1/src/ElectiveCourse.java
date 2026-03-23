/**
 * 选修课。
 */
public class ElectiveCourse extends Course {
    public ElectiveCourse(String courseId, String courseName, int credits) {
        super(courseId, courseName, credits);
    }

    @Override
    public String getCourseType() {
        return "选修";
    }
}
