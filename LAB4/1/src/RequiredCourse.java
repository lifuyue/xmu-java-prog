/**
 * 必修课。
 */
public class RequiredCourse extends Course {
    public RequiredCourse(String courseId, String courseName, int credits) {
        super(courseId, courseName, credits);
    }

    @Override
    public String getCourseType() {
        return "必修";
    }
}
