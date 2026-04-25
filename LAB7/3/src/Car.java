public class Car implements CarbonFootprint {
    private final String plateNumber;
    private final double annualKilometers;
    private final double fuelLitersPer100Km;

    public Car(String plateNumber, double annualKilometers, double fuelLitersPer100Km) {
        this.plateNumber = plateNumber;
        this.annualKilometers = annualKilometers;
        this.fuelLitersPer100Km = fuelLitersPer100Km;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public double getAnnualKilometers() {
        return annualKilometers;
    }

    public double getFuelLitersPer100Km() {
        return fuelLitersPer100Km;
    }

    @Override
    public double getCarbonFootprint() {
        double gasolineEmissionFactor = 2.31;
        double annualFuelLiters = annualKilometers * fuelLitersPer100Km / 100.0;
        return annualFuelLiters * gasolineEmissionFactor;
    }

    @Override
    public String toString() {
        return String.format("Car{plate='%s', distance=%.1f km, fuel=%.1f L/100km}",
                plateNumber, annualKilometers, fuelLitersPer100Km);
    }
}

