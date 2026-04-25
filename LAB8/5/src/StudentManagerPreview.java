import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class StudentManagerPreview {
    public static JComponent createContentPanel() {
        StudentModel model = new StudentModel();
        StudentTableModel tableModel = new StudentTableModel(new ArrayList<>(model.getStudents()));
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setPreferredSize(new Dimension(760, 500));
        root.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));
        root.setBackground(Color.WHITE);

        JLabel title = new JLabel("Student Manager - MVC JTable");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(new Color(61, 96, 142));
        root.add(title, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setOpaque(false);
        JPanel editor = new JPanel(new GridBagLayout());
        editor.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addField(editor, gbc, 0, "学号", new JTextField("22920242203268", 14));
        addField(editor, gbc, 1, "姓名", new JTextField("赵六", 8));
        addField(editor, gbc, 2, "专业", new JTextField("软件工程", 10));
        addField(editor, gbc, 3, "成绩", new JTextField("88", 5));

        JPanel commands = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        commands.setOpaque(false);
        commands.add(new JLabel("新增"));
        commands.add(new JLabel("修改"));
        commands.add(new JLabel("删除"));
        commands.add(new JLabel("查找：22920242203267"));
        commands.add(new JLabel("示例数据已加载，平均分：" + String.format("%.1f", model.averageScore())));

        bottom.add(editor, BorderLayout.CENTER);
        bottom.add(commands, BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private static void addField(JPanel panel, GridBagConstraints gbc, int col, String label, JTextField field) {
        gbc.gridx = col * 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel(label + "："), gbc);
        gbc.gridx = col * 2 + 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }
}
