import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class EventDemoApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("LAB8-3 事件处理示例");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(createContentPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static JComponent createContentPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setPreferredSize(new Dimension(560, 360));
        root.setBorder(BorderFactory.createEmptyBorder(26, 34, 26, 34));
        root.setBackground(Color.WHITE);

        JLabel title = new JLabel("事件源、事件对象与监听器");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(new Color(24, 120, 96));
        root.add(title, BorderLayout.NORTH);

        JLabel display = new JLabel("点击按钮或移动鼠标查看事件反馈", SwingConstants.CENTER);
        display.setOpaque(true);
        display.setBackground(new Color(241, 248, 246));
        display.setBorder(BorderFactory.createLineBorder(new Color(159, 204, 191)));
        display.setFont(display.getFont().deriveFont(Font.PLAIN, 16f));
        root.add(display, BorderLayout.CENTER);

        JPanel controls = new JPanel(new GridLayout(2, 2, 10, 10));
        controls.setOpaque(false);
        JButton countButton = new JButton("点击计数");
        JButton resetButton = new JButton("重置");
        JComboBox<String> colorBox = new JComboBox<>(new String[] {"浅绿", "浅蓝", "浅黄"});
        JLabel countLabel = new JLabel("点击次数：0");
        final int[] count = {0};

        countButton.addActionListener(event -> {
            count[0]++;
            countLabel.setText("点击次数：" + count[0]);
            display.setText("ActionEvent 来自按钮：" + event.getActionCommand());
        });
        resetButton.addActionListener(event -> {
            count[0] = 0;
            countLabel.setText("点击次数：0");
            display.setText("已重置事件状态。");
        });
        colorBox.addActionListener(event -> {
            String color = (String) colorBox.getSelectedItem();
            if ("浅蓝".equals(color)) {
                display.setBackground(new Color(235, 244, 255));
            } else if ("浅黄".equals(color)) {
                display.setBackground(new Color(255, 249, 224));
            } else {
                display.setBackground(new Color(241, 248, 246));
            }
            display.setText("Item/Action 事件改变了显示区域颜色：" + color);
        });
        display.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                display.setText("MouseEvent：鼠标进入显示区域。");
            }

            @Override
            public void mouseExited(MouseEvent event) {
                display.setText("MouseEvent：鼠标离开显示区域。");
            }
        });

        controls.add(countButton);
        controls.add(resetButton);
        controls.add(colorBox);
        controls.add(countLabel);
        root.add(controls, BorderLayout.SOUTH);
        return root;
    }
}
