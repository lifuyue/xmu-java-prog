import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * 第 5 题入口：在 Swing 事件线程中组装 Model、TableModel、View 和 Controller。
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentModel model = new StudentModel();
            StudentTableModel tableModel = new StudentTableModel(new ArrayList<>(model.getStudents()));
            StudentView view = new StudentView(tableModel);
            // Controller 绑定按钮和表格选择事件，是界面与数据模型之间的连接层。
            new StudentController(model, tableModel, view);
            view.setVisible(true);
        });
    }
}
