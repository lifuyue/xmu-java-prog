import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.Window;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VideoDemoDriver {
    private static final String INITIAL_TEXT = """
            22920242203267 李富悦
            这是 Java Swing 记事本功能演示。
            AI 工具辅助完成代码生成、调试、截图和实验报告整理。
            notepad find replace test
            下面加入一行较长文本，用于演示自动换行、缩放、字体设置和状态栏变化：Java Swing Notepad supports file, edit, search, format and view operations.
            """;

    private final boolean dryRun;
    private final double pace;
    private final Path demoFile;
    private final long recordingLeadInMs;
    private final boolean keepOpenAfterDemo;
    private final List<TimelineItem> timeline = new ArrayList<>();

    private NotepadFrame frame;
    private long elapsedMs;

    private VideoDemoDriver(
            boolean dryRun,
            double pace,
            Path demoFile,
            long recordingLeadInMs,
            boolean keepOpenAfterDemo
    ) {
        this.dryRun = dryRun;
        this.pace = pace;
        this.demoFile = demoFile;
        this.recordingLeadInMs = recordingLeadInMs;
        this.keepOpenAfterDemo = keepOpenAfterDemo;
        buildTimeline();
    }

    public static void main(String[] args) throws Exception {
        boolean dryRun = false;
        boolean durationOnly = false;
        boolean keepOpenAfterDemo = false;
        long recordingLeadInMs = 0;
        double pace = 1.25;
        Path demoFile = Path.of("video", "demo-note.txt").toAbsolutePath().normalize();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--dry-run" -> dryRun = true;
                case "--duration-seconds" -> durationOnly = true;
                case "--pace" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("--pace 需要 slow、normal 或 fast");
                    }
                    pace = parsePace(args[++i]);
                }
                case "--demo-file" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("--demo-file 需要一个文件路径");
                    }
                    demoFile = Path.of(args[++i]).toAbsolutePath().normalize();
                }
                case "--recording-lead-in-ms" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("--recording-lead-in-ms 需要毫秒数");
                    }
                    recordingLeadInMs = Long.parseLong(args[++i]);
                    if (recordingLeadInMs < 0) {
                        throw new IllegalArgumentException("--recording-lead-in-ms 不能为负数");
                    }
                }
                case "--keep-open-after-demo" -> keepOpenAfterDemo = true;
                default -> throw new IllegalArgumentException("未知参数：" + args[i]);
            }
        }

        VideoDemoDriver driver = new VideoDemoDriver(
                dryRun,
                pace,
                demoFile,
                recordingLeadInMs,
                keepOpenAfterDemo
        );
        if (durationOnly) {
            System.out.println(driver.recommendedRecordingSeconds());
            return;
        }
        if (dryRun) {
            driver.printTimeline();
            return;
        }
        driver.runDemo();
    }

    private static double parsePace(String value) {
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "slow" -> 1.35;
            case "normal" -> 1.15;
            case "fast" -> 0.85;
            default -> Double.parseDouble(value);
        };
    }

    private void buildTimeline() {
        add("启动程序，停留在空白记事本窗口", 3200, this::showFrame);
        type("输入学号姓名和测试文本", INITIAL_TEXT, 36, 2200);
        add("保存当前文档，标题栏显示 demo-note.txt", 2800, this::saveCurrentDocument);
        add("新建空白文档，展示文件菜单中新建后的空白状态", 2200, () -> setText("", null));
        add("重新打开刚才保存的文本，验证文件读取", 3200, this::openSavedDocument);
        add("选中第一行并复制，展示文本选择", 2400, this::selectIdentityLine);
        add("粘贴复制内容到文末，展示复制和粘贴", 2400, this::pasteSelectionToEnd);
        add("剪切刚粘贴的内容，展示剪切功能", 2400, this::cutLastLine);
        add("执行撤销，恢复刚才剪切的内容", 2400, this::undoLastEdit);
        add("插入时间日期，展示 F5 功能", 2600, this::insertDateTime);
        add("打开查找替换窗口，并填入查找和替换词", 4200, this::showFindReplaceDialog);
        add("执行替换演示，将 AI 替换为人工智能", 3200, this::replaceAiText);
        add("关闭自动换行，显示长行和水平滚动效果", 3200, () -> setWordWrap(false));
        add("重新打开自动换行，恢复接近 Windows 记事本的阅读状态", 2600, () -> setWordWrap(true));
        add("打开字体窗口，展示字号和预览区域", 4800, this::showFontDialog);
        add("应用更大的粗体字体，展示格式设置结果", 3000, this::applyLargeFontAndCloseDialog);
        add("放大显示，观察状态栏缩放比例变化", 2600, this::zoomInSeveralTimes);
        add("缩小显示，观察文本大小变化", 2200, this::zoomOutOnce);
        add("恢复默认缩放，回到正常阅读状态", 2400, this::resetZoom);
        add("移动光标到文末，观察状态栏行列号和字符数变化", 3200, this::moveCaretForStatusBar);
        add("修改文本并触发未保存确认提示", 5200, this::showUnsavedPrompt);
        add("回到最终画面，保留学号姓名和主要演示内容", 5200, this::finalScreen);
    }

    private void add(String title, int holdMs, StepAction action) {
        timeline.add(new TimelineItem(title, holdMs, 0, action, null));
    }

    private void type(String title, String text, int perCharMs, int holdMs) {
        timeline.add(new TimelineItem(title, holdMs, perCharMs, null, text));
    }

    private void printTimeline() {
        long total = 0;
        System.out.println("视频演示 dry-run 时间线（默认节奏偏慢，便于老师观看）：");
        for (int i = 0; i < timeline.size(); i++) {
            TimelineItem item = timeline.get(i);
            long duration = durationFor(item);
            if (i == 0) {
                duration += recordingLeadInMs;
            }
            total += duration;
            System.out.printf(Locale.ROOT, "%6.1fs  %s%n", total / 1000.0, item.title());
        }
        long recommended = total + Duration.ofSeconds(8).toMillis();
        System.out.printf(Locale.ROOT, "%n预计演示时长：%.1f 秒%n", total / 1000.0);
        System.out.printf(Locale.ROOT, "建议录屏秒数：%d 秒%n", (recommended + 999) / 1000);
    }

    private long recommendedRecordingSeconds() {
        long total = 0;
        for (int i = 0; i < timeline.size(); i++) {
            TimelineItem item = timeline.get(i);
            total += durationFor(item);
            if (i == 0) {
                total += recordingLeadInMs;
            }
        }
        return (total + Duration.ofSeconds(8).toMillis() + 999) / 1000;
    }

    private void runDemo() throws Exception {
        Files.createDirectories(demoFile.getParent());
        for (int i = 0; i < timeline.size(); i++) {
            TimelineItem item = timeline.get(i);
            System.out.printf(Locale.ROOT, "[%6.1fs] %s%n", elapsedMs / 1000.0, item.title());
            if (item.textToType() != null) {
                typeText(item.textToType(), scaled(item.perCharMs()));
                pause(scaled(item.holdMs()));
            } else {
                invoke(item.action());
                if (i == 0 && recordingLeadInMs > 0) {
                    pause(recordingLeadInMs);
                }
                pause(scaled(item.holdMs()));
            }
        }
        if (keepOpenAfterDemo) {
            while (true) {
                Thread.sleep(1000);
            }
        }
        invoke(() -> frame.dispose());
    }

    private int scaled(int ms) {
        return Math.max(120, (int) Math.round(ms * pace));
    }

    private long scaled(long ms) {
        return Math.max(120L, Math.round(ms * pace));
    }

    private long durationFor(TimelineItem item) {
        if (item.textToType() == null) {
            return scaled(item.holdMs());
        }
        return (long) item.textToType().length() * scaled(item.perCharMs()) + scaled(item.holdMs());
    }

    private void pause(long ms) throws InterruptedException {
        elapsedMs += ms;
        Thread.sleep(ms);
    }

    private void invoke(StepAction action) throws Exception {
        if (action == null) {
            return;
        }
        SwingUtilities.invokeAndWait(() -> {
            try {
                action.run();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void typeText(String text, int perCharMs) throws Exception {
        for (int i = 0; i < text.length(); i++) {
            String ch = String.valueOf(text.charAt(i));
            invoke(() -> {
                JTextArea area = frame.getTextArea();
                area.append(ch);
                area.setCaretPosition(area.getDocument().getLength());
            });
            pause(perCharMs);
        }
    }

    private void showFrame() {
        frame = new NotepadFrame();
        frame.setLocation(90, 80);
        frame.setVisible(true);
        frame.toFront();
        frame.requestFocus();
        frame.getTextArea().requestFocusInWindow();
    }

    private void saveCurrentDocument() throws Exception {
        FileService.save(demoFile, frame.getTextArea().getText());
        setText(frame.getTextArea().getText(), demoFile);
    }

    private void openSavedDocument() throws Exception {
        String text = Files.readString(demoFile, StandardCharsets.UTF_8);
        setText(text, demoFile);
    }

    private void setText(String text, Path path) {
        frame.setTextForDemo(text, path);
        frame.getTextArea().requestFocusInWindow();
    }

    private void selectIdentityLine() {
        JTextArea area = frame.getTextArea();
        int end = Math.max(0, area.getText().indexOf('\n'));
        area.select(0, end);
        area.copy();
    }

    private void pasteSelectionToEnd() {
        JTextArea area = frame.getTextArea();
        area.setCaretPosition(area.getDocument().getLength());
        area.append("\n");
        area.paste();
    }

    private void cutLastLine() {
        JTextArea area = frame.getTextArea();
        String text = area.getText();
        int start = text.lastIndexOf('\n');
        if (start >= 0) {
            area.select(start, text.length());
            area.cut();
        }
    }

    private void undoLastEdit() throws Exception {
        Method undo = NotepadFrame.class.getDeclaredMethod("undo");
        undo.setAccessible(true);
        undo.invoke(frame);
    }

    private void insertDateTime() throws Exception {
        JTextArea area = frame.getTextArea();
        area.setCaretPosition(area.getDocument().getLength());
        area.append("\n时间/日期功能：");
        Method insertDateTime = NotepadFrame.class.getDeclaredMethod("insertDateTime");
        insertDateTime.setAccessible(true);
        insertDateTime.invoke(frame);
    }

    private void showFindReplaceDialog() {
        FindReplaceDialog dialog = frame.getFindReplaceDialog();
        dialog.setDemoValues("AI", "人工智能");
        dialog.showReplaceTab();
        dialog.setLocation(frame.getX() + 210, frame.getY() + 135);
    }

    private void replaceAiText() {
        JTextArea area = frame.getTextArea();
        area.setText(area.getText().replace("AI", "人工智能"));
        area.setCaretPosition(Math.min(area.getText().length(), 50));
        frame.getFindReplaceDialog().setVisible(false);
    }

    private void setWordWrap(boolean enabled) throws Exception {
        Method applyWordWrap = NotepadFrame.class.getDeclaredMethod("applyWordWrap", boolean.class);
        applyWordWrap.setAccessible(true);
        applyWordWrap.invoke(frame, enabled);

        Field item = NotepadFrame.class.getDeclaredField("wordWrapItem");
        item.setAccessible(true);
        Object value = item.get(frame);
        if (value instanceof javax.swing.JCheckBoxMenuItem checkBox) {
            checkBox.setSelected(enabled);
        }
    }

    private void showFontDialog() {
        FontChooserDialog dialog = FontChooserDialog.createForScreenshot(frame, frame.getTextArea().getFont());
        dialog.setLocation(frame.getX() + 210, frame.getY() + 115);
        dialog.setVisible(true);
    }

    private void applyLargeFontAndCloseDialog() throws Exception {
        frame.chooseLargerDemoFont();
        closeDialogByTitle("字体");
    }

    private void zoomInSeveralTimes() {
        frame.zoomInForDemo();
        frame.zoomInForDemo();
        frame.zoomInForDemo();
    }

    private void zoomOutOnce() throws Exception {
        Method zoomOut = NotepadFrame.class.getDeclaredMethod("zoomOut");
        zoomOut.setAccessible(true);
        zoomOut.invoke(frame);
    }

    private void resetZoom() throws Exception {
        Method resetZoom = NotepadFrame.class.getDeclaredMethod("resetZoom");
        resetZoom.setAccessible(true);
        resetZoom.invoke(frame);
    }

    private void moveCaretForStatusBar() {
        JTextArea area = frame.getTextArea();
        area.setCaretPosition(area.getDocument().getLength());
        area.requestFocusInWindow();
    }

    private void showUnsavedPrompt() throws Exception {
        JTextArea area = frame.getTextArea();
        area.append("\n这行文字用于触发未保存确认提示。");
        Timer closer = new Timer(scaled(2600), event -> closeAllDialogs());
        closer.setRepeats(false);
        closer.start();

        Method confirm = NotepadFrame.class.getDeclaredMethod("confirmSaveIfNeeded");
        confirm.setAccessible(true);
        confirm.invoke(frame);
    }

    private void finalScreen() {
        closeAllDialogs();
        JTextArea area = frame.getTextArea();
        if (!area.getText().startsWith("22920242203267 李富悦")) {
            area.insert("22920242203267 李富悦\n", 0);
        }
        area.setCaretPosition(0);
        frame.toFront();
        area.requestFocusInWindow();
    }

    private void closeDialogByTitle(String title) {
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog dialog && dialog.isShowing() && title.equals(dialog.getTitle())) {
                dialog.dispose();
            }
        }
    }

    private void closeAllDialogs() {
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog dialog && dialog.isShowing()) {
                clickButton(dialog, "否");
                dialog.dispose();
            }
        }
    }

    private void clickButton(Window window, String text) {
        for (JButton button : findButtons(window)) {
            if (text.equals(button.getText())) {
                button.doClick();
                return;
            }
        }
    }

    private List<JButton> findButtons(java.awt.Container container) {
        List<JButton> buttons = new ArrayList<>();
        for (java.awt.Component component : container.getComponents()) {
            if (component instanceof JButton button) {
                buttons.add(button);
            }
            if (component instanceof java.awt.Container child) {
                buttons.addAll(findButtons(child));
            }
        }
        return buttons;
    }

    @FunctionalInterface
    private interface StepAction {
        void run() throws Exception;
    }

    private record TimelineItem(
            String title,
            int holdMs,
            int perCharMs,
            StepAction action,
            String textToType
    ) {
        long totalMs() {
            long typingMs = textToType == null ? 0 : (long) textToType.length() * perCharMs;
            return typingMs + holdMs;
        }
    }
}
