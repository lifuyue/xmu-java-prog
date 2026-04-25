import java.util.ArrayList;
import javax.swing.JOptionPane;

public class StudentController {
    private final StudentModel model;
    private final StudentTableModel tableModel;
    private final StudentView view;

    public StudentController(StudentModel model, StudentTableModel tableModel, StudentView view) {
        this.model = model;
        this.tableModel = tableModel;
        this.view = view;
        bindEvents();
        refresh();
    }

    private void bindEvents() {
        view.getAddButton().addActionListener(event -> handleAdd());
        view.getUpdateButton().addActionListener(event -> handleUpdate());
        view.getDeleteButton().addActionListener(event -> handleDelete());
        view.getClearButton().addActionListener(event -> clearFields());
        view.getSearchButton().addActionListener(event -> handleSearch());
        view.getTable().getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });
    }

    private void handleAdd() {
        try {
            model.addStudent(readStudentFromFields());
            refresh();
            clearFields();
            view.setStatus("新增成功，当前平均分：" + String.format("%.1f", model.averageScore()));
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void handleUpdate() {
        int row = view.getTable().getSelectedRow();
        try {
            model.updateStudent(row, readStudentFromFields());
            refresh();
            view.setStatus("修改成功。");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void handleDelete() {
        int row = view.getTable().getSelectedRow();
        try {
            model.removeStudent(row);
            refresh();
            clearFields();
            view.setStatus("删除成功。");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void handleSearch() {
        String id = view.getSearchField().getText().trim();
        Student student = model.findById(id);
        if (student == null) {
            tableModel.setStudents(new ArrayList<>(model.getStudents()));
            view.setStatus("未找到学号：" + id);
            return;
        }
        ArrayList<Student> result = new ArrayList<>();
        result.add(student);
        tableModel.setStudents(result);
        view.setStatus("已找到：" + student.getName());
    }

    private Student readStudentFromFields() {
        int score;
        try {
            score = Integer.parseInt(view.getScoreField().getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("成绩必须是整数。");
        }
        return new Student(
                view.getIdField().getText(),
                view.getNameField().getText(),
                view.getMajorField().getText(),
                score);
    }

    private void fillFieldsFromSelectedRow() {
        int row = view.getTable().getSelectedRow();
        if (row < 0 || row >= tableModel.getRowCount()) {
            return;
        }
        view.getIdField().setText(String.valueOf(tableModel.getValueAt(row, 0)));
        view.getNameField().setText(String.valueOf(tableModel.getValueAt(row, 1)));
        view.getMajorField().setText(String.valueOf(tableModel.getValueAt(row, 2)));
        view.getScoreField().setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private void clearFields() {
        view.getIdField().setText("");
        view.getNameField().setText("");
        view.getMajorField().setText("");
        view.getScoreField().setText("");
        view.getSearchField().setText("");
        tableModel.setStudents(new ArrayList<>(model.getStudents()));
        view.getTable().clearSelection();
        view.setStatus("就绪");
    }

    private void refresh() {
        tableModel.setStudents(new ArrayList<>(model.getStudents()));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "操作失败", JOptionPane.ERROR_MESSAGE);
        view.setStatus("操作失败：" + message);
    }
}
