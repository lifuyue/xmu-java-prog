/**
 * 第 1 题：演示构造方法也可以抛出异常，调用方必须用 try-catch 处理。
 */
public class Main {
    public static void main(String[] args) {
        try {
            SomeClass someObject = new SomeClass("demo");
            System.out.println("对象创建成功：" + someObject);
        } catch (ConstructionException e) {
            System.out.println("捕获到构造方法抛出的异常：");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

class SomeClass {
    private final String name;

    public SomeClass(String name) throws ConstructionException {
        this.name = name;
        // 构造过程中发现对象无法正常创建时，直接抛出自定义异常。
        throw new ConstructionException("SomeClass 构造失败，name=" + this.name);
    }

    @Override
    public String toString() {
        return "SomeClass{name='" + name + "'}";
    }
}

class ConstructionException extends Exception {
    public ConstructionException(String message) {
        super(message);
    }
}
