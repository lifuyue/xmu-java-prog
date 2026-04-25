import java.util.ArrayList;
import java.util.List;

public class CarbonFootprintDemo {
    public static void main(String[] args) {
        List<CarbonFootprint> items = new ArrayList<>();
        items.add(new Building("Software College Lab", 12000, 300));
        items.add(new Car("Min-A12345", 15000, 7.2));
        items.add(new Bicycle("Giant", 120, 8));

        System.out.println("碳足迹估算结果（单位: kg CO2e/year）");
        System.out.println("计算系数采用近似值，仅用于实验演示。");
        for (CarbonFootprint item : items) {
            System.out.printf("%s%n", item);
            System.out.printf("类别: %s, 年碳足迹: %.2f%n%n",
                    item.getClass().getSimpleName(), item.getCarbonFootprint());
        }
    }
}

