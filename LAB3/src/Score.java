/**
 * 成绩类。
 */
public class Score {
    private final String courseId;
    private final String studentId;
    private final int score;

    public Score(String courseId, String studentId, int score) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.score = score;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getScore() {
        return score;
    }
}
