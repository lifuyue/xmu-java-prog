import java.security.SecureRandom;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * 随机图形绘制程序。
 */
public class RandomShapeDrawerApp extends Application {
    private static final int MAX_SHAPES = 20;
    private static final double DRAW_WIDTH = 720;
    private static final double DRAW_HEIGHT = 480;

    private final SecureRandom random = new SecureRandom();
    private final Label currentShapeLabel = new Label();
    private final Label rangeHintLabel = new Label();
    private final Label statusLabel = new Label();
    private final Label countLabel = new Label();
    private final Label[] fieldLabels = new Label[4];
    private final TextField[] fieldInputs = new TextField[4];

    private javafx.scene.layout.Pane drawingPane;
    private Button drawButton;
    private ShapeType currentShapeType;
    private int drawnShapes;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.setLeft(createControlPane());
        root.setCenter(createDrawingPane());

        prepareNextShape();

        Scene scene = new Scene(root, 1120, 560);
        primaryStage.setTitle("LAB4-4 随机图形绘制");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createControlPane() {
        Label titleLabel = new Label("随机图形绘制程序");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label introLabel = new Label(
                "系统随机生成 0、1、2：0 对应直线，1 对应矩形，2 对应椭圆。\n"
                        + "请按提示输入参数，范围合法后即可绘制图形。绘制满 20 个后停止。"
        );
        introLabel.setWrapText(true);

        currentShapeLabel.setWrapText(true);
        currentShapeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        rangeHintLabel.setWrapText(true);
        rangeHintLabel.setStyle("-fx-text-fill: #444444;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        for (int i = 0; i < fieldLabels.length; i++) {
            fieldLabels[i] = new Label();
            fieldInputs[i] = new TextField();
            fieldInputs[i].setPrefColumnCount(10);
            formGrid.add(fieldLabels[i], 0, i);
            formGrid.add(fieldInputs[i], 1, i);
        }

        drawButton = new Button("绘制当前图形");
        drawButton.setMaxWidth(Double.MAX_VALUE);
        drawButton.setOnAction(event -> handleDrawShape());

        countLabel.setStyle("-fx-font-weight: bold;");
        statusLabel.setWrapText(true);
        statusLabel.setStyle("-fx-text-fill: #b00020;");

        VBox controlPane = new VBox(
                14,
                titleLabel,
                introLabel,
                currentShapeLabel,
                rangeHintLabel,
                formGrid,
                drawButton,
                countLabel,
                statusLabel
        );
        controlPane.setPadding(new Insets(8, 18, 8, 8));
        controlPane.setPrefWidth(340);
        return controlPane;
    }

