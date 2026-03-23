/**
 * 本科生。
 */
public class UndergraduateStudent extends Student {
    public UndergraduateStudent(String studentId, String name, String className) {
        super(studentId, name, className);
    }

    @Override
    public String getStudentType() {
        return "本科生";
    }
}
