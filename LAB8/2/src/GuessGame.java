import java.util.Random;

/**
 * 抽象猜数字游戏：这里固定“生成答案、循环猜测、询问是否继续”的流程。
 */
public abstract class GuessGame {
    private final Random random;
    private final int min;
    private final int max;

    protected GuessGame() {
        this(1, 100, new Random());
    }

    protected GuessGame(int min, int max, Random random) {
        this.min = min;
        this.max = max;
        this.random = random;
    }

    public final void play() {
        boolean again;
        do {
            int answer = random.nextInt(max - min + 1) + min;
            int count = 0;
            showMessage("系统已经生成 " + min + " 到 " + max + " 之间的整数。");

            while (true) {
                // readGuess 由子类决定来自控制台还是对话框，父类只处理游戏规则。
                Integer guess = readGuess("请输入你的猜测：");
                if (guess == null) {
                    showMessage("本轮游戏已取消。");
                    break;
                }
                count++;
                if (guess < answer) {
                    showMessage("第 " + count + " 次：" + guess + " 太小了。");
                } else if (guess > answer) {
                    showMessage("第 " + count + " 次：" + guess + " 太大了。");
                } else {
                    showMessage("恭喜猜中！答案是 " + answer + "，共猜了 " + count + " 次。");
                    break;
                }
            }
            again = askContinue();
        } while (again);
    }

    protected abstract Integer readGuess(String prompt);

    protected abstract void showMessage(String message);

    protected abstract boolean askContinue();
}
