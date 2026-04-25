public class GenericEqualityDemo {
    public static <T> boolean isEqualTo(T first, T second) {
        if (first == null) {
            return second == null;
        }
        return first.equals(second);
    }

    public static void main(String[] args) {
        Integer numberA = 1000;
        Integer numberB = 1000;
        String textA = new String("Java");
        String textB = new String("Java");
        Object objectA = new Object();
        Object objectB = new Object();
        Object sameObject = objectA;

        System.out.println("Integer 1000 与 1000 是否相等: " + isEqualTo(numberA, numberB));
        System.out.println("String \"Java\" 与 \"Java\" 是否相等: " + isEqualTo(textA, textB));
        System.out.println("两个不同 Object 对象是否相等: " + isEqualTo(objectA, objectB));
        System.out.println("同一个 Object 引用是否相等: " + isEqualTo(objectA, sameObject));
        System.out.println("null 与 null 是否相等: " + isEqualTo(null, null));
        System.out.println();
        System.out.println("结论: isEqualTo 内部调用 equals。Integer、String 会按内容比较；");
        System.out.println("未重写 equals 的 Object 默认按引用地址比较。");
    }
}

