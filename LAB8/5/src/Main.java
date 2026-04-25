import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentModel model = new StudentModel();
            StudentTableModel tableModel = new StudentTableModel(new ArrayList<>(model.getStudents()));
            StudentView view = new StudentView(tableModel);
            new StudentController(model, tableModel, view);
            view.setVisible(true);
        });
    }
}
