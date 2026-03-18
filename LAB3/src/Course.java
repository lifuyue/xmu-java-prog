/**
 * 课程类。
 */
public class Course {
    private final String courseId;
    private final String courseName;

    /**
     * 构造一门课程对象。
     *
     * @param courseId 课程编号
     * @param courseName 课程名称
     */
    public Course(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    /**
     * 返回课程编号。
     *
     * @return 课程编号
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * 返回课程名称。
     *
     * @return 课程名称
     */
    public String getCourseName() {
        return courseName;
    }
}
