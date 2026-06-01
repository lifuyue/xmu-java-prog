/**
 * 自行车碳足迹：把制造阶段排放平均分摊到预计使用年限中。
 */
public class Bicycle implements CarbonFootprint {
    private final String brand;
    private final double manufacturingEmissionKg;
    private final int expectedServiceYears;

    public Bicycle(String brand, double manufacturingEmissionKg, int expectedServiceYears) {
        this.brand = brand;
        this.manufacturingEmissionKg = manufacturingEmissionKg;
        this.expectedServiceYears = expectedServiceYears;
    }

    public String getBrand() {
        return brand;
    }

    public double getManufacturingEmissionKg() {
        return manufacturingEmissionKg;
    }

    public int getExpectedServiceYears() {
        return expectedServiceYears;
    }

    @Override
    public double getCarbonFootprint() {
        if (expectedServiceYears <= 0) {
            return manufacturingEmissionKg;
        }
        return manufacturingEmissionKg / expectedServiceYears;
    }

    @Override
    public String toString() {
        return String.format("Bicycle{brand='%s', manufacturing=%.1f kg, serviceYears=%d}",
                brand, manufacturingEmissionKg, expectedServiceYears);
    }
}
