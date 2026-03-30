public class Test {
    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle();
        Motor motor = new Motor();
        Ship ship = new Ship();
        Aeroplane aeroplane = new Aeroplane();
        Bus bus = new Bus();
        Car car = new Car();

        vehicle.run();
        motor.run();
        ship.run();
        aeroplane.run();
        bus.run();
        car.run();
    }
}
