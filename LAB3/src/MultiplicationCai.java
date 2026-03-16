import java.security.SecureRandom;
import java.util.Scanner;

/**
 * 计算机辅助教学程序。
 */
public class MultiplicationCai {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MultiplicationQuestion question = generateQuestion();
        askQuestion(question);

        while (true) {
            int answer = readAnswer(scanner);
            if (isCorrectAnswer(question, answer)) {
                printCorrectResponse();
                question = generateQuestion();
                askQuestion(question);
            } else {
                printIncorrectResponse();
                askQuestion(question);
            }
        }
    }

    private static MultiplicationQuestion generateQuestion() {
        int first = RANDOM.nextInt(9) + 1;
        int second = RANDOM.nextInt(9) + 1;
        return new MultiplicationQuestion(first, second);
    }

    private static void askQuestion(MultiplicationQuestion question) {
        System.out.printf("How much is %d times %d?%n", question.getFirst(), question.getSecond());
    }

    private static int readAnswer(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("请输入整数答案：");
            }
        }
    }

    private static boolean isCorrectAnswer(MultiplicationQuestion question, int answer) {
        return answer == question.getAnswer();
    }

    private static void printCorrectResponse() {
        switch (RANDOM.nextInt(4) + 1) {
            case 1:
                System.out.println("Very good!");
                break;
            case 2:
                System.out.println("Excellent!");
                break;
            case 3:
                System.out.println("Nice work!");
                break;
            default:
                System.out.println("Keep up the good work!");
                break;
        }
    }

    private static void printIncorrectResponse() {
        switch (RANDOM.nextInt(4) + 1) {
            case 1:
                System.out.println("No. Please try again.");
                break;
            case 2:
                System.out.println("Wrong. Try once more.");
                break;
            case 3:
                System.out.println("Don't give up!");
                break;
            default:
                System.out.println("No. Keep trying.");
                break;
        }
    }

    private static class MultiplicationQuestion {
        private final int first;
        private final int second;

        private MultiplicationQuestion(int first, int second) {
            this.first = first;
            this.second = second;
        }

        private int getFirst() {
            return first;
        }

        private int getSecond() {
            return second;
        }

        private int getAnswer() {
            return first * second;
        }
    }
}
