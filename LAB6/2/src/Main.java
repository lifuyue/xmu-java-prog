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
            throw e;
        }
    }

    public static void someMethod2() throws Exception {
        throw new Exception("someMethod2 主动抛出的异常。");
    }
}
