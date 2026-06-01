import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * JTable 适配器：把 List<Student> 转成表格需要的行、列和值。
 */
public class StudentTableModel extends AbstractTableModel {
    private final String[] columns = {"学号", "姓名", "专业", "成绩"};
    private List<Student> students;

    public StudentTableModel(List<Student> students) {
        this.students = students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
        // 通知 JTable 数据已经变化，界面会自动重绘。
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> student.getId();
            case 1 -> student.getName();
            case 2 -> student.getMajor();
            case 3 -> student.getScore();
            default -> "";
        };
    }
}
