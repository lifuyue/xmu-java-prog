import java.util.Random;

/**
 * 掷骰工具类。
 * <p>
 * 该类专门负责“生成随机骰子点数”，不参与任何判奖逻辑。
 * 这样做的好处是职责清晰：
 * - {@link DiceRoller} 只负责随机；
 * - {@link BoBingEvaluator} 只负责判奖。
 */
public class DiceRoller {
    /**
     * Java 标准库提供的随机数生成器。
     */
    private final Random random;

    /**
     * 默认构造方法。
     * 使用系统默认种子，适合正常运行游戏时使用。
     */
    public DiceRoller() {
        this(new Random());
    }

    /**
     * 允许从外部传入 Random。
     * 这样做便于测试：如果需要固定随机行为，可以传入一个固定种子的 Random。
     */
    public DiceRoller(Random random) {
        this.random = random;
    }

    /**
     * 生成 6 个骰子的点数。
     *
     * @return 长度为 6 的数组，每个元素都在 1~6 之间
     */
    public int[] rollSixDice() {
        int[] dice = new int[6];
        for (int i = 0; i < dice.length; i++) {
            // nextInt(6) 的结果范围是 0~5，因此要加 1 变成真正的骰子点数 1~6。
            dice[i] = random.nextInt(6) + 1;
        }
        return dice;
    }
}
