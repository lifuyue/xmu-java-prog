/**
 * 学生类。
 */
public class Student {
    private final String studentId;
    private final String name;
    private final String className;
    private final String phone;

    /**
     * 三参数构造函数。
     * <p>
     * 当电话未填写时，调用这个重载构造函数，
     * 程序会自动把电话保存为空字符串。
     *
     * @param studentId 学号
     * @param name 姓名
     * @param className 班级
     */
    public Student(String studentId, String name, String className) {
        this(studentId, name, className, "");
    }

    /**
     * 四参数构造函数。
     * <p>
     * 用于创建一个完整的学生对象。
     * 如果电话传入为 null，会被统一处理成空字符串，避免后续空指针问题。
     *
     * @param studentId 学号
     * @param name 姓名
     * @param className 班级
     * @param phone 电话，可为空
     */
    public Student(String studentId, String name, String className, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.className = className;
        this.phone = phone == null ? "" : phone.trim();
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
     * 返回学生姓名。
     *
     * @return 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 返回学生班级。
     *
     * @return 班级
     */
    public String getClassName() {
        return className;
    }

    /**
     * 返回学生电话。
     * <p>
     * 如果用户没有填写电话，这里会返回空字符串。
     *
     * @return 电话
     */
    public String getPhone() {
        return phone;
    }
}
