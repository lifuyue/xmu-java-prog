/**
 * 第 2 题测试类。
 */
public class Main {
    public static void main(String[] args) {
        Rational first = new Rational(1, 3);
        Rational second = new Rational("3/4");

        System.out.println("=== Rational 测试 ===");
        System.out.println("第一个有理数：" + first);
        System.out.println("第二个有理数：" + second);
        System.out.println();

        System.out.println("加法结果：" + first.add(second));
        System.out.println("减法结果：" + first.subtract(second));
        System.out.println("乘法结果：" + first.multiply(second));
        System.out.println("除法结果：" + first.divide(second));
        System.out.println();

        Rational negative = new Rational("-6/8");
        System.out.println("负有理数示例：" + negative);
        System.out.println("绝对值：" + negative.abs());
        System.out.println("第一个有理数是否为正数：" + first.isPositive());
        System.out.println("负有理数是否为负数：" + negative.isNegative());
        System.out.println("0 是否为零：" + new Rational().isZero());
        System.out.println();

        Rational equivalent = new Rational(2, 4);
        System.out.println("1/2 与 2/4 是否相等：" + new Rational(1, 2).equals(equivalent));
        System.out.println("compareTo 结果：" + first.compareTo(second));
        System.out.println("最大值：" + Rational.max(first, second));
        System.out.println("最小值：" + Rational.min(first, second));
        System.out.println();

        System.out.println("第二个有理数的分子：" + second.getNumerator());
        System.out.println("第二个有理数的分母：" + second.getDenominator());
        System.out.println("第二个有理数转 int：" + second.intValue());
        System.out.println("第二个有理数转 double：" + second.doubleValue());
        System.out.println("第二个有理数转 long：" + second.longValue());
        System.out.println("第二个有理数转 float：" + second.floatValue());
    }
}
