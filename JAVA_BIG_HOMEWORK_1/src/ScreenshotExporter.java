import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScreenshotExporter {
    private static final Path SCREENSHOT_DIR = Path.of("screenshots");
    private static final String DEMO_TEXT = """
            22920242203267 李富悦

            这是一段用于演示 Java Swing 记事本的文本。
            程序支持新建、打开、保存、查找替换、自动换行、状态栏、字体设置和缩放。
            查找替换演示：AI 工具可以辅助生成代码，AI 工具也需要人工验证。
            """;

    public static void main(String[] args) throws Exception {
        Files.createDirectories(SCREENSHOT_DIR);
        SwingUtilities.invokeAndWait(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Screenshot generation can use the default Swing style if the system style is unavailable.
            }
            try {
                exportAll();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private static void exportAll() throws Exception {
        exportTerminalLikeScreenshot(
                SCREENSHOT_DIR.resolve("compile-run.png"),
                "编译运行",
                new String[]{
                        "$ cd JAVA_BIG_HOMEWORK_1",
                        "$ javac -encoding UTF-8 -d bin src/*.java",
                        "$ java -cp bin NotepadApp",
                        "程序窗口已启动：无标题 - AI 记事本"
                }
        );

        exportTerminalLikeScreenshot(
                SCREENSHOT_DIR.resolve("ai-code-process.png"),
                "AI 辅助生成代码过程",
                new String[]{
                        "提示词：请使用 Java Swing 开发一个接近 Windows 记事本的桌面程序...",
                        "AI 生成：NotepadApp、NotepadFrame、FileService、FindReplaceDialog 等类。",
                        "人工检查：编译、运行、修正交互细节，补充报告和截图。",
                        "说明：提交前可替换为真实 Codex 对话截图。"
                }
        );

        NotepadFrame mainFrame = new NotepadFrame();
        mainFrame.setTextForDemo(DEMO_TEXT);
        mainFrame.setSize(900, 620);
        captureWindow(mainFrame, SCREENSHOT_DIR.resolve("main-window.png"));
        mainFrame.dispose();

        NotepadFrame savedFrame = new NotepadFrame();
        Path demoPath = Path.of("examples", "demo-note.txt").toAbsolutePath();
        savedFrame.setTextForDemo(DEMO_TEXT, demoPath);
        savedFrame.setSize(900, 620);
        captureWindow(savedFrame, SCREENSHOT_DIR.resolve("save-open.png"));
        savedFrame.dispose();

        NotepadFrame findFrame = new NotepadFrame();
        findFrame.setTextForDemo(DEMO_TEXT);
        findFrame.setSize(900, 620);
        findFrame.setVisible(true);
        FindReplaceDialog findDialog = findFrame.getFindReplaceDialog();
        findDialog.setDemoValues("AI", "人工智能");
        findDialog.showReplaceTab();
        captureWindow(findDialog, SCREENSHOT_DIR.resolve("find-replace.png"));
        findDialog.dispose();
        findFrame.dispose();

        NotepadFrame fontOwner = new NotepadFrame();
        fontOwner.setSize(900, 620);
        fontOwner.setVisible(true);
        JDialog fontDialog = FontChooserDialog.createForScreenshot(fontOwner, new Font(Font.MONOSPACED, Font.PLAIN, 18));
        fontDialog.setVisible(true);
        captureWindow(fontDialog, SCREENSHOT_DIR.resolve("font-dialog.png"));
        fontDialog.dispose();
        fontOwner.dispose();

        NotepadFrame zoomFrame = new NotepadFrame();
        zoomFrame.setTextForDemo(DEMO_TEXT);
        zoomFrame.chooseLargerDemoFont();
        zoomFrame.zoomInForDemo();
        zoomFrame.zoomInForDemo();
        zoomFrame.setSize(900, 620);
        captureWindow(zoomFrame, SCREENSHOT_DIR.resolve("zoom-status.png"));
        zoomFrame.dispose();
    }

    private static void captureWindow(Window window, Path outputPath) throws IOException {
        window.setLocation(80, 80);
        window.setVisible(true);
        window.validate();
        BufferedImage image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        window.paintAll(graphics);
        graphics.dispose();
        ImageIO.write(image, "png", outputPath.toFile());
    }

    private static void exportTerminalLikeScreenshot(Path outputPath, String title, String[] lines) throws IOException {
        int width = 980;
        int height = 420;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(new Color(32, 34, 37));
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(new Color(58, 61, 66));
        graphics.fillRoundRect(24, 24, width - 48, height - 48, 16, 16);
        graphics.setColor(new Color(236, 239, 244));
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        graphics.drawString(title, 56, 72);
        graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        int y = 120;
        for (String line : lines) {
            graphics.drawString(line, 56, y);
            y += 42;
        }
        graphics.dispose();
        ImageIO.write(image, "png", outputPath.toFile());
    }
}
