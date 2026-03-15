import java.util.Arrays;

/**
 * 固定测试样例。
 * <p>
 * 该类不是游戏主程序，而是一个“验证程序逻辑是否正确”的辅助入口。
 * 它做了两件事：
 * 1. 使用固定骰子点数测试所有关键规则；
 * 2. 演示奖池在库存不足时的行为。
 * <p>
 * 适合在提交实验前运行，用来证明程序的主要逻辑已经覆盖并通过验证。
 */
public class TestCases {
    private static final BoBingEvaluator EVALUATOR = new BoBingEvaluator();

    public static void main(String[] args) {
        runRuleTests();
        runPrizePoolDemo();
    }

    /**
     * 逐条验证常见奖项规则。
     * 每个测试都直接指定 6 个骰子点数，因此结果是确定的。
     */
    private static void runRuleTests() {
        System.out.println("========== 固定规则测试 ==========");
        testCase("对堂", new int[] {1, 2, 3, 4, 5, 6}, "对堂");
        testCase("三红", new int[] {4, 4, 4, 1, 2, 5}, "三红");
        testCase("四进带一秀", new int[] {2, 2, 2, 2, 4, 6}, "四进 + 一秀");
        testCase("四进带二举", new int[] {6, 6, 6, 6, 4, 4}, "四进 + 二举");
        testCase("四红", new int[] {4, 4, 4, 4, 2, 3}, "状元档：四红");
        testCase("五子", new int[] {2, 2, 2, 2, 2, 1}, "状元档：五子");
        testCase("五王", new int[] {4, 4, 4, 4, 4, 2}, "状元档：五王");
        testCase("状元插金花", new int[] {4, 4, 4, 4, 1, 1}, "状元档：状元插金花");
        testCase("六勃红", new int[] {4, 4, 4, 4, 4, 4}, "状元档：六勃红");
        testCase("六勃黑", new int[] {5, 5, 5, 5, 5, 5}, "状元档：六勃黑");
        testCase("遍地锦", new int[] {1, 1, 1, 1, 1, 1}, "状元档：遍地锦");
        testCase("无奖", new int[] {1, 1, 2, 3, 5, 6}, "无奖");
        System.out.println();
    }

    /**
     * 执行单个测试用例，并打印“期望值”和“实际值”。
     */
    private static void testCase(String caseName, int[] dice, String expected) {
        RollResult result = EVALUATOR.evaluate(dice);
        String actual = result.getResultDescription();
        boolean passed = expected.equals(actual);

        System.out.println("测试名称：" + caseName);
        System.out.println("骰子点数：" + Arrays.toString(dice));
        System.out.println("期望结果：" + expected);
        System.out.println("实际结果：" + actual);
        System.out.println("测试结论：" + (passed ? "通过" : "失败"));
        System.out.println();
    }

    /**
     * 演示奖池库存逻辑。
     * <p>
     * 这里先命中一次“四进 + 二举”，确认组合奖可以正常领取；
     * 再故意把“二举”库存消耗到接近空；
     * 最后再次命中相同结果，观察“库存不足时部分奖项无法发放”的现象。
     */
    private static void runPrizePoolDemo() {
        System.out.println("========== 奖池库存演示 ==========");
        PrizePool prizePool = new PrizePool();

        RollResult comboResult = EVALUATOR.evaluate(new int[] {2, 2, 2, 2, 4, 4});
        AwardOutcome firstOutcome = prizePool.claimAwards(comboResult);
        printOutcome("首次命中四进带二举", firstOutcome);

        for (int i = 0; i < 15; i++) {
            prizePool.claimAwards(EVALUATOR.evaluate(new int[] {4, 4, 1, 2, 3, 5}));
        }

        AwardOutcome exhaustedOutcome = prizePool.claimAwards(comboResult);
        printOutcome("二举库存耗尽后再次命中四进带二举", exhaustedOutcome);
    }

    /**
     * 统一打印一次库存发奖结果。
     */
    private static void printOutcome(String title, AwardOutcome outcome) {
        System.out.println(title);
        if (outcome.hasGrantedPrize()) {
            System.out.println("实际获得：" + String.join("、", outcome.getGrantedPrizeLabels()));
        } else {
            System.out.println("实际获得：无");
        }

        if (outcome.hasExhaustedPrize()) {
            for (String label : outcome.getExhaustedPrizeLabels()) {
                System.out.println(label + "：命中但奖项已发完，不得奖");
            }
        }
        System.out.println();
    }
}
