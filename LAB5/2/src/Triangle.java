public class Triangle extends Graph {
    private final int sideA;
    private final int sideB;
    private final int sideC;

    public Triangle(int sideA, int sideB, int sideC) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    @Override
    public double getArea() {
        double halfPerimeter = (sideA + sideB + sideC) / 2.0;
        return Math.sqrt(halfPerimeter
                * (halfPerimeter - sideA)
                * (halfPerimeter - sideB)
                * (halfPerimeter - sideC));
    }
}
