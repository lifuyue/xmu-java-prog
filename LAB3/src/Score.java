/**
 * 成绩类。
 */
public class Score {
    private final String courseId;
    private final String studentId;
    private final int score;

    /**
     * 构造一条成绩记录。
     *
     * @param courseId 课程编号
     * @param studentId 学生学号
     * @param score 成绩分数
     */
    public Score(String courseId, String studentId, int score) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.score = score;
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
     * 返回学生学号。
     *
     * @return 学号
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * 返回成绩分数。
     *
     * @return 分数
     */
    public int getScore() {
        return score;
    }
}
