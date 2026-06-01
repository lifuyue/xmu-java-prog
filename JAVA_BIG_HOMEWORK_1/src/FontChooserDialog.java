import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class FontChooserDialog extends JDialog {
    private final JComboBox<String> familyBox;
    private final JComboBox<FontStyleItem> styleBox;
    private final JSpinner sizeSpinner;
    private final JTextArea previewArea;
    private Font selectedFont;

    private FontChooserDialog(Frame owner, Font currentFont) {
        super(owner, "字体", true);
        String[] families = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        familyBox = new JComboBox<>(families);
        styleBox = new JComboBox<>(new FontStyleItem[]{
                new FontStyleItem("常规", Font.PLAIN),
                new FontStyleItem("粗体", Font.BOLD),
                new FontStyleItem("斜体", Font.ITALIC),
                new FontStyleItem("粗斜体", Font.BOLD | Font.ITALIC)
        });
        sizeSpinner = new JSpinner(new SpinnerNumberModel(currentFont.getSize(), 8, 72, 1));
        previewArea = new JTextArea("22920242203267 李富悦\nAI 记事本字体预览");
        previewArea.setRows(4);
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        selectedFont = currentFont;

        familyBox.setSelectedItem(currentFont.getFamily());
        selectStyle(currentFont.getStyle());
        buildLayout();
        addListeners();
        updatePreview();

        setSize(520, 360);
        setLocationRelativeTo(owner);
    }

    public static Font showDialog(Component parent, Font currentFont) {
        Frame owner = parent instanceof Frame ? (Frame) parent : null;
        FontChooserDialog dialog = new FontChooserDialog(owner, currentFont);
        dialog.setVisible(true);
        return dialog.selectedFont;
    }

    static FontChooserDialog createForScreenshot(Frame owner, Font currentFont) {
        FontChooserDialog dialog = new FontChooserDialog(owner, currentFont);
        dialog.setModal(false);
        return dialog;
    }

    private void buildLayout() {
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        form.add(new JLabel("字体:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(familyBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        form.add(new JLabel("字形:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(styleBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        form.add(new JLabel("大小:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(sizeSpinner, gbc);

        content.add(form, BorderLayout.NORTH);

        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setBorder(BorderFactory.createTitledBorder("预览"));
        content.add(previewScroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttons.add(okButton);
        buttons.add(cancelButton);
        content.add(buttons, BorderLayout.SOUTH);

        okButton.addActionListener(event -> {
            selectedFont = buildSelectedFont();
            dispose();
        });
        cancelButton.addActionListener(event -> {
            selectedFont = null;
            dispose();
        });
        getRootPane().setDefaultButton(okButton);
    }

    private void addListeners() {
        familyBox.addActionListener(event -> updatePreview());
        styleBox.addActionListener(event -> updatePreview());
        sizeSpinner.addChangeListener(event -> updatePreview());
    }

    private void selectStyle(int style) {
        for (int i = 0; i < styleBox.getItemCount(); i++) {
            if (styleBox.getItemAt(i).style == style) {
                styleBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private Font buildSelectedFont() {
        String family = (String) familyBox.getSelectedItem();
        FontStyleItem style = (FontStyleItem) styleBox.getSelectedItem();
        int size = (Integer) sizeSpinner.getValue();
        return new Font(family == null ? Font.MONOSPACED : family, style == null ? Font.PLAIN : style.style, size);
    }

    private void updatePreview() {
        previewArea.setFont(buildSelectedFont());
    }

    private static final class FontStyleItem {
        private final String label;
        private final int style;

        private FontStyleItem(String label, int style) {
            this.label = label;
            this.style = style;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
