import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final BoBingEvaluator EVALUATOR = new BoBingEvaluator();
    private static final DiceRoller DICE_ROLLER = new DiceRoller();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

    private static void printMenu() {
        System.out.println("========== 博饼程序 ==========");
        System.out.println("1. 单次判奖");
        System.out.println("2. 多人模式");
        System.out.println("3. 退出");
    }

    private static void runSingleMode() {
        int[] dice = DICE_ROLLER.rollSixDice();
        RollResult result = EVALUATOR.evaluate(dice);
        System.out.println("本次骰子点数：" + formatDice(dice));
        System.out.println("判奖结果：" + result.getResultDescription());
        System.out.println();
    }

    private static void runMultiPlayerMode(Scanner scanner) {
        int playerCount = readPlayerCount(scanner);
        List<PlayerRecord> players = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            players.add(new PlayerRecord("玩家" + i));
        }

        PrizePool prizePool = new PrizePool();
        int round = 1;
        while (!prizePool.isEmpty()) {
            System.out.println("========== 第 " + round + " 轮 ==========");
            for (PlayerRecord player : players) {
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

    private static int readPlayerCount(Scanner scanner) {
        while (true) {
            int playerCount = readInt(scanner, "请输入玩家数（6~10）：");
            if (playerCount >= 6 && playerCount <= 10) {
                return playerCount;
            }
            System.out.println("玩家数必须在 6 到 10 之间，请重新输入。");
        }
    }

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

    private static void printPrizePool(PrizePool prizePool) {
        System.out.println("当前剩余库存："
                + " 状元=" + prizePool.getRemainingCount(PrizeType.ZHUANGYUAN)
                + " 对堂=" + prizePool.getRemainingCount(PrizeType.DUITANG)
                + " 三红=" + prizePool.getRemainingCount(PrizeType.SANHONG)
                + " 四进=" + prizePool.getRemainingCount(PrizeType.SIJIN)
                + " 二举=" + prizePool.getRemainingCount(PrizeType.ERJU)
                + " 一秀=" + prizePool.getRemainingCount(PrizeType.YIXIU));
    }

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

    private static void printPrizeStatistics(PrizePool prizePool, PrizeType prizeType) {
        System.out.println(prizeType.getDisplayName()
                + "：已发 " + prizePool.getAwardedCount(prizeType)
                + "，剩余 " + prizePool.getRemainingCount(prizeType));
    }

    private static String formatDice(int[] dice) {
        return Arrays.toString(dice);
    }
}
