public class Main {
    public static void main(String[] args) {
        Integer number1 = 100;
        Integer number2 = 100;

        Object object1 = new String("hello");
        Object object2 = new String("hello");

        Object refObject1 = new Object();
        Object refObject2 = new Object();
        Object refObject3 = refObject1;

        System.out.println("Integer 比较结果：" + isEqualTo(number1, number2));
        System.out.println("内容相同的 String 对象比较结果：" + isEqualTo(object1, object2));
        System.out.println("两个不同 Object 对象比较结果：" + isEqualTo(refObject1, refObject2));
        System.out.println("同一引用的 Object 比较结果：" + isEqualTo(refObject1, refObject3));

        System.out.println("结论：");
        System.out.println("1. isEqualTo 调用的是对象自己的 equals 方法。");
        System.out.println("2. Integer、String 等重写了 equals 的类型比较的是内容。");
        System.out.println("3. Object 默认没有按内容重写 equals，因此比较的是引用地址是否相同。");
    }

    public static <T> boolean isEqualTo(T first, T second) {
        if (first == null) {
            return second == null;
        }
        return first.equals(second);
    }
}
