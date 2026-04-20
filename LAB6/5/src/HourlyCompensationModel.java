public class HourlyCompensationModel implements CompensationModel {
    private final double wage;
    private final double hours;

    public HourlyCompensationModel(double wage, double hours) {
        if (wage < 0.0) {
            throw new IllegalArgumentException("wage 不能小于 0。");
        }
        if (hours < 0.0 || hours > 168.0) {
            throw new IllegalArgumentException("hours 必须在 0 到 168 之间。");
        }
        this.wage = wage;
        this.hours = hours;
    }

    @Override
    public double earnings() {
        if (hours <= 40.0) {
            return wage * hours;
        }
        return 40.0 * wage + (hours - 40.0) * wage * 1.5;
    }

    @Override
    public String toString() {
        return String.format("HourlyCompensationModel[wage=%.2f, hours=%.2f]", wage, hours);
    }
}
