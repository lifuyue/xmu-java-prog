import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 简单选课系统图形界面。
 */
public class CourseSelectionApp extends Application {
    private final List<Course> courseCatalog = createCourseCatalog();
    private final Map<Course, CheckBox> courseCheckBoxes = new LinkedHashMap<>();
    private final SecureRandom random = new SecureRandom();

    private Student currentStudent;
    private List<Course> selectedCourses = new ArrayList<>();
    private List<Score> currentScores = new ArrayList<>();

    private TextField studentIdField;
    private TextField nameField;
    private TextField classField;
    private TextField phoneField;
    private TextArea resultArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.setTop(createHeader());
        root.setCenter(createMainContent());

        Scene scene = new Scene(root, 780, 520);
        primaryStage.setTitle("LAB3 简单选课系统");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Node createHeader() {
        Label titleLabel = new Label("学生选课系统");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label descLabel = new Label("请输入学生信息，并从 5 门课程中至少选择 4 门。");
        descLabel.setStyle("-fx-font-size: 13px;");

        VBox headerBox = new VBox(6, titleLabel, descLabel);
        headerBox.setPadding(new Insets(0, 0, 14, 0));
        return headerBox;
    }

    private Node createMainContent() {
        VBox inputPane = createInputPane();
        VBox resultPane = createResultPane();
        HBox.setHgrow(inputPane, Priority.ALWAYS);
        HBox.setHgrow(resultPane, Priority.ALWAYS);

        HBox container = new HBox(18, inputPane, resultPane);
        container.setAlignment(Pos.TOP_LEFT);
        return container;
    }

    private VBox createInputPane() {
        studentIdField = new TextField();
        nameField = new TextField();
        classField = new TextField();
        phoneField = new TextField();

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(10);
        infoGrid.add(new Label("学号："), 0, 0);
        infoGrid.add(studentIdField, 1, 0);
        infoGrid.add(new Label("姓名："), 0, 1);
        infoGrid.add(nameField, 1, 1);
        infoGrid.add(new Label("班级："), 0, 2);
        infoGrid.add(classField, 1, 2);
        infoGrid.add(new Label("电话："), 0, 3);
        infoGrid.add(phoneField, 1, 3);

        Label courseLabel = new Label("请选择课程：");
        courseLabel.setStyle("-fx-font-weight: bold;");

        VBox courseBox = new VBox(8);
        for (Course course : courseCatalog) {
            CheckBox checkBox = new CheckBox(course.getCourseName() + "（" + course.getCourseId() + "）");
            courseCheckBoxes.put(course, checkBox);
            courseBox.getChildren().add(checkBox);
        }

        Button confirmButton = new Button("确认选课");
        confirmButton.setOnAction(event -> handleConfirmSelection());

        Button scoreButton = new Button("生成成绩");
        scoreButton.setOnAction(event -> handleGenerateScores());

        HBox buttonBox = new HBox(12, confirmButton, scoreButton);

        VBox inputPane = new VBox(14, infoGrid, courseLabel, courseBox, buttonBox);
        inputPane.setPadding(new Insets(14));
        inputPane.setStyle("-fx-border-color: #c9c9c9; -fx-border-radius: 6; -fx-background-radius: 6;");
        inputPane.setPrefWidth(340);
        return inputPane;
    }

    private VBox createResultPane() {
        Label resultLabel = new Label("选课结果与成绩");
        resultLabel.setStyle("-fx-font-weight: bold;");

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPromptText("确认选课后，这里会显示学生信息、选课结果和课程成绩。");
        VBox.setVgrow(resultArea, Priority.ALWAYS);

        VBox resultPane = new VBox(10, resultLabel, resultArea);
        resultPane.setPadding(new Insets(14));
        resultPane.setStyle("-fx-border-color: #c9c9c9; -fx-border-radius: 6; -fx-background-radius: 6;");
        resultPane.setPrefWidth(400);
        return resultPane;
    }

    private void handleConfirmSelection() {
        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();
        String className = classField.getText().trim();
        String phone = phoneField.getText().trim();

        if (studentId.isEmpty() || name.isEmpty() || className.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "输入不完整", "学号、姓名和班级不能为空。");
            return;
        }

        List<Course> chosenCourses = collectSelectedCourses();
        if (chosenCourses.size() < 4) {
            showAlert(Alert.AlertType.WARNING, "选课数量不足", "请至少选择 4 门课程。");
            return;
        }

        currentStudent = phone.isEmpty()
                ? new Student(studentId, name, className)
                : new Student(studentId, name, className, phone);
        selectedCourses = chosenCourses;
        currentScores = new ArrayList<>();

        resultArea.setText(buildResultText());
        showAlert(Alert.AlertType.INFORMATION, "选课成功", "已记录学生信息并显示选课结果。");
    }

    private void handleGenerateScores() {
        if (currentStudent == null || selectedCourses.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "尚未确认选课", "请先填写信息并确认选课。");
            return;
        }

        currentScores = createScores(selectedCourses, currentStudent);
        resultArea.setText(buildResultText());
        showAlert(Alert.AlertType.INFORMATION, "成绩已生成", "已为所选课程随机生成 80 到 100 分的成绩。");
    }

    private List<Course> collectSelectedCourses() {
        List<Course> courses = new ArrayList<>();
        for (Map.Entry<Course, CheckBox> entry : courseCheckBoxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                courses.add(entry.getKey());
            }
        }
        return courses;
    }

    private List<Score> createScores(List<Course> courses, Student student) {
        List<Score> scores = new ArrayList<>();
        for (Course course : courses) {
            int value = 80 + random.nextInt(21);
            scores.add(new Score(course.getCourseId(), student.getStudentId(), value));
        }
        return scores;
    }

    private String buildResultText() {
        if (currentStudent == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("学生信息").append('\n');
        builder.append("学号：").append(currentStudent.getStudentId()).append('\n');
        builder.append("姓名：").append(currentStudent.getName()).append('\n');
        builder.append("班级：").append(currentStudent.getClassName()).append('\n');
        builder.append("电话：")
                .append(currentStudent.getPhone().isEmpty() ? "未填写" : currentStudent.getPhone())
                .append('\n')
                .append('\n');

        builder.append("选课结果").append('\n');
        for (Course course : selectedCourses) {
            builder.append(course.getCourseName())
                    .append("（")
                    .append(course.getCourseId())
                    .append("）")
                    .append('\n');
        }

        builder.append('\n').append("课程成绩").append('\n');
        if (currentScores.isEmpty()) {
            builder.append("暂未生成成绩。");
        } else {
            for (Course course : selectedCourses) {
                builder.append(course.getCourseName())
                        .append("：")
                        .append(findScoreByCourseId(course.getCourseId()))
                        .append(" 分")
                        .append('\n');
            }
        }

        return builder.toString();
    }

    private int findScoreByCourseId(String courseId) {
        for (Score score : currentScores) {
            if (score.getCourseId().equals(courseId)) {
                return score.getScore();
            }
        }
        return 0;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private List<Course> createCourseCatalog() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("C001", "大学英语"));
        courses.add(new Course("C002", "Java程序设计"));
        courses.add(new Course("C003", "操作系统"));
        courses.add(new Course("C004", "数据库"));
        courses.add(new Course("C005", "C语言"));
        return courses;
    }
}
