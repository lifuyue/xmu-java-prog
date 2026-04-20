public class CommissionCompensationModel implements CompensationModel {
    private final double grossSales;
    private final double commissionRate;

    public CommissionCompensationModel(double grossSales, double commissionRate) {
        if (grossSales < 0.0) {
            throw new IllegalArgumentException("grossSales 不能小于 0。");
        }
        if (commissionRate <= 0.0 || commissionRate >= 1.0) {
            throw new IllegalArgumentException("commissionRate 必须大于 0 且小于 1。");
        }
        this.grossSales = grossSales;
        this.commissionRate = commissionRate;
    }

    @Override
    public double earnings() {
        return grossSales * commissionRate;
    }

    @Override
    public String toString() {
        return String.format(
                "CommissionCompensationModel[grossSales=%.2f, commissionRate=%.2f]",
                grossSales,
                commissionRate);
    }
}
