import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class LetterCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一段英文文本:");
        String text = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<Character, Integer> counts = new TreeMap<>();
        for (char ch : text.toLowerCase().toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                counts.put(ch, counts.getOrDefault(ch, 0) + 1);
            }
        }

        System.out.println("字母出现次数:");
        for (char ch = 'a'; ch <= 'z'; ch++) {
            int count = counts.getOrDefault(ch, 0);
            if (count > 0) {
                System.out.printf("%c: %d%n", ch, count);
            }
        }
    }
}

