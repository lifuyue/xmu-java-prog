import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 第 3 题主程序。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        List<Graph> graphs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine().trim();
            while (line.isEmpty()) {
                line = scanner.nextLine().trim();
            }

            String[] parts = line.split("\\s+");
            if (parts.length == 2) {
                int length = Integer.parseInt(parts[0]);
                int width = Integer.parseInt(parts[1]);
                graphs.add(new Rectangle(length, width));
            } else if (parts.length == 3) {
                int sideA = Integer.parseInt(parts[0]);
                int sideB = Integer.parseInt(parts[1]);
                int sideC = Integer.parseInt(parts[2]);
                graphs.add(new Triangle(sideA, sideB, sideC));
            }
        }

        for (Graph graph : graphs) {
            System.out.println((int) graph.getArea());
        }
        scanner.close();
    }
}
