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

    /**
     * JavaFX 程序入口。
     * <p>
     * 调用 {@code launch(args)} 后，
     * JavaFX 会自动创建应用实例，并回调本类的 {@code start()} 方法。
     *
     * @param args 命令行参数，本实验中不会使用
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * JavaFX 启动后首先执行的方法。
     * <p>
     * 这里负责创建主窗口、设置根布局、设置场景大小和标题，
     * 最后把窗口显示出来。
     *
     * @param primaryStage 主舞台，也就是程序主窗口
     */
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

    /**
     * 创建窗口顶部标题区域。
     * <p>
     * 顶部区域只负责展示程序名称和提示说明，
     * 不参与输入和结果计算。
     *
     * @return 顶部标题区域对应的节点
     */
    private Node createHeader() {
        Label titleLabel = new Label("学生选课系统");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label descLabel = new Label("请输入学生信息，并从 5 门课程中至少选择 4 门。");
        descLabel.setStyle("-fx-font-size: 13px;");

        VBox headerBox = new VBox(6, titleLabel, descLabel);
        headerBox.setPadding(new Insets(0, 0, 14, 0));
        return headerBox;
    }

    /**
     * 创建窗口中间的主体内容区域。
     * <p>
     * 主体内容分为左右两部分：
     * - 左边是学生信息输入和课程勾选；
     * - 右边是选课结果和成绩显示。
     *
     * @return 主体内容区域对应的节点
     */
    private Node createMainContent() {
        VBox inputPane = createInputPane();
        VBox resultPane = createResultPane();
        HBox.setHgrow(inputPane, Priority.ALWAYS);
        HBox.setHgrow(resultPane, Priority.ALWAYS);

        HBox container = new HBox(18, inputPane, resultPane);
        container.setAlignment(Pos.TOP_LEFT);
        return container;
    }

    /**
     * 创建左侧输入区域。
     * <p>
     * 这里会初始化全部文本框、课程复选框以及按钮，
     * 并把“确认选课”和“生成成绩”的点击事件绑定到对应方法上。
     *
     * @return 左侧输入面板
     */
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

    /**
     * 创建右侧结果显示区域。
     * <p>
     * 这个区域主要是一个只读文本框，
     * 用来显示学生信息、选课结果和随机生成的成绩。
     *
     * @return 右侧结果面板
     */
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

    /**
     * 处理“确认选课”按钮的点击事件。
     * <p>
     * 处理流程：
     * - 先读取并校验学生信息；
     * - 再检查是否至少选了 4 门课；
     * - 校验通过后创建学生对象并保存当前已选课程；
     * - 最后把选课结果显示到右侧文本框。
     */
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

    /**
     * 处理“生成成绩”按钮的点击事件。
     * <p>
     * 只有在已经确认选课之后，才能生成成绩。
     * 生成完成后，会立刻刷新右侧文本区域。
     */
    private void handleGenerateScores() {
        if (currentStudent == null || selectedCourses.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "尚未确认选课", "请先填写信息并确认选课。");
            return;
        }

        currentScores = createScores(selectedCourses, currentStudent);
        resultArea.setText(buildResultText());
        showAlert(Alert.AlertType.INFORMATION, "成绩已生成", "已为所选课程随机生成 80 到 100 分的成绩。");
    }

    /**
     * 收集当前被勾选的课程。
     * <p>
     * 本方法会遍历“课程对象 -> 复选框”的映射表，
     * 把所有被用户选中的课程提取出来并返回。
     *
     * @return 当前选中的课程列表
     */
    private List<Course> collectSelectedCourses() {
        List<Course> courses = new ArrayList<>();
        for (Map.Entry<Course, CheckBox> entry : courseCheckBoxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                courses.add(entry.getKey());
            }
        }
        return courses;
    }

    /**
     * 为当前所选课程随机生成成绩。
     * <p>
     * 每门课会生成一个 80 到 100 之间的整数分数，
     * 并封装成一个 {@code Score} 对象保存下来。
     *
     * @param courses 已选择的课程列表
     * @param student 当前学生对象
     * @return 生成好的成绩列表
     */
    private List<Score> createScores(List<Course> courses, Student student) {
        List<Score> scores = new ArrayList<>();
        for (Course course : courses) {
            int value = 80 + random.nextInt(21);
            scores.add(new Score(course.getCourseId(), student.getStudentId(), value));
        }
        return scores;
    }

    /**
     * 生成右侧文本框要显示的完整内容。
     * <p>
     * 输出顺序固定为：
     * - 学生信息；
     * - 选课结果；
     * - 课程成绩。
     * 如果成绩还没有生成，则给出“暂未生成成绩”的提示。
     *
     * @return 要显示在文本框中的完整字符串
     */
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

    /**
     * 根据课程编号查找对应成绩。
     * <p>
     * 因为成绩列表里保存的是多个 {@code Score} 对象，
     * 所以这里需要通过课程编号逐个匹配，找到对应分数。
     *
     * @param courseId 要查找的课程编号
     * @return 找到则返回分数，找不到则返回 0
     */
    private int findScoreByCourseId(String courseId) {
        for (Score score : currentScores) {
            if (score.getCourseId().equals(courseId)) {
                return score.getScore();
            }
        }
        return 0;
    }

    /**
     * 弹出一个提示框。
     * <p>
     * 这个方法统一负责所有成功或警告消息的弹窗显示，
     * 这样界面代码就不需要在各个位置重复创建 Alert 对象。
     *
     * @param type 弹窗类型，例如信息框或警告框
     * @param title 弹窗标题
     * @param content 弹窗正文内容
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * 创建系统内置的课程列表。
     * <p>
     * 题目要求的 5 门课程会在这里统一初始化，
     * 后续界面中的复选框和成绩生成都依赖这份课程目录。
     *
     * @return 内置课程列表
     */
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
