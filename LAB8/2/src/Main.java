/**
 * 第 2 题入口：默认运行 GUI 版，传入 console 参数时运行控制台版。
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "console".equalsIgnoreCase(args[0])) {
            new ConsoleGuessGame().play();
        } else {
            new GUIGame().play();
        }
    }
}
