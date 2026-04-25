public class Building implements CarbonFootprint {
    private final String name;
    private final double annualElectricityKwh;
    private final double annualNaturalGasM3;

    public Building(String name, double annualElectricityKwh, double annualNaturalGasM3) {
        this.name = name;
        this.annualElectricityKwh = annualElectricityKwh;
        this.annualNaturalGasM3 = annualNaturalGasM3;
    }

    public String getName() {
        return name;
    }

    public double getAnnualElectricityKwh() {
        return annualElectricityKwh;
    }

    public double getAnnualNaturalGasM3() {
        return annualNaturalGasM3;
    }

    @Override
    public double getCarbonFootprint() {
        double electricityEmissionFactor = 0.581;
        double naturalGasEmissionFactor = 2.162;
        return annualElectricityKwh * electricityEmissionFactor
                + annualNaturalGasM3 * naturalGasEmissionFactor;
    }

    @Override
    public String toString() {
        return String.format("Building{name='%s', electricity=%.1f kWh, naturalGas=%.1f m3}",
                name, annualElectricityKwh, annualNaturalGasM3);
    }
}

