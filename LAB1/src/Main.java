import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        // 输入方式1：使用 BufferedReader 读取姓名和一句话
        System.out.println("请输入你的姓名：");
        String name = reader.readLine();

        System.out.println("请输入你的年龄：");
        String ageText = reader.readLine();

        System.out.println("请输入一句你想说的话：");
        String message = reader.readLine();

        // 输入方式2：使用 Scanner 解析年龄文本
        Scanner scanner = new Scanner(ageText);
        int age = scanner.nextInt();

        // 输出方式1：使用 println 逐行输出
        System.out.println("欢迎你，" + name + "！");
        System.out.println("你刚才输入的话是：" + message);

        // 输出方式2：使用 printf 格式化输出
        System.out.printf("格式化输出：姓名=%s，年龄=%d岁。%n", name, age);

        scanner.close();
    }
}
