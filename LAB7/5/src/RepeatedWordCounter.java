import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 第 5 题：把句子拆成单词，用 Map 统计每个单词出现次数。
 */
public class RepeatedWordCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个英文句子:");
        String sentence = scanner.hasNextLine() ? scanner.nextLine() : "";

        String[] words = sentence.toLowerCase().split("[^a-z0-9]+");
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                // getOrDefault 让第一次出现的单词从 0 开始累加。
                counts.put(word, counts.getOrDefault(word, 0) + 1);
            }
        }

        int repeatedWordKinds = 0;
        int repeatedOccurrences = 0;
        System.out.println("重复单词明细:");
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            int count = entry.getValue();
            if (count > 1) {
                repeatedWordKinds++;
                repeatedOccurrences += count - 1;
                System.out.printf("%s: %d 次%n", entry.getKey(), count);
            }
        }

        System.out.println("不同重复单词数: " + repeatedWordKinds);
        System.out.println("重复出现的额外次数: " + repeatedOccurrences);
    }
}
