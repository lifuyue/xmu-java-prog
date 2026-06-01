import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class FindReplaceDialog extends JDialog {
    private final JTextArea editor;
    private final JTabbedPane tabs = new JTabbedPane();
    private final JTextField findField = new JTextField(24);
    private final JTextField replaceFindField = new JTextField(24);
    private final JTextField replaceField = new JTextField(24);
    private final JCheckBox matchCaseBox = new JCheckBox("区分大小写");

    public FindReplaceDialog(Frame owner, JTextArea editor) {
        super(owner, "查找和替换", false);
        this.editor = editor;
        buildLayout();
        setSize(520, 260);
        setLocationRelativeTo(owner);
    }

    public void showFindTab() {
        syncFindText();
        tabs.setSelectedIndex(0);
        setVisible(true);
        findField.requestFocusInWindow();
        findField.selectAll();
    }

    public void showReplaceTab() {
        syncFindText();
        tabs.setSelectedIndex(1);
        setVisible(true);
        replaceFindField.requestFocusInWindow();
        replaceFindField.selectAll();
    }

    public void setDemoValues(String findText, String replaceText) {
        findField.setText(findText);
        replaceFindField.setText(findText);
        replaceField.setText(replaceText);
    }

    private void buildLayout() {
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(content);

        tabs.addTab("查找", buildFindPanel());
        tabs.addTab("替换", buildReplacePanel());
        content.add(tabs, BorderLayout.CENTER);

        JPanel options = new JPanel(new FlowLayout(FlowLayout.LEFT));
        options.add(matchCaseBox);
        content.add(options, BorderLayout.SOUTH);
    }

    private JPanel buildFindPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        form.add(new JLabel("查找内容:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(findField, gbc);
        panel.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton findNext = new JButton("查找下一个");
        JButton cancel = new JButton("取消");
        buttons.add(findNext);
        buttons.add(cancel);
        panel.add(buttons, BorderLayout.SOUTH);

        findNext.addActionListener(event -> findNext(findField.getText()));
        cancel.addActionListener(event -> setVisible(false));
        return panel;
    }

    private JPanel buildReplacePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        form.add(new JLabel("查找内容:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(replaceFindField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        form.add(new JLabel("替换为:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(replaceField, gbc);
        panel.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton findNext = new JButton("查找下一个");
        JButton replace = new JButton("替换");
        JButton replaceAll = new JButton("全部替换");
        JButton cancel = new JButton("取消");
        buttons.add(findNext);
        buttons.add(replace);
        buttons.add(replaceAll);
        buttons.add(cancel);
        panel.add(buttons, BorderLayout.SOUTH);

        findNext.addActionListener(event -> findNext(replaceFindField.getText()));
        replace.addActionListener(event -> replaceCurrent());
        replaceAll.addActionListener(event -> replaceAll());
        cancel.addActionListener(event -> setVisible(false));
        return panel;
    }

    private void syncFindText() {
        String selected = editor.getSelectedText();
        if (selected != null && !selected.isEmpty() && !selected.contains("\n")) {
            findField.setText(selected);
            replaceFindField.setText(selected);
            return;
        }
        if (!findField.getText().isEmpty()) {
            replaceFindField.setText(findField.getText());
        } else if (!replaceFindField.getText().isEmpty()) {
            findField.setText(replaceFindField.getText());
        }
    }

    private boolean findNext(String needle) {
        if (needle == null || needle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入查找内容。", "查找", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        String haystack = editor.getText();
        SearchText searchText = new SearchText(haystack, needle, matchCaseBox.isSelected());
        int start = Math.max(editor.getSelectionEnd(), editor.getCaretPosition());
        int index = searchText.indexOf(start);
        if (index < 0 && start > 0) {
            index = searchText.indexOf(0);
        }

        if (index < 0) {
            JOptionPane.showMessageDialog(this, "没有找到 \"" + needle + "\"。", "查找", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        editor.requestFocusInWindow();
        editor.select(index, index + needle.length());
        return true;
    }

    private void replaceCurrent() {
        String needle = replaceFindField.getText();
        if (needle == null || needle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入查找内容。", "替换", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String selected = editor.getSelectedText();
        if (selected == null || !matches(selected, needle)) {
            if (!findNext(needle)) {
                return;
            }
        }
        editor.replaceSelection(replaceField.getText());
        findNext(needle);
    }

    private void replaceAll() {
        String needle = replaceFindField.getText();
        if (needle == null || needle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入查找内容。", "替换", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String replacement = replaceField.getText();
        String source = editor.getText();
        SearchText searchText = new SearchText(source, needle, matchCaseBox.isSelected());
        StringBuilder result = new StringBuilder(source.length());
        int count = 0;
        int cursor = 0;
        int index = searchText.indexOf(0);
        while (index >= 0) {
            result.append(source, cursor, index).append(replacement);
            cursor = index + needle.length();
            count++;
            index = searchText.indexOf(cursor);
        }
        result.append(source.substring(cursor));

        if (count == 0) {
            JOptionPane.showMessageDialog(this, "没有找到 \"" + needle + "\"。", "替换", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        editor.setText(result.toString());
        editor.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, "已替换 " + count + " 处。", "替换", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean matches(String selected, String needle) {
        if (matchCaseBox.isSelected()) {
            return selected.equals(needle);
        }
        return selected.equalsIgnoreCase(needle);
    }

    private static final class SearchText {
        private final String haystack;
        private final String needle;

        private SearchText(String haystack, String needle, boolean matchCase) {
            this.haystack = matchCase ? haystack : haystack.toLowerCase();
            this.needle = matchCase ? needle : needle.toLowerCase();
        }

        private int indexOf(int fromIndex) {
            return haystack.indexOf(needle, Math.max(0, fromIndex));
        }
    }
}
