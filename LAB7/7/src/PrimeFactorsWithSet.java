import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 第 7 题：先判断质数，再对合数做质因数分解并用 Set 去重。
 */
public class PrimeFactorsWithSet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个大于 1 的整数:");
        if (!scanner.hasNextInt()) {
            System.out.println("输入不是整数。");
            return;
        }

        int number = scanner.nextInt();
        if (number <= 1) {
            System.out.println("请输入大于 1 的整数。");
            return;
        }

        if (isPrime(number)) {
            System.out.println(number + " 是质数。质数的因子只有 1 和它自身。");
            return;
        }

        Set<Integer> uniquePrimeFactors = new LinkedHashSet<>();
        String factorization = buildFactorization(number, uniquePrimeFactors);

        System.out.println(number + " 不是质数。");
        System.out.println("质因数分解: " + factorization);
        System.out.println("使用 Set 去重后的质因子:");
        for (Integer factor : uniquePrimeFactors) {
            System.out.println(factor);
        }
    }

    private static boolean isPrime(int number) {
        if (number == 2) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }
        for (int divisor = 3; divisor * divisor <= number; divisor += 2) {
            if (number % divisor == 0) {
                return false;
            }
        }
        return true;
    }

    private static String buildFactorization(int number, Set<Integer> uniquePrimeFactors) {
        StringBuilder builder = new StringBuilder();
        int remaining = number;

        for (int divisor = 2; divisor * divisor <= remaining; divisor++) {
            // 同一个因子可能出现多次，例如 12 = 2 * 2 * 3，所以要用 while 反复除。
            while (remaining % divisor == 0) {
                appendFactor(builder, divisor);
                uniquePrimeFactors.add(divisor);
                remaining /= divisor;
            }
        }

        if (remaining > 1) {
            appendFactor(builder, remaining);
            uniquePrimeFactors.add(remaining);
        }

        return builder.toString();
    }

    private static void appendFactor(StringBuilder builder, int factor) {
        if (builder.length() > 0) {
            builder.append(" * ");
        }
        builder.append(factor);
    }
}
