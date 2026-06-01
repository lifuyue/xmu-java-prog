import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * 第 6 题：只统计英文字母，TreeMap 让结果按字母自然顺序保存。
 */
public class LetterCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一段英文文本:");
        String text = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<Character, Integer> counts = new TreeMap<>();
        for (char ch : text.toLowerCase().toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                // 非英文字母直接跳过，避免标点和空格影响统计结果。
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
