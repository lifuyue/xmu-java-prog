public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "console".equalsIgnoreCase(args[0])) {
            new ConsoleGuessGame().play();
        } else {
            new GUIGame().play();
        }
    }
}
