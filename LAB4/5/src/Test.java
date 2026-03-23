/**
 * 第 5 题测试类。
 */
public class Test {
    public static void main(String[] args) {
        for (TrafficLight light : TrafficLight.values()) {
            System.out.println(
                    light.name()
                            + " -> RGB("
                            + light.getRed()
                            + ", "
                            + light.getGreen()
                            + ", "
                            + light.getBlue()
                            + ")"
            );
        }
    }
}
