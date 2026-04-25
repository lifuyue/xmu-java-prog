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
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginFrameApp {
    private static final Color BLUE = new Color(35, 94, 173);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("LAB8-1 登录框界面");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(createContentPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static JComponent createContentPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setPreferredSize(new Dimension(520, 340));
        root.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));
        root.setBackground(Color.WHITE);

        JLabel title = new JLabel("Java GUI 登录示例");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(BLUE);
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("用户名：");
        JTextField userField = new JTextField("李富悦", 18);
        JLabel passwordLabel = new JLabel("密码：");
        JPasswordField passwordField = new JPasswordField("java-lab8", 18);
        JCheckBox remember = new JCheckBox("记住用户名", true);
        remember.setOpaque(false);
        JLabel status = new JLabel("请输入用户名和密码后登录。");
        status.setForeground(new Color(80, 80, 80));

        addRow(form, gbc, 0, userLabel, userField);
        addRow(form, gbc, 1, passwordLabel, passwordField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        form.add(remember, gbc);
        gbc.gridy = 3;
        form.add(status, gbc);

        root.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        JButton clearButton = new JButton("清空");
        JButton loginButton = new JButton("登录");
        loginButton.setBackground(BLUE);
        loginButton.setForeground(Color.WHITE);

        clearButton.addActionListener(event -> {
            userField.setText("");
            passwordField.setText("");
            status.setText("表单已清空。");
        });
        loginButton.addActionListener(event -> {
            String name = userField.getText().trim();
            if (name.isEmpty() || passwordField.getPassword().length == 0) {
                status.setText("用户名和密码不能为空。");
            } else {
                status.setText("欢迎，" + name + "。登录信息已提交。");
            }
        });

        buttons.add(clearButton);
        buttons.add(loginButton);
        root.add(buttons, BorderLayout.SOUTH);
        return root;
    }

    private static void addRow(
            JPanel form,
            GridBagConstraints gbc,
            int row,
            JLabel label,
            JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        label.setHorizontalAlignment(JLabel.RIGHT);
        form.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(field, gbc);
    }
}
