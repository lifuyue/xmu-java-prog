# LAB9 JavaFX 实验

本实验是 JavaFX 综合程序，包含学生信息查询、课程管理、选课管理和 Painter 绘图面板。工程使用 Maven 管理 JavaFX 依赖。

## 实验内容

- 学生信息查询：按学号、电话、班级和政治面貌过滤学生列表。
- 课程管理：新增、修改、删除课程，并刷新关联的选课界面。
- 选课管理：为学生选择课程，修改成绩和状态，统计选课记录数量。
- Painter 绘图：选择矩形、圆形或直线，选择颜色后在画布拖拽绘制。

## 目录结构

```text
LAB9/
├── pom.xml
├── README.md
├── src/main/java/module-info.java
├── src/main/java/com/example/demo/HelloApplication.java
├── src/main/java/com/example/demo/ManagementPane.java
├── src/main/java/com/example/demo/PainterPane.java
├── screenshots/
├── make_screenshots.py
└── build_report.py
```

## 快速启动

在仓库根目录执行：

```bash
mvn -f LAB9/pom.xml javafx:run
```

只检查是否能编译：

```bash
mvn -f LAB9/pom.xml -DskipTests compile
```

## 设计说明

- `HelloApplication` 是 JavaFX 入口，创建两个 Tab：学生选课管理和 Painter 绘图。
- `ManagementPane` 保存三组内存数据：学生、课程、选课记录。界面操作直接修改这些集合，再刷新表格。
- 学生查询使用 `FilteredList`，点击查询或输入课程关键字时改变过滤条件，不需要复制数据。
- 课程删除时同步删除关联选课记录，避免选课记录指向已经不存在的课程。
- 选课管理用 `ComboBox<Student>` 和 `ComboBox<Course>` 选择对象，用 `StringConverter` 控制下拉框显示文字。
- `PainterPane` 使用 `Canvas` 绘图，鼠标按下记录起点，拖拽时画预览，松开后把图形保存到列表并重绘。

## 截图与报告

生成截图：

```bash
python3 LAB9/make_screenshots.py
```

生成实验报告：

```bash
python3 LAB9/build_report.py
```

## 注意事项

- 第一次运行 Maven 可能会下载 JavaFX 依赖，需要等待一段时间。
- JavaFX 程序需要桌面环境。
- 本实验的数据保存在内存中，关闭程序后不会持久化；这是为了突出界面、集合和事件处理。
