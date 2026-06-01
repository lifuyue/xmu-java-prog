import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotepadFrame extends JFrame {
    private static final String APP_TITLE = "AI 记事本";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private final DocumentModel documentModel = new DocumentModel();
    private final JTextArea textArea = new JTextArea();
    private final StatusBar statusBar = new StatusBar();
    private final UndoManager undoManager = new UndoManager();
    private final JFileChooser fileChooser = new JFileChooser();
    private final FindReplaceDialog findReplaceDialog;
    private final JScrollPane scrollPane;

    private JCheckBoxMenuItem wordWrapItem;
    private JCheckBoxMenuItem statusBarItem;
    private Font editorBaseFont = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    private int defaultFontSize = 16;
    private int currentFontSize = 16;
    private boolean loadingDocument;

    public NotepadFrame() {
        super(APP_TITLE);
        fileChooser.setFileFilter(new FileNameExtensionFilter("文本文档 (*.txt)", "txt"));
        findReplaceDialog = new FindReplaceDialog(this, textArea);
        scrollPane = new JScrollPane(textArea);

        configureTextArea();
        setJMenuBar(createMenuBar());
        add(scrollPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        configureWindow();
        applyWordWrap(true);
        applyEditorFont();
        updateStatusBar();
        updateTitle();
    }

    JTextArea getTextArea() {
        return textArea;
    }

    FindReplaceDialog getFindReplaceDialog() {
        return findReplaceDialog;
    }

    void setTextForDemo(String text) {
        loadText(text, null);
    }

    void setTextForDemo(String text, Path path) {
        loadText(text, path);
    }

    void zoomInForDemo() {
        zoomIn();
    }

    void chooseLargerDemoFont() {
        editorBaseFont = editorBaseFont.deriveFont(Font.BOLD, 20f);
        defaultFontSize = 20;
        currentFontSize = 20;
        applyEditorFont();
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitApplication();
            }
        });
    }

    private void configureTextArea() {
        textArea.setFont(editorBaseFont);
        textArea.setTabSize(4);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                markModified();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                markModified();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                markModified();
            }
        });
        textArea.getDocument().addUndoableEditListener(event -> {
            if (!loadingDocument) {
                undoManager.addEdit(event.getEdit());
            }
        });
        textArea.addCaretListener(event -> updateStatusBar());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createFormatMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("文件");
        menu.add(menuItem("新建", KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK, event -> newDocument()));
        menu.add(menuItem("打开...", KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK, event -> openDocument()));
        menu.add(menuItem("保存", KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, event -> saveDocument()));
        menu.add(menuItem("另存为...", KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, event -> saveDocumentAs()));
        menu.addSeparator();
        menu.add(menuItem("退出", 0, 0, event -> exitApplication()));
        return menu;
    }

    private JMenu createEditMenu() {
        JMenu menu = new JMenu("编辑");
        menu.add(menuItem("撤销", KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK, event -> undo()));
        menu.addSeparator();
        menu.add(menuItem("剪切", KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK, event -> textArea.cut()));
        menu.add(menuItem("复制", KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK, event -> textArea.copy()));
        menu.add(menuItem("粘贴", KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK, event -> textArea.paste()));
        menu.add(menuItem("删除", KeyEvent.VK_DELETE, 0, event -> textArea.replaceSelection("")));
        menu.addSeparator();
        menu.add(menuItem("查找...", KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK, event -> findReplaceDialog.showFindTab()));
        menu.add(menuItem("替换...", KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK, event -> findReplaceDialog.showReplaceTab()));
        menu.addSeparator();
        menu.add(menuItem("全选", KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK, event -> textArea.selectAll()));
        menu.add(menuItem("时间/日期", KeyEvent.VK_F5, 0, event -> insertDateTime()));
        return menu;
    }

    private JMenu createFormatMenu() {
        JMenu menu = new JMenu("格式");
        wordWrapItem = new JCheckBoxMenuItem("自动换行", true);
        wordWrapItem.addActionListener(event -> applyWordWrap(wordWrapItem.isSelected()));
        menu.add(wordWrapItem);
        menu.add(menuItem("字体...", 0, 0, event -> chooseFont()));
        return menu;
    }

    private JMenu createViewMenu() {
        JMenu menu = new JMenu("查看");
        menu.add(menuItem("放大", KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK, event -> zoomIn()));
        menu.add(menuItem("缩小", KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK, event -> zoomOut()));
        menu.add(menuItem("恢复默认缩放", KeyEvent.VK_0, InputEvent.CTRL_DOWN_MASK, event -> resetZoom()));
        menu.addSeparator();
        statusBarItem = new JCheckBoxMenuItem("状态栏", true);
        statusBarItem.addActionListener(event -> statusBar.setVisible(statusBarItem.isSelected()));
        menu.add(statusBarItem);
        return menu;
    }

    private JMenu createHelpMenu() {
        JMenu menu = new JMenu("帮助");
        menu.add(menuItem("关于 AI 记事本", 0, 0, event -> showAbout()));
        return menu;
    }

    private JMenuItem menuItem(String label, int keyCode, int modifiers, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(listener);
        if (keyCode != 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        }
        return item;
    }

    private void newDocument() {
        if (!confirmSaveIfNeeded()) {
            return;
        }
        loadText("", null);
    }

    private void openDocument() {
        if (!confirmSaveIfNeeded()) {
            return;
        }
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        try {
            String content = FileService.open(file.toPath());
            loadText(content, file.toPath());
        } catch (IOException ex) {
            showError("打开文件失败：" + ex.getMessage());
        }
    }

    private boolean saveDocument() {
        if (!documentModel.hasCurrentFile()) {
            return saveDocumentAs();
        }
        return saveToPath(documentModel.getCurrentFile());
    }

    private boolean saveDocumentAs() {
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        File selected = FileService.withTxtExtension(fileChooser.getSelectedFile());
        if (selected.exists()) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "文件已存在，是否覆盖？",
                    APP_TITLE,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        return saveToPath(selected.toPath());
    }

    private boolean saveToPath(Path path) {
        try {
            FileService.save(path, textArea.getText());
            documentModel.setCurrentFile(path);
            documentModel.setModified(false);
            fileChooser.setSelectedFile(path.toFile());
            updateTitle();
            return true;
        } catch (IOException ex) {
            showError("保存文件失败：" + ex.getMessage());
            return false;
        }
    }

    private boolean confirmSaveIfNeeded() {
        if (!documentModel.isModified()) {
            return true;
        }
        int result = JOptionPane.showConfirmDialog(
                this,
                "是否保存对 \"" + documentModel.getDisplayName() + "\" 的更改？",
                APP_TITLE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
            return false;
        }
        if (result == JOptionPane.YES_OPTION) {
            return saveDocument();
        }
        return true;
    }

    private void exitApplication() {
        if (confirmSaveIfNeeded()) {
            dispose();
            System.exit(0);
        }
    }

    private void undo() {
        try {
            if (undoManager.canUndo()) {
                undoManager.undo();
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (CannotUndoException ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void insertDateTime() {
        textArea.replaceSelection(LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    private void chooseFont() {
        Font chosen = FontChooserDialog.showDialog(this, textArea.getFont());
        if (chosen == null) {
            return;
        }
        editorBaseFont = chosen;
        defaultFontSize = chosen.getSize();
        currentFontSize = defaultFontSize;
        applyEditorFont();
    }

    private void applyWordWrap(boolean enabled) {
        textArea.setLineWrap(enabled);
        textArea.setWrapStyleWord(enabled);
        scrollPane.setHorizontalScrollBarPolicy(
                enabled ? JScrollPane.HORIZONTAL_SCROLLBAR_NEVER : JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
    }

    private void zoomIn() {
        if (currentFontSize < 72) {
            currentFontSize = Math.min(72, currentFontSize + 2);
            applyEditorFont();
        }
    }

    private void zoomOut() {
        if (currentFontSize > 8) {
            currentFontSize = Math.max(8, currentFontSize - 2);
            applyEditorFont();
        }
    }

    private void resetZoom() {
        currentFontSize = defaultFontSize;
        applyEditorFont();
    }

    private void applyEditorFont() {
        textArea.setFont(editorBaseFont.deriveFont((float) currentFontSize));
        int zoomPercent = Math.round((currentFontSize * 100f) / Math.max(1, defaultFontSize));
        statusBar.updateZoom(zoomPercent);
    }

    private void loadText(String content, Path path) {
        loadingDocument = true;
        textArea.setText(content);
        textArea.setCaretPosition(0);
        undoManager.discardAllEdits();
        loadingDocument = false;

        documentModel.setCurrentFile(path);
        documentModel.setModified(false);
        updateTitle();
        updateStatusBar();
    }

    private void markModified() {
        if (loadingDocument) {
            return;
        }
        if (!documentModel.isModified()) {
            documentModel.setModified(true);
            updateTitle();
        }
        updateStatusBar();
    }

    private void updateTitle() {
        String prefix = documentModel.isModified() ? "*" : "";
        setTitle(prefix + documentModel.getDisplayName() + " - " + APP_TITLE);
    }

    private void updateStatusBar() {
        try {
            int caret = textArea.getCaretPosition();
            int line = textArea.getLineOfOffset(caret);
            int column = caret - textArea.getLineStartOffset(line);
            statusBar.updateCaretPosition(line + 1, column + 1);
        } catch (BadLocationException ex) {
            statusBar.updateCaretPosition(1, 1);
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(
                this,
                "AI 记事本\n\nJava Swing 大作业 1\n作者：22920242203267 李富悦\n技术栈：Java Swing",
                "关于 AI 记事本",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, APP_TITLE, JOptionPane.ERROR_MESSAGE);
    }
}
