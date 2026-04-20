public class SalariedCompensationModel implements CompensationModel {
    private final double weeklySalary;

    public SalariedCompensationModel(double weeklySalary) {
        if (weeklySalary < 0.0) {
            throw new IllegalArgumentException("weeklySalary 不能小于 0。");
        }
        this.weeklySalary = weeklySalary;
    }

    @Override
    public double earnings() {
        return weeklySalary;
    }

    @Override
    public String toString() {
        return String.format("SalariedCompensationModel[weeklySalary=%.2f]", weeklySalary);
    }
}
