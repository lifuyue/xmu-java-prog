import java.util.ArrayList;
import java.util.List;

/**
 * 素数实验程序入口。
 */
public class PrimeExperiment {
    private static final int MAX_NUMBER = 10000;
    private static final int NUMBERS_PER_LINE = 10;

    /**
     * 程序入口。
     * <p>
     * 本方法会分别运行两轮实验：
     * - 第一轮使用“最多测试到 n/2”的方法；
     * - 第二轮使用“最多测试到 sqrt(n)”的方法。
     * 最后把素数列表和两种算法的统计结果一起输出。
     *
     * @param args 命令行参数，本实验中不会使用
     */
    public static void main(String[] args) {
        PrimeRunStats halfStats = runExperiment(false);
        PrimeRunStats sqrtStats = runExperiment(true);

        System.out.println("小于 10000 的全部素数如下：");
        printPrimes(sqrtStats.getPrimes());
        System.out.println();

        System.out.println("共测试 " + sqrtStats.getTestedCount() + " 个整数。");
        printStats("方法一：最多测试 n/2 次", halfStats);
        printStats("方法二：最多测试 sqrt(n) 次", sqrtStats);
        System.out.println("两种方法找到的素数个数一致："
                + (halfStats.getPrimeCount() == sqrtStats.getPrimeCount() ? "是" : "否"));
    }

    /**
     * 执行一轮完整的素数实验。
     * <p>
     * 实验流程：
     * - 依次测试 2 到 9999；
     * - 根据参数决定使用哪种素数判断方法；
     * - 记录测试个数、素数个数、总试除次数和素数列表；
     * - 最终封装成 {@code PrimeRunStats} 返回。
     *
     * @param useSqrtMethod 为 true 表示使用平方根策略，为 false 表示使用 n/2 策略
     * @return 当前策略对应的实验统计结果
     */
    private static PrimeRunStats runExperiment(boolean useSqrtMethod) {
        List<Integer> primes = new ArrayList<>();
        int testedCount = 0;
        int totalChecks = 0;

        for (int number = 2; number < MAX_NUMBER; number++) {
            testedCount++;

            boolean isPrime = useSqrtMethod
                    ? PrimeUtils.isPrimeBySqrt(number)
                    : PrimeUtils.isPrimeByHalf(number);

            totalChecks += useSqrtMethod
                    ? PrimeUtils.countChecksBySqrt(number)
                    : PrimeUtils.countChecksByHalf(number);

            if (isPrime) {
                primes.add(number);
            }
        }

        return new PrimeRunStats(testedCount, primes.size(), totalChecks, primes);
    }

    /**
     * 按固定排版输出全部素数。
     * <p>
     * 这里每行打印 10 个数字，目的是让控制台结果更整齐、更方便阅读。
     *
     * @param primes 要输出的素数列表
     */
    private static void printPrimes(List<Integer> primes) {
        for (int i = 0; i < primes.size(); i++) {
            System.out.printf("%-6d", primes.get(i));
            if ((i + 1) % NUMBERS_PER_LINE == 0) {
                System.out.println();
            }
        }

        if (primes.size() % NUMBERS_PER_LINE != 0) {
            System.out.println();
        }
    }

    /**
     * 输出某一种策略的统计信息。
     * <p>
     * 主要展示两项内容：
     * - 找到的素数个数；
     * - 总试除次数。
     *
     * @param title 当前策略的标题说明
     * @param stats 当前策略对应的统计结果
     */
    private static void printStats(String title, PrimeRunStats stats) {
        System.out.println(title);
        System.out.println("找到的素数个数：" + stats.getPrimeCount());
        System.out.println("总试除次数：" + stats.getTotalChecks());
        System.out.println();
    }
}