    private BorderPane createDrawingPane() {
        drawingPane = new javafx.scene.layout.Pane();
        drawingPane.setPrefSize(DRAW_WIDTH, DRAW_HEIGHT);
        drawingPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        drawingPane.setStyle("-fx-background-color: white; -fx-border-color: #d0d0d0;");

        Label canvasTitle = new Label("绘图区");
        canvasTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox header = new HBox(canvasTitle);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 10, 0));

        BorderPane pane = new BorderPane();
        pane.setTop(header);
        pane.setCenter(drawingPane);
        BorderPane.setMargin(drawingPane, new Insets(0, 0, 0, 8));
        HBox.setHgrow(pane, Priority.ALWAYS);
        return pane;
    }

    private void prepareNextShape() {
        if (drawnShapes >= MAX_SHAPES) {
            currentShapeType = null;
            currentShapeLabel.setText("已达到 20 个图形的上限。");
            rangeHintLabel.setText("程序不再创建新的图形。");
            countLabel.setText("已绘制：20 / 20");
            statusLabel.setText("绘制完成。");
            drawButton.setDisable(true);
            setInputEnabled(false);
            return;
        }

        currentShapeType = ShapeType.fromCode(random.nextInt(3));
        countLabel.setText("已绘制：" + drawnShapes + " / " + MAX_SHAPES);
        currentShapeLabel.setText(
                "当前随机数：" + currentShapeType.code + "，请输入 " + currentShapeType.displayName + " 的初始化参数。"
        );
        rangeHintLabel.setText(currentShapeType.buildRangeHint());
        statusLabel.setText("");
        setInputEnabled(true);
        updateFormForShape(currentShapeType);
    }

    private void updateFormForShape(ShapeType shapeType) {
        List<ParamSpec> specs = shapeType.paramSpecs;
        for (int i = 0; i < fieldLabels.length; i++) {
            boolean visible = i < specs.size();
            fieldLabels[i].setVisible(visible);
            fieldLabels[i].setManaged(visible);
            fieldInputs[i].setVisible(visible);
            fieldInputs[i].setManaged(visible);

            if (visible) {
                ParamSpec spec = specs.get(i);
                fieldLabels[i].setText(spec.name + "：");
                fieldInputs[i].clear();
                fieldInputs[i].setPromptText(spec.min + " - " + spec.max);
            } else {
                fieldLabels[i].setText("");
                fieldInputs[i].clear();
                fieldInputs[i].setPromptText("");
            }
        }
    }

    private void handleDrawShape() {
        if (currentShapeType == null) {
            return;
        }

        try {
            int[] values = readAndValidateInputs(currentShapeType.paramSpecs);
            drawShape(currentShapeType, values);
            drawnShapes++;
            prepareNextShape();
        } catch (IllegalArgumentException exception) {
            statusLabel.setText(exception.getMessage());
        }
    }

    private int[] readAndValidateInputs(List<ParamSpec> specs) {
        int[] values = new int[specs.size()];
        for (int i = 0; i < specs.size(); i++) {
            ParamSpec spec = specs.get(i);
            String text = fieldInputs[i].getText().trim();
            if (text.isEmpty()) {
                throw new IllegalArgumentException(spec.name + " 不能为空，范围是 " + spec.min + " 到 " + spec.max + "。");
            }

            int value;
            try {
                value = Integer.parseInt(text);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException(spec.name + " 必须是整数，范围是 " + spec.min + " 到 " + spec.max + "。");
            }

            if (value < spec.min || value > spec.max) {
                throw new IllegalArgumentException(spec.name + " 超出范围，应在 " + spec.min + " 到 " + spec.max + " 之间。");
            }
            values[i] = value;
        }
        return values;
    }

    private void drawShape(ShapeType shapeType, int[] values) {
        Color strokeColor = Color.hsb(random.nextDouble() * 360, 0.85, 0.85);
        Color fillColor = Color.hsb(random.nextDouble() * 360, 0.45, 0.95, 0.45);

        switch (shapeType) {
            case LINE -> {
                Line line = new Line(values[0], values[1], values[2], values[3]);
                line.setStroke(strokeColor);
                line.setStrokeWidth(3);
                drawingPane.getChildren().add(line);
            }
            case RECTANGLE -> {
                Rectangle rectangle = new Rectangle(values[0], values[1], values[2], values[3]);
                rectangle.setStroke(strokeColor);
                rectangle.setStrokeWidth(2.5);
                rectangle.setFill(fillColor);
                drawingPane.getChildren().add(rectangle);
            }
            case ELLIPSE -> {
                Ellipse ellipse = new Ellipse(values[0], values[1], values[2], values[3]);
                ellipse.setStroke(strokeColor);
                ellipse.setStrokeWidth(2.5);
                ellipse.setFill(fillColor);
                drawingPane.getChildren().add(ellipse);
            }
        }
    }

    private void setInputEnabled(boolean enabled) {
        for (TextField fieldInput : fieldInputs) {
            fieldInput.setDisable(!enabled);
        }
    }

    private record ParamSpec(String name, int min, int max) {
    }

    private enum ShapeType {
        LINE(
                0,
                "直线",
                List.of(
                        new ParamSpec("起点 X", 10, 710),
                        new ParamSpec("起点 Y", 10, 470),
                        new ParamSpec("终点 X", 10, 710),
                        new ParamSpec("终点 Y", 10, 470)
                )
        ),
        RECTANGLE(
                1,
                "矩形",
                List.of(
                        new ParamSpec("左上角 X", 10, 560),
                        new ParamSpec("左上角 Y", 10, 360),
                        new ParamSpec("宽度", 30, 150),
                        new ParamSpec("高度", 30, 110)
                )
        ),
        ELLIPSE(
                2,
                "椭圆",
                List.of(
                        new ParamSpec("中心 X", 120, 600),
                        new ParamSpec("中心 Y", 100, 380),
                        new ParamSpec("横向半径", 20, 100),
                        new ParamSpec("纵向半径", 20, 80)
                )
        );

        private final int code;
        private final String displayName;
        private final List<ParamSpec> paramSpecs;

        ShapeType(int code, String displayName, List<ParamSpec> paramSpecs) {
            this.code = code;
            this.displayName = displayName;
            this.paramSpecs = paramSpecs;
        }

        private static ShapeType fromCode(int code) {
            for (ShapeType shapeType : values()) {
                if (shapeType.code == code) {
                    return shapeType;
                }
            }
            throw new IllegalArgumentException("不支持的图形编号：" + code);
        }

        private String buildRangeHint() {
            StringBuilder builder = new StringBuilder("参数范围：");
            for (int i = 0; i < paramSpecs.size(); i++) {
                ParamSpec spec = paramSpecs.get(i);
                if (i > 0) {
                    builder.append("；");
                }
                builder.append(spec.name)
                        .append(" ∈ [")
                        .append(spec.min)
                        .append(", ")
                        .append(spec.max)
                        .append("]");
            }
            return builder.toString();
        }
    }
}
