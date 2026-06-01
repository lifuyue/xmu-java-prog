# LAB10 文件处理

本实验是 JavaFX 文件处理程序，包含文件/文件夹路径解析和学生顺序文件管理。学生数据保存在 `data/students.txt`，照片统一保存到 `data/photos/`。

## 实验内容

- 路径解析：输入文件或文件夹路径，显示文件大小、最后修改日期，或统计文件夹直接包含的文件数和文件夹数。
- 学生顺序文件管理：新增、删除、修改、查询和显示学生记录。
- 照片管理：新增或修改学生时选择 `.JPG` 或 `.JPEG` 照片，程序复制到统一目录。
- 浏览记录：查询结果和全部学生列表都可以用“上一条”“下一条”浏览。
- 截图模式：用真实 JavaFX 窗口生成实验报告需要的截图。

## 目录结构

```text
LAB10/
├── pom.xml
├── README.md
├── data/
│   ├── students.txt
│   └── photos/
├── src/main/java/module-info.java
├── src/main/java/com/example/lab10/MainApp.java
├── src/main/java/com/example/lab10/FileAnalyzerPane.java
├── src/main/java/com/example/lab10/Student.java
├── src/main/java/com/example/lab10/StudentRepository.java
├── src/main/java/com/example/lab10/StudentManagerPane.java
├── src/main/java/com/example/lab10/ScreenshotSession.java
├── src/main/resources/style.css
├── screenshots/
└── build_report.py
```

## 快速启动

在仓库根目录执行：

```bash
mvn -f LAB10/pom.xml javafx:run
```

只检查是否能编译：

```bash
mvn -f LAB10/pom.xml -DskipTests compile
```

## 数据文件格式

`data/students.txt` 每行是一条学生记录，字段之间用制表符分隔：

```text
学号    姓名    电话    邮箱    照片路径
```

程序每次新增、修改、删除后都会重新写入整个顺序文件。这样实现简单，适合实验中演示顺序文件的读写流程。

## 设计说明

- `MainApp` 是 JavaFX 入口，创建“路径解析”和“学生顺序文件管理”两个 Tab。
- `FileAnalyzerPane` 负责路径解析。它先判断路径是否存在，再区分文件夹和普通文件。
- `Student` 使用 Java record 表示一条学生记录，字段少且主要用于传递数据。
- `StudentRepository` 专门负责文件读写、照片复制和路径整理，界面层不直接操作文本文件格式。
- `StudentManagerPane` 负责学生管理界面和按钮事件。界面中维护全部学生列表和当前显示列表，查询时只改变当前显示列表。
- `ScreenshotSession` 是截图辅助流程，会切换 Tab、填充演示数据、调用系统截图命令生成报告图片。

## 截图与报告

生成截图：

```bash
mvn -f LAB10/pom.xml javafx:run -Djavafx.args="--screenshots"
```

该命令会打开真实 JavaFX 窗口，并调用 macOS `screencapture` 生成 `screenshots/` 下的实验截图。

生成实验报告：

```bash
python3 LAB10/build_report.py
```

## 注意事项

- 第一次运行 Maven 可能会下载 JavaFX 依赖。
- JavaFX 程序需要桌面环境。
- 新增学生时必须选择 `.JPG` 或 `.JPEG` 照片。
- 如果手动编辑 `students.txt`，请保留制表符分隔格式，否则程序可能跳过格式不完整的行。
