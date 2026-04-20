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
