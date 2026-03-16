/**
 * 素数判断工具类。
 */
public final class PrimeUtils {
    private PrimeUtils() {
    }

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
