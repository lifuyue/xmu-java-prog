import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 第 4 题：使用 LinkedHashSet 删除重复姓名，并保留第一次输入的顺序。
 */
public class UniqueNames {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<String> names = new LinkedHashSet<>();

        System.out.println("请输入名字，每行一个；输入空行结束。");
        while (scanner.hasNextLine()) {
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                break;
            }
            // Set 的 add 会自动忽略已经存在的名字。
            names.add(name);
        }

        System.out.println("删除重复后的名字:");
        for (String name : names) {
            System.out.println(name);
        }
    }
}
