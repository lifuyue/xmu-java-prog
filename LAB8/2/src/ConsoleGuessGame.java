import java.util.Scanner;

/**
 * 控制台版猜数字：把抽象输入输出方法映射到 Scanner 和 System.out。
 */
public class ConsoleGuessGame extends GuessGame {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    protected Integer readGuess(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if ("q".equalsIgnoreCase(line.trim())) {
                // 返回 null 表示本轮取消，父类 play 方法会结束当前轮。
                return null;
            }
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("请输入有效整数，或输入 q 退出本轮。");
            }
        }
    }

    @Override
    protected void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    protected boolean askContinue() {
        System.out.print("是否再玩一局？(y/n) ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }
}
