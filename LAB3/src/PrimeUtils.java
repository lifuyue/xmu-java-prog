/**
 * 素数判断工具类。
 */
public final class PrimeUtils {
    /**
     * 私有构造方法。
     * <p>
     * 工具类不需要被创建对象，所以把构造方法设为私有。
     */
    private PrimeUtils() {
    }

    /**
     * 使用“最多测试到 n/2”的方式判断一个整数是否为素数。
     * <p>
     * 判断思路：
     * - 如果 n 小于 2，按定义不是素数；
     * - 否则从 2 一直试到 n/2；
     * - 只要中途找到能整除 n 的数，就说明 n 不是素数。
     *
     * @param n 要判断的整数
     * @return 如果 n 是素数则返回 true，否则返回 false
     */
    public static boolean isPrimeByHalf(int n) {
        if (n < 2) {
            return false;
        }

        for (int divisor = 2; divisor <= n / 2; divisor++) {
            if (n % divisor == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 使用“最多测试到 sqrt(n)”的方式判断一个整数是否为素数。
     * <p>
     * 这是更高效的写法，因为如果 n 有因子，
     * 那么至少会有一个因子不大于 sqrt(n)。
     *
     * @param n 要判断的整数
     * @return 如果 n 是素数则返回 true，否则返回 false
     */
    public static boolean isPrimeBySqrt(int n) {
        if (n < 2) {
            return false;
        }

        int limit = (int) Math.sqrt(n);
        for (int divisor = 2; divisor <= limit; divisor++) {
            if (n % divisor == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 统计“测试到 n/2”为止，一共做了多少次试除。
     * <p>
     * 这个方法不关心最终是否是素数，而是专门用来做效率统计。
     * 一旦中途找到因子，就停止计数并返回当前累计次数。
     *
     * @param n 要统计的整数
     * @return 在 n/2 策略下的试除次数
     */
    static int countChecksByHalf(int n) {
        if (n < 2) {
            return 0;
        }

        int checks = 0;
        for (int divisor = 2; divisor <= n / 2; divisor++) {
            checks++;
            if (n % divisor == 0) {
                break;
            }
        }
        return checks;
    }

    /**
     * 统计“测试到 sqrt(n)”为止，一共做了多少次试除。
     * <p>
     * 这个方法与 {@code countChecksByHalf} 的区别，
     * 是它只统计到平方根位置，因此通常次数更少。
     *
     * @param n 要统计的整数
     * @return 在 sqrt(n) 策略下的试除次数
     */
    static int countChecksBySqrt(int n) {
        if (n < 2) {
            return 0;
        }

        int checks = 0;
        int limit = (int) Math.sqrt(n);
        for (int divisor = 2; divisor <= limit; divisor++) {
            checks++;
            if (n % divisor == 0) {
                break;
            }
        }
        return checks;
    }
}
