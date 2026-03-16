import java.util.ArrayList;
import java.util.List;

/**
 * 素数实验程序入口。
 */
public class PrimeExperiment {
    private static final int MAX_NUMBER = 10000;
    private static final int NUMBERS_PER_LINE = 10;

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

    private static void printStats(String title, PrimeRunStats stats) {
        System.out.println(title);
        System.out.println("找到的素数个数：" + stats.getPrimeCount());
        System.out.println("总试除次数：" + stats.getTotalChecks());
        System.out.println();
    }
}
