package com.example.lab10;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ScreenshotSession {
    private final Stage stage;
    private final TabPane tabPane;
    private final FileAnalyzerPane fileAnalyzerPane;
    private final StudentManagerPane studentManagerPane;
    private final Path outputDir;
    private final List<Step> steps = new ArrayList<>();

    public ScreenshotSession(Stage stage, TabPane tabPane, FileAnalyzerPane fileAnalyzerPane,
                             StudentManagerPane studentManagerPane, Path outputDir) {
        this.stage = stage;
        this.tabPane = tabPane;
        this.fileAnalyzerPane = fileAnalyzerPane;
        this.studentManagerPane = studentManagerPane;
        this.outputDir = outputDir;
    }

    public void run() {
        try {
            Files.createDirectories(outputDir);
        } catch (IOException ex) {
            throw new IllegalStateException("无法创建截图目录", ex);
        }
        studentManagerPane.resetDemoData();
        configureSteps();
        runStep(0);
    }

    private void configureSteps() {
        steps.add(new Step(() -> {
            tabPane.getSelectionModel().select(0);
            fileAnalyzerPane.setPathAndAnalyze(Path.of(System.getProperty("user.dir"), "data"));
        }, "lab10-file-analysis.png"));
        steps.add(new Step(() -> {
            tabPane.getSelectionModel().select(1);
            studentManagerPane.runAddDemo();
        }, "lab10-add-student.png"));
        steps.add(new Step(() -> {
            studentManagerPane.runModifyDemo();
        }, "lab10-modify-student.png"));
        steps.add(new Step(() -> {
            studentManagerPane.runQueryDemo();
        }, "lab10-query-student.png"));
        steps.add(new Step(() -> {
            studentManagerPane.runDisplayDemo();
        }, "lab10-display-students.png"));
        steps.add(new Step(() -> {
            studentManagerPane.runDeleteDemo();
        }, "lab10-delete-student.png"));
    }

    private void runStep(int index) {
        if (index >= steps.size()) {
            Platform.exit();
            return;
        }
        stage.toFront();
        Step step = steps.get(index);
        step.action().run();
        PauseTransition beforeCapture = new PauseTransition(Duration.millis(650));
        beforeCapture.setOnFinished(event -> {
            try {
                capture(step.filename());
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                Platform.exit();
                return;
            }
            PauseTransition afterCapture = new PauseTransition(Duration.millis(300));
            afterCapture.setOnFinished(next -> runStep(index + 1));
            afterCapture.play();
        });
        beforeCapture.play();
    }

    private void capture(String filename) {
        try {
            stage.toFront();
            int x = (int) Math.round(stage.getX());
            int y = (int) Math.round(stage.getY());
            int width = (int) Math.round(stage.getWidth());
            int height = (int) Math.round(stage.getHeight());
            Path target = outputDir.resolve(filename);
            Process process = new ProcessBuilder(
                    "screencapture",
                    "-x",
                    "-R" + x + "," + y + "," + width + "," + height,
                    target.toString()
            ).inheritIO().start();
            int exit = process.waitFor();
            if (exit != 0) {
                throw new IOException("screencapture 退出码：" + exit);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("截图失败：" + filename, ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("截图中断：" + filename, ex);
        }
    }

    private record Step(Runnable action, String filename) {
    }
}
