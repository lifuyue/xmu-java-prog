import java.util.Objects;

/**
 * 有理数类。
 * <p>
 * 该类使用最简分数形式保存数据，分母始终保持为正数。
 */
public class Rational extends Number implements Comparable<Rational> {
    private final int numerator;
    private final int denominator;

    /**
     * 默认构造器，创建 0/1。
     */
    public Rational() {
        this(0, 1);
    }

    /**
     * 整数构造器，等价于 numerator/1。
     *
     * @param numerator 分子
     */
    public Rational(int numerator) {
        this(numerator, 1);
    }

    /**
     * 分子分母构造器。
     *
     * @param numerator 分子
     * @param denominator 分母
     */
    public Rational(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("分母不能为 0。");
        }

        if (numerator == 0) {
            this.numerator = 0;
            this.denominator = 1;
            return;
        }

        int normalizedNumerator = numerator;
        int normalizedDenominator = denominator;
        if (normalizedDenominator < 0) {
            normalizedNumerator = -normalizedNumerator;
            normalizedDenominator = -normalizedDenominator;
        }

        int gcd = gcd(Math.abs(normalizedNumerator), normalizedDenominator);
        this.numerator = normalizedNumerator / gcd;
        this.denominator = normalizedDenominator / gcd;
    }

    /**
     * 字符串构造器，支持 "a/b" 和 "a" 两种格式。
     *
     * @param text 有理数字符串
     */
    public Rational(String text) {
        this(parseNumerator(text), parseDenominator(text));
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public Rational add(Rational other) {
        requireNonNull(other);
        long newNumerator = (long) numerator * other.denominator + (long) other.numerator * denominator;
        long newDenominator = (long) denominator * other.denominator;
        return new Rational(safeToInt(newNumerator), safeToInt(newDenominator));
    }

    public Rational subtract(Rational other) {
        requireNonNull(other);
        long newNumerator = (long) numerator * other.denominator - (long) other.numerator * denominator;
        long newDenominator = (long) denominator * other.denominator;
        return new Rational(safeToInt(newNumerator), safeToInt(newDenominator));
    }

    public Rational multiply(Rational other) {
        requireNonNull(other);
        long newNumerator = (long) numerator * other.numerator;
        long newDenominator = (long) denominator * other.denominator;
        return new Rational(safeToInt(newNumerator), safeToInt(newDenominator));
    }

    public Rational divide(Rational other) {
        requireNonNull(other);
        if (other.numerator == 0) {
            throw new ArithmeticException("除数不能为 0。");
        }
        long newNumerator = (long) numerator * other.denominator;
        long newDenominator = (long) denominator * other.numerator;
        return new Rational(safeToInt(newNumerator), safeToInt(newDenominator));
    }

    public Rational abs() {
        if (numerator >= 0) {
            return this;
        }
        return new Rational(Math.abs(numerator), denominator);
    }

    public boolean isPositive() {
        return numerator > 0;
    }

    public boolean isNegative() {
        return numerator < 0;
    }

    public boolean isZero() {
        return numerator == 0;
    }

    @Override
    public int compareTo(Rational other) {
        requireNonNull(other);
        long left = (long) numerator * other.denominator;
        long right = (long) other.numerator * denominator;
        return Long.compare(left, right);
    }

    public static Rational max(Rational first, Rational second) {
        requireNonNull(first);
        requireNonNull(second);
        return first.compareTo(second) >= 0 ? first : second;
    }

    public static Rational min(Rational first, Rational second) {
        requireNonNull(first);
        requireNonNull(second);
        return first.compareTo(second) <= 0 ? first : second;
    }

    @Override
    public int intValue() {
        return numerator / denominator;
    }

    @Override
    public long longValue() {
        return (long) numerator / denominator;
    }

    @Override
    public float floatValue() {
        return (float) numerator / denominator;
    }

    @Override
    public double doubleValue() {
        return (double) numerator / denominator;
    }

    @Override
    public String toString() {
        if (denominator == 1) {
            return String.valueOf(numerator);
        }
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Rational other)) {
            return false;
        }
        return numerator == other.numerator && denominator == other.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    private static void requireNonNull(Rational value) {
        if (value == null) {
            throw new IllegalArgumentException("有理数参数不能为空。");
        }
    }

    private static int parseNumerator(String text) {
        String normalized = normalizeText(text);
        if (!normalized.contains("/")) {
            return parseInteger(normalized);
        }

        String[] parts = normalized.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("有理数字符串格式错误，应为 a/b 或 a。");
        }
        return parseInteger(parts[0].trim());
    }

    private static int parseDenominator(String text) {
        String normalized = normalizeText(text);
        if (!normalized.contains("/")) {
            return 1;
        }

        String[] parts = normalized.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("有理数字符串格式错误，应为 a/b 或 a。");
        }
        return parseInteger(parts[1].trim());
    }

    private static String normalizeText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("有理数字符串不能为空。");
        }
        String normalized = text.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("有理数字符串不能为空。");
        }
        return normalized;
    }

    private static int parseInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("有理数字符串中包含非法整数：" + text, exception);
        }
    }

    private static int gcd(int first, int second) {
        int a = first;
        int b = second;
        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a == 0 ? 1 : a;
    }

    private static int safeToInt(long value) {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new ArithmeticException("运算结果超出 int 范围。");
        }
        return (int) value;
    }
}
