package com.example.demo;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class ManagementPane extends BorderPane {
    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private final ObservableList<Enrollment> enrollments = FXCollections.observableArrayList();

    private final BorderPane workspace = new BorderPane();
    private TableView<Student> studentTable;
    private TableView<Course> courseTable;
    private TableView<Enrollment> enrollmentTable;

    public ManagementPane() {
        loadSampleData();
        setLeft(createMenuPanel());
        setCenter(workspace);
        showStudentManagement();
    }

    private VBox createMenuPanel() {
        VBox menuBox = new VBox(12);
        menuBox.setPadding(new Insets(22, 12, 22, 12));
        menuBox.setPrefWidth(150);
        menuBox.setStyle("-fx-background-color: #f4f7fb; -fx-border-color: #ccd5e1; -fx-border-width: 0 1 0 0;");

        Label titleLabel = new Label("系统菜单");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button studentButton = menuButton("学生管理");
        studentButton.setOnAction(event -> showStudentManagement());

        Button courseButton = menuButton("课程管理");
        courseButton.setOnAction(event -> showCourseManagement());

        Button enrollmentButton = menuButton("选课管理");
        enrollmentButton.setOnAction(event -> showEnrollmentManagement());

        menuBox.getChildren().addAll(titleLabel, studentButton, courseButton, enrollmentButton);
        return menuBox;
    }

    private Button menuButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 9 10;");
        return button;
    }

    private void showStudentManagement() {
        TextField idField = new TextField();
        idField.setPromptText("学号");
        TextField phoneField = new TextField();
        phoneField.setPromptText("电话");
        TextField classField = new TextField();
        classField.setPromptText("班级");
        ComboBox<String> politicsCombo = new ComboBox<>();
        politicsCombo.getItems().addAll("全部", "中共党员", "中共预备党员", "共青团员", "群众");
        politicsCombo.setValue("全部");

        FilteredList<Student> filteredStudents = new FilteredList<>(students, student -> true);
        studentTable = new TableView<>(filteredStudents);
        studentTable.getColumns().addAll(
                textColumn("学号", 110, Student::getStudentId),
                textColumn("姓名", 90, Student::getName),
                textColumn("电话", 130, Student::getPhone),
                textColumn("班级", 130, Student::getClassName),
                textColumn("政治面貌", 130, Student::getPoliticsStatus),
                textColumn("邮箱", 190, Student::getEmail)
        );
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        Button searchButton = new Button("查询");
        searchButton.setOnAction(event -> filteredStudents.setPredicate(student ->
                contains(student.getStudentId(), idField.getText())
                        && contains(student.getPhone(), phoneField.getText())
                        && contains(student.getClassName(), classField.getText())
                        && ("全部".equals(politicsCombo.getValue())
                        || student.getPoliticsStatus().equals(politicsCombo.getValue()))));

        Button resetButton = new Button("重置");
        resetButton.setOnAction(event -> {
            idField.clear();
            phoneField.clear();
            classField.clear();
            politicsCombo.setValue("全部");
            filteredStudents.setPredicate(student -> true);
        });

        GridPane searchPanel = grid();
        searchPanel.addRow(0, new Label("学号:"), idField, new Label("电话:"), phoneField, new Label("班级:"), classField);
        searchPanel.addRow(1, new Label("政治面貌:"), politicsCombo, new Label(), searchButton, resetButton);

        VBox view = page("学生信息查询", searchPanel, studentTable);
        workspace.setCenter(view);
    }

    private void showCourseManagement() {
        FilteredList<Course> filteredCourses = new FilteredList<>(courses, course -> true);
        courseTable = new TableView<>(filteredCourses);
        courseTable.getColumns().addAll(
                textColumn("课程号", 100, Course::getCourseId),
                textColumn("课程名称", 190, Course::getCourseName),
                textColumn("学分", 80, Course::getCredit),
                textColumn("任课教师", 110, Course::getTeacher),
                textColumn("课程类型", 110, Course::getCategory),
                textColumn("上课时间", 160, Course::getSchedule)
        );
        courseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TextField filterField = new TextField();
        filterField.setPromptText("输入课程号、课程名或教师查询");
        filterField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredCourses.setPredicate(course -> contains(course.getCourseId(), newValue)
                        || contains(course.getCourseName(), newValue)
                        || contains(course.getTeacher(), newValue)));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField creditField = new TextField();
        TextField teacherField = new TextField();
        TextField categoryField = new TextField();
        TextField scheduleField = new TextField();

        GridPane form = grid();
        form.addRow(0, new Label("课程号:"), idField, new Label("课程名称:"), nameField, new Label("学分:"), creditField);
        form.addRow(1, new Label("任课教师:"), teacherField, new Label("课程类型:"), categoryField, new Label("上课时间:"), scheduleField);

        Button addButton = new Button("新增课程");
        Button updateButton = new Button("修改课程");
        Button deleteButton = new Button("删除课程");
        Button clearButton = new Button("清空表单");

        addButton.setOnAction(event -> {
            if (isBlank(idField) || isBlank(nameField)) {
                showWarning("课程号和课程名称不能为空。");
                return;
            }
            if (findCourse(idField.getText().trim()) != null) {
                showWarning("课程号已存在，不能重复新增。");
                return;
            }
            courses.add(new Course(
                    idField.getText().trim(),
                    nameField.getText().trim(),
                    valueOrDefault(creditField, "2"),
                    valueOrDefault(teacherField, "待定"),
                    valueOrDefault(categoryField, "专业课"),
                    valueOrDefault(scheduleField, "周一 1-2 节")
            ));
            clearCourseForm(idField, nameField, creditField, teacherField, categoryField, scheduleField);
            refreshEnrollmentTables();
        });

        updateButton.setOnAction(event -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning("请先选择要修改的课程。");
                return;
            }
            selected.setCourseId(valueOrDefault(idField, selected.getCourseId()));
            selected.setCourseName(valueOrDefault(nameField, selected.getCourseName()));
            selected.setCredit(valueOrDefault(creditField, selected.getCredit()));
            selected.setTeacher(valueOrDefault(teacherField, selected.getTeacher()));
            selected.setCategory(valueOrDefault(categoryField, selected.getCategory()));
            selected.setSchedule(valueOrDefault(scheduleField, selected.getSchedule()));
            courseTable.refresh();
            refreshEnrollmentTables();
        });

        deleteButton.setOnAction(event -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning("请先选择要删除的课程。");
                return;
            }
            enrollments.removeIf(enrollment -> enrollment.getCourse() == selected);
            courses.remove(selected);
            clearCourseForm(idField, nameField, creditField, teacherField, categoryField, scheduleField);
            refreshEnrollmentTables();
        });

        clearButton.setOnAction(event -> clearCourseForm(idField, nameField, creditField, teacherField, categoryField, scheduleField));

        courseTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            idField.setText(selected.getCourseId());
            nameField.setText(selected.getCourseName());
            creditField.setText(selected.getCredit());
            teacherField.setText(selected.getTeacher());
            categoryField.setText(selected.getCategory());
            scheduleField.setText(selected.getSchedule());
        });

        HBox actions = new HBox(10, addButton, updateButton, deleteButton, clearButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox view = page("课程管理", filterField, courseTable, form, actions);
        workspace.setCenter(view);
    }

    private void showEnrollmentManagement() {
        FilteredList<Enrollment> filteredEnrollments = new FilteredList<>(enrollments, enrollment -> true);
        enrollmentTable = new TableView<>(filteredEnrollments);
        enrollmentTable.getColumns().addAll(
                textColumn("学号", 100, enrollment -> enrollment.getStudent().getStudentId()),
                textColumn("姓名", 90, enrollment -> enrollment.getStudent().getName()),
                textColumn("课程号", 100, enrollment -> enrollment.getCourse().getCourseId()),
                textColumn("课程名称", 180, enrollment -> enrollment.getCourse().getCourseName()),
                textColumn("成绩", 70, Enrollment::getScore),
                textColumn("状态", 90, Enrollment::getStatus)
        );
        enrollmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TextField filterField = new TextField();
        filterField.setPromptText("输入学号、姓名或课程名查询");
        filterField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredEnrollments.setPredicate(enrollment -> contains(enrollment.getStudent().getStudentId(), newValue)
                        || contains(enrollment.getStudent().getName(), newValue)
                        || contains(enrollment.getCourse().getCourseName(), newValue)));

        ComboBox<Student> studentCombo = new ComboBox<>(students);
        studentCombo.setConverter(studentConverter());
        ComboBox<Course> courseCombo = new ComboBox<>(courses);
        courseCombo.setConverter(courseConverter());
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("已选", "已退课", "已结课");
        statusCombo.setValue("已选");
        TextField scoreField = new TextField();
        scoreField.setPromptText("未录入或 0-100");

        GridPane form = grid();
        form.addRow(0, new Label("学生:"), studentCombo, new Label("课程:"), courseCombo);
        form.addRow(1, new Label("成绩:"), scoreField, new Label("状态:"), statusCombo);

        Label summary = new Label(enrollmentSummary());
        summary.setStyle("-fx-text-fill: #475569;");

        Button addButton = new Button("新增选课");
        Button updateButton = new Button("修改记录");
        Button deleteButton = new Button("删除记录");
        Button clearButton = new Button("清空表单");

        addButton.setOnAction(event -> {
            Student student = studentCombo.getValue();
            Course course = courseCombo.getValue();
            if (student == null || course == null) {
                showWarning("请选择学生和课程。");
                return;
            }
            if (findEnrollment(student, course) != null) {
                showWarning("该学生已经选择了这门课程。");
                return;
            }
            enrollments.add(new Enrollment(student, course, normalizedScore(scoreField), statusCombo.getValue()));
            summary.setText(enrollmentSummary());
        });

        updateButton.setOnAction(event -> {
            Enrollment selected = enrollmentTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning("请先选择要修改的选课记录。");
                return;
            }
            if (studentCombo.getValue() != null) {
                selected.setStudent(studentCombo.getValue());
            }
            if (courseCombo.getValue() != null) {
                selected.setCourse(courseCombo.getValue());
            }
            selected.setScore(normalizedScore(scoreField));
            selected.setStatus(statusCombo.getValue());
            enrollmentTable.refresh();
            summary.setText(enrollmentSummary());
        });

        deleteButton.setOnAction(event -> {
            Enrollment selected = enrollmentTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showWarning("请先选择要删除的选课记录。");
                return;
            }
            enrollments.remove(selected);
            summary.setText(enrollmentSummary());
        });

        clearButton.setOnAction(event -> {
            studentCombo.getSelectionModel().clearSelection();
            courseCombo.getSelectionModel().clearSelection();
            scoreField.clear();
            statusCombo.setValue("已选");
        });

        enrollmentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected == null) {
                return;
            }
            studentCombo.setValue(selected.getStudent());
            courseCombo.setValue(selected.getCourse());
            scoreField.setText(selected.getScore());
            statusCombo.setValue(selected.getStatus());
        });

        HBox actions = new HBox(10, addButton, updateButton, deleteButton, clearButton, summary);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox view = page("选课管理", filterField, enrollmentTable, form, actions);
        workspace.setCenter(view);
    }

    private GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 4;");
        return grid;
    }

    private VBox page(String title, Node... children) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        VBox box = new VBox(12);
        box.setPadding(new Insets(18));
        box.getChildren().add(titleLabel);
        box.getChildren().addAll(children);
        for (Node child : children) {
            if (child instanceof TableView<?>) {
                VBox.setVgrow(child, Priority.ALWAYS);
            }
        }
        return box;
    }

    private <T> TableColumn<T, String> textColumn(String title, double width, ValueGetter<T> getter) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setPrefWidth(width);
        column.setCellValueFactory(data -> new ReadOnlyStringWrapper(getter.get(data.getValue())));
        return column;
    }

    private boolean contains(String source, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }
        return source != null && source.toLowerCase().contains(keyword.trim().toLowerCase());
    }

    private boolean isBlank(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    private String valueOrDefault(TextField field, String defaultValue) {
        return isBlank(field) ? defaultValue : field.getText().trim();
    }

    private void clearCourseForm(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
        if (courseTable != null) {
            courseTable.getSelectionModel().clearSelection();
        }
    }

    private Course findCourse(String courseId) {
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }

    private Enrollment findEnrollment(Student student, Course course) {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent() == student && enrollment.getCourse() == course) {
                return enrollment;
            }
        }
        return null;
    }

    private String normalizedScore(TextField scoreField) {
        String score = scoreField.getText() == null ? "" : scoreField.getText().trim();
        if (score.isEmpty()) {
            return "未录入";
        }
        try {
            int value = Integer.parseInt(score);
            if (value < 0 || value > 100) {
                showWarning("成绩应在 0 到 100 之间，已按未录入处理。");
                return "未录入";
            }
            return String.valueOf(value);
        } catch (NumberFormatException exception) {
            showWarning("成绩必须是整数，已按未录入处理。");
            return "未录入";
        }
    }

    private String enrollmentSummary() {
        return "当前选课记录：" + enrollments.size() + " 条";
    }

    private void refreshEnrollmentTables() {
        if (courseTable != null) {
            courseTable.refresh();
        }
        if (enrollmentTable != null) {
            enrollmentTable.refresh();
        }
    }

    private StringConverter<Student> studentConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Student student) {
                return student == null ? "" : student.getStudentId() + " - " + student.getName();
            }

            @Override
            public Student fromString(String string) {
                return null;
            }
        };
    }

    private StringConverter<Course> courseConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Course course) {
                return course == null ? "" : course.getCourseId() + " - " + course.getCourseName();
            }

            @Override
            public Course fromString(String string) {
                return null;
            }
        };
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("输入提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadSampleData() {
        students.addAll(
                new Student("2024001", "张三", "13800000001", "计算机1班", "中共党员", "zhangsan@example.com"),
                new Student("2024002", "李四", "13800000002", "计算机1班", "共青团员", "lisi@example.com"),
                new Student("2024003", "王芳", "13800000003", "计算机2班", "中共预备党员", "wangfang@example.com"),
                new Student("2024004", "赵强", "13800000004", "软件工程1班", "群众", "zhaoqiang@example.com"),
                new Student("2024005", "孙丽", "13800000005", "软件工程1班", "中共党员", "sunli@example.com"),
                new Student("2024006", "周杰", "13912345678", "计算机2班", "共青团员", "zhoujie@example.com"),
                new Student("2024007", "吴迪", "13788889999", "计算机1班", "群众", "wudi@example.com")
        );

        courses.addAll(
                new Course("JAVA101", "Java程序设计", "3", "陈老师", "专业必修", "周一 1-2 节"),
                new Course("DB202", "数据库系统", "3", "林老师", "专业必修", "周三 3-4 节"),
                new Course("UI305", "人机交互设计", "2", "黄老师", "专业选修", "周五 5-6 节")
        );

        enrollments.addAll(
                new Enrollment(students.get(0), courses.get(0), "92", "已结课"),
                new Enrollment(students.get(1), courses.get(0), "88", "已结课"),
                new Enrollment(students.get(2), courses.get(1), "未录入", "已选"),
                new Enrollment(students.get(4), courses.get(2), "95", "已选")
        );
    }

    @FunctionalInterface
    private interface ValueGetter<T> {
        String get(T value);
    }
}

class Student {
    private final String studentId;
    private final String name;
    private final String phone;
    private final String className;
    private final String politicsStatus;
    private final String email;

    Student(String studentId, String name, String phone, String className, String politicsStatus, String email) {
        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.className = className;
        this.politicsStatus = politicsStatus;
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getClassName() {
        return className;
    }

    public String getPoliticsStatus() {
        return politicsStatus;
    }

    public String getEmail() {
        return email;
    }
}

class Course {
    private String courseId;
    private String courseName;
    private String credit;
    private String teacher;
    private String category;
    private String schedule;

    Course(String courseId, String courseName, String credit, String teacher, String category, String schedule) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credit = credit;
        this.teacher = teacher;
        this.category = category;
        this.schedule = schedule;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}

class Enrollment {
    private Student student;
    private Course course;
    private String score;
    private String status;

    Enrollment(Student student, Course course, String score, String status) {
        this.student = student;
        this.course = course;
        this.score = score;
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
