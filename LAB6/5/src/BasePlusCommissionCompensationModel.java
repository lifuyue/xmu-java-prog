public class BasePlusCommissionCompensationModel implements CompensationModel {
    private final double grossSales;
    private final double commissionRate;
    private final double baseSalary;

    public BasePlusCommissionCompensationModel(
            double grossSales, double commissionRate, double baseSalary) {
        if (grossSales < 0.0) {
            throw new IllegalArgumentException("grossSales 不能小于 0。");
        }
        if (commissionRate <= 0.0 || commissionRate >= 1.0) {
            throw new IllegalArgumentException("commissionRate 必须大于 0 且小于 1。");
        }
        if (baseSalary < 0.0) {
            throw new IllegalArgumentException("baseSalary 不能小于 0。");
        }
        this.grossSales = grossSales;
        this.commissionRate = commissionRate;
        this.baseSalary = baseSalary;
    }

    @Override
    public double earnings() {
        return baseSalary + grossSales * commissionRate;
    }

    @Override
    public String toString() {
        return String.format(
                "BasePlusCommissionCompensationModel[grossSales=%.2f, commissionRate=%.2f, baseSalary=%.2f]",
                grossSales,
                commissionRate,
                baseSalary);
    }
}
