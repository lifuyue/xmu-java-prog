/**
 * 第 2 题：演示异常从底层方法抛出后，被中间方法捕获并继续向上重抛。
 */
public class Main {
    public static void main(String[] args) {
        try {
            someMethod();
        } catch (Exception e) {
            System.out.println("main 方法捕获到被重抛的异常，栈踪迹如下：");
            e.printStackTrace();
        }
    }

    public static void someMethod() throws Exception {
        try {
            someMethod2();
        } catch (Exception e) {
            System.out.println("someMethod 捕获到异常，准备重抛。");
            // 重新抛出同一个异常对象，main 中打印栈踪迹时仍能看到原始出错位置。
            throw e;
        }
    }

    public static void someMethod2() throws Exception {
        throw new Exception("someMethod2 主动抛出的异常。");
    }
}
