package com.example.lab10;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/**
 * 文件路径解析面板：根据输入路径判断文件类型并展示基础属性。
 */
public class FileAnalyzerPane extends BorderPane {
    private final TextField pathField = new TextField();
    private final TextArea resultArea = new TextArea();

    public FileAnalyzerPane() {
        setPadding(new Insets(18));
        getStyleClass().add("page");

        Label title = new Label("文件路径解析");
        title.getStyleClass().add("title");

        pathField.setPromptText("输入文件或文件夹的完整路径");
        HBox.setHgrow(pathField, Priority.ALWAYS);

        Button chooseFileButton = new Button("选择文件");
        chooseFileButton.setOnAction(event -> chooseFile(getScene().getWindow()));

        Button chooseDirButton = new Button("选择文件夹");
        chooseDirButton.setOnAction(event -> chooseDirectory(getScene().getWindow()));

        Button analyzeButton = new Button("解析路径");
        analyzeButton.getStyleClass().add("primary");
        analyzeButton.setOnAction(event -> analyze());

        HBox inputRow = new HBox(10, pathField, chooseFileButton, chooseDirButton, analyzeButton);
        inputRow.setAlignment(Pos.CENTER_LEFT);

        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefRowCount(14);
        resultArea.setText("输入路径后点击“解析路径”。\n\n"
                + "若路径是文件夹，程序统计其直接包含的文件个数和文件夹个数；"
                + "若路径是文件，程序显示文件大小和最后修改日期。");

        VBox content = new VBox(14, title, inputRow, resultArea);
        setCenter(content);
    }

    public void setPathAndAnalyze(Path path) {
        pathField.setText(path.toString());
        analyze();
    }

    private void chooseFile(Window owner) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("选择文件");
        var file = chooser.showOpenDialog(owner);
        if (file != null) {
            pathField.setText(file.toPath().toString());
        }
    }

    private void chooseDirectory(Window owner) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择文件夹");
        var directory = chooser.showDialog(owner);
        if (directory != null) {
            pathField.setText(directory.toPath().toString());
        }
    }

    private void analyze() {
        Path path = Path.of(pathField.getText().trim());
        if (pathField.getText().isBlank()) {
            resultArea.setText("请先输入文件或文件夹路径。");
            return;
        }
        if (Files.notExists(path)) {
            resultArea.setText("路径不存在：\n" + path);
            return;
        }

        try {
            // Files API 先区分目录和普通文件，再分别调用不同的统计逻辑。
            if (Files.isDirectory(path)) {
                analyzeDirectory(path);
            } else if (Files.isRegularFile(path)) {
                analyzeFile(path);
            } else {
                resultArea.setText("该路径既不是普通文件，也不是文件夹：\n" + path);
            }
        } catch (IOException ex) {
            resultArea.setText("解析失败：" + ex.getMessage());
        }
    }

    private void analyzeDirectory(Path path) throws IOException {
        long fileCount;
        long directoryCount;
        try (Stream<Path> children = Files.list(path)) {
            var list = children.toList();
            // 这里只统计直接子项，不递归进入子文件夹，符合实验中的路径解析要求。
            fileCount = list.stream().filter(Files::isRegularFile).count();
            directoryCount = list.stream().filter(Files::isDirectory).count();
        }
        resultArea.setText("""
                路径类型：文件夹
                路径：%s
                直接包含的文件个数：%d
                直接包含的文件夹个数：%d
                """.formatted(path, fileCount, directoryCount));
    }

    private void analyzeFile(Path path) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String modified = formatter.format(attributes.lastModifiedTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        resultArea.setText("""
                路径类型：文件
                路径：%s
                文件大小：%d 字节
                最后修改日期：%s
                """.formatted(path, attributes.size(), modified));
    }
}
