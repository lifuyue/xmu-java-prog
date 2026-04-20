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
        this.compensationModel = compensationModel;
    }

    public double earnings() {
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
