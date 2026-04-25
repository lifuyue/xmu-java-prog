import java.util.Scanner;

public class ConsoleGuessGame extends GuessGame {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    protected Integer readGuess(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if ("q".equalsIgnoreCase(line.trim())) {
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
