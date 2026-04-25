import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GuessGamePanel {
    public static JComponent createContentPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setPreferredSize(new Dimension(520, 300));
        root.setBorder(BorderFactory.createEmptyBorder(26, 34, 26, 34));
        root.setBackground(Color.WHITE);

        JLabel title = new JLabel("GUIGame：对话框版猜数字游戏");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(new Color(128, 55, 32));
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 6, 6));
        center.setOpaque(false);
        center.add(new JLabel("抽象类 GuessGame 负责游戏流程。"));
        center.add(new JLabel("GUIGame 只负责用 JOptionPane 完成输入、输出和继续确认。"));
        center.add(new JLabel("当前提示：第 3 次：42 太小了。"));
        JTextField sampleInput = new JTextField("58");
        center.add(sampleInput);
        root.add(center, BorderLayout.CENTER);

        JButton submit = new JButton("提交猜测");
        root.add(submit, BorderLayout.SOUTH);
        return root;
    }
}
