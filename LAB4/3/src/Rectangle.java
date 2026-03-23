/**
 * 矩形类。
 */
public class Rectangle extends Graph {
    private final int length;
    private final int width;

    public Rectangle(int length, int width) {
        this.length = length;
        this.width = width;
    }

    @Override
    public double getArea() {
        return (double) length * width;
    }
}
