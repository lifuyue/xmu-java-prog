/**
 * 员工类只保存员工身份信息，并把收入计算委托给 CompensationModel。
 */
public class Employee {
    private final String firstName;
    private final String lastName;
    private final String socialSecurityNumber;
    private CompensationModel compensationModel;

    public Employee(
            String firstName,
            String lastName,
            String socialSecurityNumber,
            CompensationModel compensationModel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        setCompensationModel(compensationModel);
    }

    public void setCompensationModel(CompensationModel compensationModel) {
        if (compensationModel == null) {
            throw new IllegalArgumentException("compensationModel 不能为空。");
        }
        // 组合薪酬模型对象后，Employee 不需要知道具体是周薪、时薪还是提成。
        this.compensationModel = compensationModel;
    }

    public double earnings() {
        // 多态调用：运行时由当前模型对象决定具体收入公式。
        return compensationModel.earnings();
    }

    @Override
    public String toString() {
        return String.format(
                "Employee[firstName=%s, lastName=%s, ssn=%s, model=%s]",
                firstName,
                lastName,
                socialSecurityNumber,
                compensationModel);
    }
}
