import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class StudentView extends JFrame {
    private final JTextField idField = new JTextField(14);
    private final JTextField nameField = new JTextField(8);
    private final JTextField majorField = new JTextField(10);
    private final JTextField scoreField = new JTextField(5);
    private final JTextField searchField = new JTextField(12);
    private final JLabel statusLabel = new JLabel("就绪");
    private final JTable table;
    private final JButton addButton = new JButton("新增");
    private final JButton updateButton = new JButton("修改");
    private final JButton deleteButton = new JButton("删除");
    private final JButton clearButton = new JButton("清空");
    private final JButton searchButton = new JButton("按学号查找");

    public StudentView(StudentTableModel tableModel) {
        super("LAB8-5 学生管理系统");
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setContentPane(createContentPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public JComponent createContentPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setPreferredSize(new Dimension(760, 500));
        root.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));
        root.setBackground(Color.WHITE);

        JLabel title = new JLabel("学生管理系统（MVC + JTable）");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(new Color(61, 96, 142));
        root.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 219, 232)));
        root.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setOpaque(false);
        bottom.add(createEditorPanel(), BorderLayout.CENTER);
        bottom.add(createCommandPanel(), BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private JPanel createEditorPanel() {
        JPanel editor = new JPanel(new GridBagLayout());
        editor.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addField(editor, gbc, 0, "学号", idField);
        addField(editor, gbc, 1, "姓名", nameField);
        addField(editor, gbc, 2, "专业", majorField);
        addField(editor, gbc, 3, "成绩", scoreField);
        return editor;
    }

    private JPanel createCommandPanel() {
        JPanel commands = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        commands.setOpaque(false);
        commands.add(addButton);
        commands.add(updateButton);
        commands.add(deleteButton);
        commands.add(clearButton);
        commands.add(new JLabel("查找："));
        commands.add(searchField);
        commands.add(searchButton);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
        commands.add(statusLabel);
        return commands;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int col, String label, JTextField field) {
        gbc.gridx = col * 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel(label + "："), gbc);
        gbc.gridx = col * 2 + 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    public JTextField getIdField() {
        return idField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getMajorField() {
        return majorField;
    }

    public JTextField getScoreField() {
        return scoreField;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }
}
