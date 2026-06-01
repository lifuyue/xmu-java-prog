package com.example.demo;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Painter 绘图面板：根据当前工具、颜色和鼠标拖拽轨迹在 Canvas 上画图。
 */
public class PainterPane extends BorderPane {
    private final Canvas canvas = new Canvas(900, 540);
    private final List<PaintShape> shapes = new ArrayList<>();
    private final ObjectProperty<Color> currentColor = new SimpleObjectProperty<>(Color.web("#2563eb"));
    private Tool currentTool = Tool.RECTANGLE;
    private double startX;
    private double startY;

    public PainterPane() {
        setPadding(new Insets(16));
        setTop(createToolbar());
        setCenter(createCanvasArea());
        drawAll();
        bindMouseEvents();
    }

    private HBox createToolbar() {
        ToggleGroup toolGroup = new ToggleGroup();

        ToggleButton rectangleButton = toolButton("矩形", Tool.RECTANGLE, toolGroup);
        ToggleButton circleButton = toolButton("圆形", Tool.CIRCLE, toolGroup);
        ToggleButton lineButton = toolButton("直线", Tool.LINE, toolGroup);
        rectangleButton.setSelected(true);

        Button colorButton = new Button("修改颜色");
        colorButton.setStyle(colorButtonStyle(currentColor.get()));
        colorButton.setOnAction(event -> chooseColor(colorButton));

        Button undoButton = new Button("撤销");
        undoButton.setOnAction(event -> {
            if (!shapes.isEmpty()) {
                shapes.remove(shapes.size() - 1);
                drawAll();
            }
        });

        Button clearButton = new Button("清空画布");
        clearButton.setOnAction(event -> {
            shapes.clear();
            drawAll();
        });

        Label tipLabel = new Label("在画布拖拽鼠标即可绘制当前图形");
        tipLabel.setStyle("-fx-text-fill: #475569;");

        HBox toolbar = new HBox(10, new Label("图形:"), rectangleButton, circleButton, lineButton,
                colorButton, undoButton, clearButton, tipLabel);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 0, 12, 0));
        return toolbar;
    }

    private ToggleButton toolButton(String title, Tool tool, ToggleGroup group) {
        ToggleButton button = new ToggleButton(title);
        button.setToggleGroup(group);
        button.setOnAction(event -> currentTool = tool);
        return button;
    }

    private StackPane createCanvasArea() {
        StackPane canvasBox = new StackPane(canvas);
        canvasBox.setAlignment(Pos.CENTER);
        canvasBox.setPadding(new Insets(12));
        canvasBox.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 4;");
        return canvasBox;
    }

    private void chooseColor(Button colorButton) {
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("选择画笔颜色");
        dialog.setHeaderText("选择矩形、圆形和直线的描边颜色");

        ColorPicker colorPicker = new ColorPicker(currentColor.get());
        dialog.getDialogPane().setContent(colorPicker);

        ButtonType okButton = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);
        dialog.setResultConverter(buttonType -> buttonType == okButton ? colorPicker.getValue() : null);

        Optional<Color> chosenColor = dialog.showAndWait();
        chosenColor.ifPresent(color -> {
            currentColor.set(color);
            colorButton.setStyle(colorButtonStyle(color));
        });
    }

    private void bindMouseEvents() {
        canvas.setOnMousePressed(event -> {
            // 鼠标按下时只记录起点，真正的图形在拖拽或松开时根据终点计算。
            startX = event.getX();
            startY = event.getY();
        });

        canvas.setOnMouseDragged(event -> {
            drawAll();
            // 拖拽中的图形只做虚线预览，不保存到 shapes 列表。
            drawShape(new PaintShape(currentTool, startX, startY, event.getX(), event.getY(), currentColor.get()), true);
        });

        canvas.setOnMouseReleased(event -> {
            // 松开鼠标后才把图形加入历史列表，撤销功能也依赖这个列表。
            shapes.add(new PaintShape(currentTool, startX, startY, event.getX(), event.getY(), currentColor.get()));
            drawAll();
        });
    }

    private void drawAll() {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setStroke(Color.web("#e2e8f0"));
        graphics.strokeRect(0.5, 0.5, canvas.getWidth() - 1, canvas.getHeight() - 1);
        // Canvas 不会自动记住之前画过的图形，所以每次都从列表完整重画。
        for (PaintShape shape : shapes) {
            drawShape(shape, false);
        }
    }

    private void drawShape(PaintShape shape, boolean preview) {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setStroke(shape.color());
        graphics.setLineWidth(preview ? 2.0 : 2.5);
        graphics.setLineCap(StrokeLineCap.ROUND);
        if (preview) {
            graphics.setLineDashes(8);
        } else {
            graphics.setLineDashes(null);
        }

        switch (shape.tool()) {
            case RECTANGLE -> drawRectangle(graphics, shape);
            case CIRCLE -> drawCircle(graphics, shape);
            case LINE -> graphics.strokeLine(shape.startX(), shape.startY(), shape.endX(), shape.endY());
        }
        graphics.setLineDashes(null);
    }

    private void drawRectangle(GraphicsContext graphics, PaintShape shape) {
        double x = Math.min(shape.startX(), shape.endX());
        double y = Math.min(shape.startY(), shape.endY());
        double width = Math.abs(shape.endX() - shape.startX());
        double height = Math.abs(shape.endY() - shape.startY());
        graphics.strokeRect(x, y, width, height);
    }

    private void drawCircle(GraphicsContext graphics, PaintShape shape) {
        // 用较短边作为直径，保证拖出的图形是圆而不是椭圆。
        double radius = Math.min(Math.abs(shape.endX() - shape.startX()), Math.abs(shape.endY() - shape.startY()));
        double x = shape.endX() < shape.startX() ? shape.startX() - radius : shape.startX();
        double y = shape.endY() < shape.startY() ? shape.startY() - radius : shape.startY();
        graphics.strokeOval(x, y, radius, radius);
    }

    private String colorButtonStyle(Color color) {
        return "-fx-background-color: " + toHex(color) + "; -fx-text-fill: white;";
    }

    private String toHex(Color color) {
        int red = (int) Math.round(color.getRed() * 255);
        int green = (int) Math.round(color.getGreen() * 255);
        int blue = (int) Math.round(color.getBlue() * 255);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    private enum Tool {
        RECTANGLE,
        CIRCLE,
        LINE
    }

    private record PaintShape(Tool tool, double startX, double startY, double endX, double endY, Color color) {
    }
}
