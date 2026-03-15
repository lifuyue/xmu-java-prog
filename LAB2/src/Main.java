import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 程序主入口。
 * <p>
 * 本类负责把整个实验串起来，包括：
 * - 显示菜单；
 * - 读取用户输入；
 * - 执行单次判奖；
 * - 执行多人模式；
 * - 输出奖池变化和最终统计。
 * <p>
 * 可以把它理解为“控制台版界面层”，它主要负责流程组织，
 * 具体的掷骰、判奖、库存处理，分别交给其他类完成。
 */
public class Main {
    /**
     * 全局复用一个判奖器。
     */
    private static final BoBingEvaluator EVALUATOR = new BoBingEvaluator();

    /**
     * 全局复用一个掷骰器。
     */
    private static final DiceRoller DICE_ROLLER = new DiceRoller();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 主循环：程序会一直显示菜单，直到用户主动选择“退出”。
        while (true) {
            printMenu();
            int choice = readInt(scanner, "请输入菜单编号：");

            if (choice == 1) {
                runSingleMode();
            } else if (choice == 2) {
                runMultiPlayerMode(scanner);
            } else if (choice == 3) {
                System.out.println("程序结束。");
                scanner.close();
                return;
            } else {
                System.out.println("菜单编号无效，请重新输入。");
            }
        }
    }

    /**
     * 打印主菜单。
     */
    private static void printMenu() {
        System.out.println("========== 博饼程序 ==========");
        System.out.println("1. 单次判奖");
        System.out.println("2. 多人模式");
        System.out.println("3. 退出");
    }

    /**
     * 执行基本题中的“单次判奖”。
     * 流程很直接：
     * 1. 随机掷 6 个骰子；
     * 2. 调用判奖器得出结果；
     * 3. 打印骰子点数和判奖结果。
     */
    private static void runSingleMode() {
        int[] dice = DICE_ROLLER.rollSixDice();
        RollResult result = EVALUATOR.evaluate(dice);
        System.out.println("本次骰子点数：" + formatDice(dice));
        System.out.println("判奖结果：" + result.getResultDescription());
        System.out.println();
    }

    /**
     * 执行扩展题中的“多人模式”。
     * <p>
     * 整体流程：
     * 1. 读取玩家人数；
     * 2. 创建每位玩家的记录；
     * 3. 初始化奖池；
     * 4. 所有玩家按轮次依次掷骰；
     * 5. 每次命中后都尝试从奖池中领取奖项；
     * 6. 当奖池全部发完时结束游戏；
     * 7. 输出最终统计结果。
     */
    private static void runMultiPlayerMode(Scanner scanner) {
        int playerCount = readPlayerCount(scanner);
        List<PlayerRecord> players = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            players.add(new PlayerRecord("玩家" + i));
        }

        PrizePool prizePool = new PrizePool();
        int round = 1;

        // 只要奖池还有任意奖项没发完，就继续进行下一轮。
        while (!prizePool.isEmpty()) {
            System.out.println("========== 第 " + round + " 轮 ==========");
            for (PlayerRecord player : players) {
                // 有可能在本轮中途刚好发完奖，所以每位玩家开始前都要再检查一次。
                if (prizePool.isEmpty()) {
                    break;
                }

                int[] dice = DICE_ROLLER.rollSixDice();
                RollResult result = EVALUATOR.evaluate(dice);
                AwardOutcome outcome = prizePool.claimAwards(result);
                player.addAwardOutcome(outcome);

                System.out.println(player.getName() + " 掷出的点数：" + formatDice(dice));
                System.out.println("命中结果：" + result.getResultDescription());
                printOutcome(outcome, result);
                printPrizePool(prizePool);
                System.out.println();
            }
            round++;
        }

        printFinalSummary(players, prizePool);
    }

    /**
     * 读取玩家人数，并且限制范围必须在 6~10。
     */
    private static int readPlayerCount(Scanner scanner) {
        while (true) {
            int playerCount = readInt(scanner, "请输入玩家数（6~10）：");
            if (playerCount >= 6 && playerCount <= 10) {
                return playerCount;
            }
            System.out.println("玩家数必须在 6 到 10 之间，请重新输入。");
        }
    }

    /**
     * 安全读取一个整数。
     * <p>
     * 这里没有直接使用 scanner.nextInt()，而是先读取整行字符串再转整数，
     * 这样更容易处理非法输入，也不会留下换行符影响后续输入。
     */
    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException exception) {
                System.out.println("输入格式错误，请输入整数。");
            }
        }
    }

    /**
     * 打印“本轮最终实际发奖情况”。
     * <p>
     * 这里需要同时结合两个对象来看：
     * - {@code result} 告诉我们规则上命中了什么；
     * - {@code outcome} 告诉我们库存上最终发出了什么。
     */
    private static void printOutcome(AwardOutcome outcome, RollResult result) {
        if (!result.hasPrize()) {
            System.out.println("本轮无奖。");
            return;
        }

        if (outcome.hasGrantedPrize()) {
            System.out.println("实际获得：" + String.join("、", outcome.getGrantedPrizeLabels()));
        } else {
            System.out.println("本轮未获得实际奖项。");
        }

        if (outcome.hasExhaustedPrize()) {
            for (String label : outcome.getExhaustedPrizeLabels()) {
                System.out.println(label + "：命中但奖项已发完，不得奖");
            }
        }
    }

    /**
     * 打印当前奖池库存，便于观察多人模式下奖项如何逐步减少。
     */
    private static void printPrizePool(PrizePool prizePool) {
        System.out.println("当前剩余库存："
                + " 状元=" + prizePool.getRemainingCount(PrizeType.ZHUANGYUAN)
                + " 对堂=" + prizePool.getRemainingCount(PrizeType.DUITANG)
                + " 三红=" + prizePool.getRemainingCount(PrizeType.SANHONG)
                + " 四进=" + prizePool.getRemainingCount(PrizeType.SIJIN)
                + " 二举=" + prizePool.getRemainingCount(PrizeType.ERJU)
                + " 一秀=" + prizePool.getRemainingCount(PrizeType.YIXIU));
    }

    /**
     * 游戏结束后输出总表。
     * 包括：
     * - 每位玩家拿到了哪些奖；
     * - 每类奖项一共发了多少，还剩多少。
     */
    private static void printFinalSummary(List<PlayerRecord> players, PrizePool prizePool) {
        System.out.println("========== 最终结果 ==========");
        for (PlayerRecord player : players) {
            System.out.println(player.getName() + " 获得的奖项明细：");
            if (player.getAwardDetails().isEmpty()) {
                System.out.println("无");
            } else {
                System.out.println(String.join("、", player.getAwardDetails()));
            }
        }

        System.out.println("========== 各类奖项发放统计 ==========");
        printPrizeStatistics(prizePool, PrizeType.ZHUANGYUAN);
        printPrizeStatistics(prizePool, PrizeType.DUITANG);
        printPrizeStatistics(prizePool, PrizeType.SANHONG);
        printPrizeStatistics(prizePool, PrizeType.SIJIN);
        printPrizeStatistics(prizePool, PrizeType.ERJU);
        printPrizeStatistics(prizePool, PrizeType.YIXIU);
    }

    /**
     * 打印某一类奖项的发放统计。
     */
    private static void printPrizeStatistics(PrizePool prizePool, PrizeType prizeType) {
        System.out.println(prizeType.getDisplayName()
                + "：已发 " + prizePool.getAwardedCount(prizeType)
                + "，剩余 " + prizePool.getRemainingCount(prizeType));
    }

    /**
     * 将骰子数组格式化成类似 [1, 2, 3, 4, 5, 6] 的字符串。
     */
    private static String formatDice(int[] dice) {
        return Arrays.toString(dice);
    }
}
