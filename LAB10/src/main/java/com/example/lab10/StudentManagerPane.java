package com.example.lab10;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StudentManagerPane extends BorderPane {
    private final StudentRepository repository;
    private final ObservableList<Student> allStudents = FXCollections.observableArrayList();
    private final ObservableList<Student> visibleStudents = FXCollections.observableArrayList();
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField phoneField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField photoField = new TextField();
    private final TextField searchField = new TextField();
    private final TableView<Student> tableView = new TableView<>();
    private final ImageView photoView = new ImageView();
    private final Label indexLabel = new Label("第 0 / 0 条");
    private final Label statusLabel = new Label();
    private Path selectedPhotoSource;
    private String activeStudentId;
    private int currentIndex = -1;

    public StudentManagerPane(StudentRepository repository) {
        this.repository = repository;
        setPadding(new Insets(18));
        getStyleClass().add("page");
        loadFromFile();
        buildUi();
        showAll();
    }

    public void prepareAddDemo() {
        clearForm();
        idField.setText("22920242203267");
        nameField.setText("李富悦");
        phoneField.setText("13900001010");
        emailField.setText("lifuyue@example.com");
        selectedPhotoSource = createDemoPhoto("22920242203267", "李富悦");
        photoField.setText(selectedPhotoSource.toString());
        updatePhotoPreview(selectedPhotoSource.toString());
        statusLabel.setText("已填写新增学生信息，并选择 JPG 照片。");
    }

    public void runAddDemo() {
        prepareAddDemo();
        addStudent();
    }

    public void runModifyDemo() {
        selectById("22920242203267");
        phoneField.setText("13900002020");
        emailField.setText("lifuyue-lab10@example.com");
        modifyStudent();
    }

    public void runQueryDemo() {
        searchField.setText("李");
        queryByName();
    }

    public void runDisplayDemo() {
        showAll();
    }

    public void runDeleteDemo() {
        Student temporary = new Student("20249999", "临时删除", "13899990000",
                "delete-demo@example.com", createDemoPhoto("20249999", "临时").toString());
        allStudents.removeIf(student -> student.id().equals(temporary.id()));
        allStudents.add(temporary);
        allStudents.sort(Comparator.comparing(Student::id));
        saveToFile();
        visibleStudents.setAll(allStudents);
        currentIndex = indexOfVisible(temporary.id());
        displayCurrent();
        deleteStudent();
    }

    public void resetDemoData() {
        allStudents.setAll(List.of(
                new Student("20240001", "张三", "13800000001", "zhangsan@example.com",
                        createDemoPhoto("20240001", "张三").toString()),
                new Student("20240002", "王芳", "13800000002", "wangfang@example.com",
                        createDemoPhoto("20240002", "王芳").toString())
        ));
        saveToFile();
        showAll();
    }

    private void buildUi() {
        Label title = new Label("学生信息顺序文件管理");
        title.getStyleClass().add("title");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.getStyleClass().add("panel");
        form.addRow(0, new Label("学号"), idField);
        form.addRow(1, new Label("姓名"), nameField);
        form.addRow(2, new Label("电话"), phoneField);
        form.addRow(3, new Label("邮箱"), emailField);
        photoField.setEditable(false);
        Button choosePhotoButton = new Button("选择 JPG 照片");
        choosePhotoButton.setOnAction(event -> choosePhoto());
        HBox photoRow = new HBox(8, photoField, choosePhotoButton);
        HBox.setHgrow(photoField, Priority.ALWAYS);
        form.addRow(4, new Label("照片"), photoRow);
        form.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(72));
        form.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(380));

        HBox crudRow = new HBox(8);
        Button addButton = new Button("新增");
        addButton.getStyleClass().add("primary");
        addButton.setOnAction(event -> addStudent());
        Button modifyButton = new Button("修改");
        modifyButton.setOnAction(event -> modifyStudent());
        Button deleteButton = new Button("删除");
        deleteButton.setOnAction(event -> deleteStudent());
        Button clearButton = new Button("清空");
        clearButton.setOnAction(event -> clearForm());
        crudRow.getChildren().addAll(addButton, modifyButton, deleteButton, clearButton);

        searchField.setPromptText("输入姓名查询");
        Button queryButton = new Button("查询");
        queryButton.setOnAction(event -> queryByName());
        Button displayButton = new Button("显示全部");
        displayButton.setOnAction(event -> showAll());
        HBox searchRow = new HBox(8, searchField, queryButton, displayButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button previousButton = new Button("上一条");
        previousButton.setOnAction(event -> previous());
        Button nextButton = new Button("下一条");
        nextButton.setOnAction(event -> next());
        HBox navigationRow = new HBox(8, previousButton, nextButton, indexLabel);
        navigationRow.setAlignment(Pos.CENTER_LEFT);

        photoView.setFitWidth(180);
        photoView.setFitHeight(180);
        photoView.setPreserveRatio(true);
        VBox photoBox = new VBox(10, new Label("当前照片"), photoView);
        photoBox.setAlignment(Pos.TOP_CENTER);
        photoBox.getStyleClass().add("photo-box");

        VBox left = new VBox(12, title, form, crudRow, searchRow, navigationRow, statusLabel);
        left.setPrefWidth(560);
        left.setMinWidth(520);

        configureTable();
        tableView.setItems(visibleStudents);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                currentIndex = visibleStudents.indexOf(newValue);
                displayCurrent();
            }
        });

        VBox right = new VBox(10, tableView, photoBox);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        HBox content = new HBox(18, left, right);
        HBox.setHgrow(right, Priority.ALWAYS);
        setCenter(content);
    }

    private void configureTable() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Student, String> idColumn = new TableColumn<>("学号");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().id()));
        idColumn.setPrefWidth(140);

        TableColumn<Student, String> nameColumn = new TableColumn<>("姓名");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name()));
        nameColumn.setPrefWidth(90);

        TableColumn<Student, String> phoneColumn = new TableColumn<>("电话");
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().phone()));
        phoneColumn.setPrefWidth(130);

        TableColumn<Student, String> emailColumn = new TableColumn<>("邮箱");
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().email()));
        emailColumn.setPrefWidth(190);

        TableColumn<Student, String> photoColumn = new TableColumn<>("照片文件");
        photoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().photoFileName()));
        photoColumn.setPrefWidth(150);

        tableView.getColumns().addAll(idColumn, nameColumn, phoneColumn, emailColumn, photoColumn);
    }

    private void choosePhoto() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("选择学生 JPG 照片");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG 图片", "*.jpg", "*.jpeg", "*.JPG", "*.JPEG"));
        var file = chooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            selectedPhotoSource = file.toPath();
            photoField.setText(selectedPhotoSource.toString());
            updatePhotoPreview(selectedPhotoSource.toString());
        }
    }

    private void addStudent() {
        Optional<Student> candidate = buildStudentFromForm(null);
        if (candidate.isEmpty()) {
            return;
        }
        Student student = candidate.get();
        boolean duplicate = allStudents.stream().anyMatch(item -> item.id().equals(student.id()));
        if (duplicate) {
            statusLabel.setText("新增失败：学号已存在。");
            return;
        }
        allStudents.add(student);
        allStudents.sort(Comparator.comparing(Student::id));
        saveToFile();
        visibleStudents.setAll(allStudents);
        currentIndex = indexOfVisible(student.id());
        displayCurrent();
        statusLabel.setText("新增成功：已按顺序写入 " + repository.dataFile());
    }

    private void modifyStudent() {
        if (activeStudentId == null || activeStudentId.isBlank()) {
            statusLabel.setText("请先查询或显示并选中一条学生信息。");
            return;
        }
        Optional<Student> candidate = buildStudentFromForm(activeStudentId);
        if (candidate.isEmpty()) {
            return;
        }
        Student updated = candidate.get();
        boolean duplicate = allStudents.stream()
                .anyMatch(item -> !item.id().equals(activeStudentId) && item.id().equals(updated.id()));
        if (duplicate) {
            statusLabel.setText("修改失败：新的学号已存在。");
            return;
        }
        for (int i = 0; i < allStudents.size(); i++) {
            if (allStudents.get(i).id().equals(activeStudentId)) {
                allStudents.set(i, updated);
                break;
            }
        }
        allStudents.sort(Comparator.comparing(Student::id));
        saveToFile();
        visibleStudents.setAll(allStudents);
        currentIndex = indexOfVisible(updated.id());
        displayCurrent();
        statusLabel.setText("修改成功：文本文件已重新顺序保存。");
    }

    private void deleteStudent() {
        if (activeStudentId == null || activeStudentId.isBlank()) {
            statusLabel.setText("请先查询或显示并选中一条学生信息。");
            return;
        }
        String removedId = activeStudentId;
        allStudents.removeIf(item -> item.id().equals(removedId));
        visibleStudents.removeIf(item -> item.id().equals(removedId));
        saveToFile();
        if (visibleStudents.isEmpty()) {
            clearForm();
        } else {
            currentIndex = Math.min(currentIndex, visibleStudents.size() - 1);
            displayCurrent();
        }
        statusLabel.setText("删除成功：已从顺序文件中移除学号 " + removedId + "。");
    }

    private void queryByName() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            statusLabel.setText("请输入要查询的姓名关键字。");
            return;
        }
        visibleStudents.setAll(allStudents.stream()
                .filter(student -> student.name().contains(keyword))
                .toList());
        currentIndex = visibleStudents.isEmpty() ? -1 : 0;
        displayCurrent();
        statusLabel.setText("查询完成：姓名包含“" + keyword + "”的记录共 " + visibleStudents.size() + " 条。");
    }

    private void showAll() {
        visibleStudents.setAll(allStudents);
        currentIndex = visibleStudents.isEmpty() ? -1 : 0;
        displayCurrent();
        statusLabel.setText("显示全部：当前文件共有 " + visibleStudents.size() + " 条学生信息。");
    }

    private void previous() {
        if (visibleStudents.isEmpty()) {
            return;
        }
        currentIndex = (currentIndex - 1 + visibleStudents.size()) % visibleStudents.size();
        displayCurrent();
    }

    private void next() {
        if (visibleStudents.isEmpty()) {
            return;
        }
        currentIndex = (currentIndex + 1) % visibleStudents.size();
        displayCurrent();
    }

    private Optional<Student> buildStudentFromForm(String originalId) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            statusLabel.setText("学号、姓名、电话、邮箱均不能为空。");
            return Optional.empty();
        }
        if (!phone.matches("[0-9+\\- ]{6,20}")) {
            statusLabel.setText("电话格式不正确。");
            return Optional.empty();
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            statusLabel.setText("邮箱格式不正确。");
            return Optional.empty();
        }

        String photoPath = photoField.getText().trim();
        if (selectedPhotoSource != null) {
            try {
                photoPath = repository.copyPhoto(selectedPhotoSource, id);
            } catch (IOException ex) {
                statusLabel.setText("照片保存失败：" + ex.getMessage());
                return Optional.empty();
            }
        } else if (photoPath.isBlank() && originalId == null) {
            statusLabel.setText("新增学生时请选择 .JPG 照片。");
            return Optional.empty();
        }
        selectedPhotoSource = null;
        return Optional.of(new Student(id, name, phone, email, photoPath));
    }

    private void displayCurrent() {
        if (currentIndex < 0 || currentIndex >= visibleStudents.size()) {
            activeStudentId = null;
            indexLabel.setText("第 0 / " + visibleStudents.size() + " 条");
            tableView.getSelectionModel().clearSelection();
            return;
        }
        Student student = visibleStudents.get(currentIndex);
        activeStudentId = student.id();
        idField.setText(student.id());
        nameField.setText(student.name());
        phoneField.setText(student.phone());
        emailField.setText(student.email());
        photoField.setText(student.photoPath());
        selectedPhotoSource = null;
        updatePhotoPreview(student.photoPath());
        indexLabel.setText("第 " + (currentIndex + 1) + " / " + visibleStudents.size() + " 条");
        tableView.getSelectionModel().select(student);
        tableView.scrollTo(student);
    }

    private void clearForm() {
        activeStudentId = null;
        selectedPhotoSource = null;
        idField.clear();
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        photoField.clear();
        photoView.setImage(null);
        indexLabel.setText("第 0 / " + visibleStudents.size() + " 条");
        tableView.getSelectionModel().clearSelection();
        statusLabel.setText("已清空输入框。");
    }

    private void updatePhotoPreview(String pathText) {
        Path imagePath = resolvePhotoPath(pathText);
        if (imagePath == null || Files.notExists(imagePath)) {
            photoView.setImage(null);
            return;
        }
        photoView.setImage(new Image(imagePath.toUri().toString(), 180, 180, true, true));
    }

    private void loadFromFile() {
        try {
            allStudents.setAll(repository.load());
        } catch (IOException ex) {
            statusLabel.setText("读取数据文件失败：" + ex.getMessage());
        }
    }

    private void saveToFile() {
        try {
            repository.save(new ArrayList<>(allStudents));
        } catch (IOException ex) {
            statusLabel.setText("保存数据文件失败：" + ex.getMessage());
        }
    }

    private int indexOfVisible(String id) {
        for (int i = 0; i < visibleStudents.size(); i++) {
            if (visibleStudents.get(i).id().equals(id)) {
                return i;
            }
        }
        return visibleStudents.isEmpty() ? -1 : 0;
    }

    private void selectById(String id) {
        visibleStudents.setAll(allStudents);
        currentIndex = indexOfVisible(id);
        displayCurrent();
    }

    private Path createDemoPhoto(String id, String name) {
        try {
            Files.createDirectories(repository.photosDir());
            Path actualPhoto = repository.photosDir().resolve(id + "-demo.jpg");
            Path storedPhoto = Path.of(repository.dataFile().getParent().getFileName().toString(), "photos", actualPhoto.getFileName().toString());
            if (Files.exists(actualPhoto)) {
                return storedPhoto;
            }
            BufferedImage image = new BufferedImage(420, 420, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(238, 242, 247));
            g.fillRect(0, 0, 420, 420);
            g.setColor(new Color(37, 99, 235));
            g.fillOval(120, 70, 180, 180);
            g.setColor(new Color(15, 23, 42));
            g.setStroke(new BasicStroke(12));
            g.drawArc(95, 220, 230, 180, 20, 140);
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("SansSerif", Font.BOLD, 48));
            String label = name.length() >= 2 ? name.substring(name.length() - 2) : name;
            int width = g.getFontMetrics().stringWidth(label);
            g.drawString(label, (420 - width) / 2, 180);
            g.dispose();
            ImageIO.write(image, "jpg", actualPhoto.toFile());
            return storedPhoto;
        } catch (IOException ex) {
            throw new IllegalStateException("创建演示照片失败", ex);
        }
    }

    private Path resolvePhotoPath(String pathText) {
        if (pathText == null || pathText.isBlank()) {
            return null;
        }
        Path path = Path.of(pathText);
        if (path.isAbsolute()) {
            return path;
        }
        return Path.of(System.getProperty("user.dir")).resolve(path).normalize();
    }
}
