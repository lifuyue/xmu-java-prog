/**
 * 研究生。
 */
public class GraduateStudent extends Student {
    private final String advisor;

    public GraduateStudent(String studentId, String name, String className, String advisor) {
        super(studentId, name, className);
        this.advisor = advisor;
    }

    public String getAdvisor() {
        return advisor;
    }

    @Override
    public String getStudentType() {
        return "研究生";
    }
}
