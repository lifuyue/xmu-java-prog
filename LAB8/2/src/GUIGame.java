import javax.swing.JOptionPane;

/**
 * GUI 版猜数字：用 JOptionPane 实现输入、提示和继续确认。
 */
public class GUIGame extends GuessGame {
    @Override
    protected Integer readGuess(String prompt) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, prompt, "猜数字游戏", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                // 用户关闭输入框时，同样用 null 告诉父类取消本轮。
                return null;
            }
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "请输入有效整数。", "输入错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "猜数字游戏", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected boolean askContinue() {
        int result = JOptionPane.showConfirmDialog(null, "是否再玩一局？", "继续游戏", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
