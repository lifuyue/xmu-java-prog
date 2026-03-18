import java.security.SecureRandom;
import java.util.Scanner;

/**
 * 计算机辅助教学程序。
 */
public class MultiplicationCai {
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 程序入口。
     * <p>
     * 运行流程如下：
     * - 先创建输入对象；
     * - 生成第一道题并显示；
     * - 反复读取用户答案；
     * - 如果答对就换下一题，如果答错就继续回答当前题。
     *
     * @param args 命令行参数，本实验中不会使用
     */
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

    /**
     * 随机生成一道新的乘法题。
     * <p>
     * 两个乘数都限定在 1 到 9 之间，满足题目对“一位正整数”的要求。
     *
     * @return 随机生成的乘法题对象
     */
    private static MultiplicationQuestion generateQuestion() {
        int first = RANDOM.nextInt(9) + 1;
        int second = RANDOM.nextInt(9) + 1;
        return new MultiplicationQuestion(first, second);
    }

    /**
     * 把题目打印到控制台。
     *
     * @param question 要展示的乘法题
     */
    private static void askQuestion(MultiplicationQuestion question) {
        System.out.printf("How much is %d times %d?%n", question.getFirst(), question.getSecond());
    }

    /**
     * 从控制台读取学生输入的答案。
     * <p>
     * 如果用户输入的不是整数，就提示重新输入，
     * 直到成功读到一个合法整数为止。
     *
     * @param scanner 用来读取控制台输入的扫描器
     * @return 用户输入的整数答案
     */
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

    /**
     * 判断学生答案是否正确。
     *
     * @param question 当前乘法题
     * @param answer 学生输入的答案
     * @return 如果答案正确则返回 true，否则返回 false
     */
    private static boolean isCorrectAnswer(MultiplicationQuestion question, int answer) {
        return answer == question.getAnswer();
    }

    /**
     * 随机输出一条答对后的鼓励语。
     * <p>
     * 这里使用 switch 语句在 4 条正确评语中随机选择 1 条。
     */
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

    /**
     * 随机输出一条答错后的提示语。
     * <p>
     * 这里使用 switch 语句在 4 条错误评语中随机选择 1 条。
     */
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

    /**
     * 表示一道乘法题的内部数据对象。
     * <p>
     * 它只保存两个乘数，并提供读取乘数和正确答案的方法。
     */
    private static class MultiplicationQuestion {
        private final int first;
        private final int second;

        /**
         * 构造一道乘法题。
         *
         * @param first 第一个乘数
         * @param second 第二个乘数
         */
        private MultiplicationQuestion(int first, int second) {
            this.first = first;
            this.second = second;
        }

        /**
         * 返回第一个乘数。
         *
         * @return 第一个乘数
         */
        private int getFirst() {
            return first;
        }

        /**
         * 返回第二个乘数。
         *
         * @return 第二个乘数
         */
        private int getSecond() {
            return second;
        }

        /**
         * 计算并返回这道题的正确答案。
         *
         * @return 两个乘数相乘后的结果
         */
        private int getAnswer() {
            return first * second;
        }
    }
}
