# LAB8 Java GUI 编程

本目录按照第 08 周 Java GUI 编程要求整理，包含登录框、GuessGame 图形界面、事件处理示例以及 JTable/MVC 学生管理系统。

作业要求参考：第 08 周预习、实验及作业要求中的 Java GUI 编程内容，重点包括 Swing 组件、事件处理、JTable 与 MVC。

## 目录结构

```text
LAB8/
├── 1/   # 登录框界面，练习 JTextField、JPasswordField、按钮事件
├── 2/   # GuessGame：抽象类 + 控制台/图形界面两种输入输出
├── 3/   # Swing 事件处理示例，演示 ActionEvent 与 MouseEvent
├── 5/   # JTable + MVC 学生管理系统，也覆盖 GUI 作业选做要求
└── screenshots/  # 实验报告使用的运行截图
```

## 编译运行

每个题目目录都可以独立编译运行，例如：

```bash
cd LAB8/1
javac -d bin src/*.java
java -cp bin LoginFrameApp
```

第 2 题默认运行图形界面版：

```bash
cd LAB8/2
javac -d bin src/*.java
java -cp bin Main
```

若要运行控制台版：

```bash
java -cp bin Main console
```

第 5 题运行学生管理系统：

```bash
cd LAB8/5
javac -d bin src/*.java
java -cp bin Main
```

## 实验要点

- GUI 组件：使用 `JFrame`、`JPanel`、`JTextField`、`JButton`、`JTable` 等 Swing 组件。
- 布局管理：综合使用 `BorderLayout`、`GridBagLayout`、`GridLayout`、`FlowLayout`。
- 事件处理：通过 `ActionListener`、`MouseAdapter` 和表格选择监听器响应用户操作。
- 抽象类：第 2 题中 `GuessGame` 固定猜数字流程，控制台版和图形版只替换输入输出方式。
- MVC：第 5 题将 `StudentModel`、`StudentView`、`StudentController` 分离，`StudentTableModel` 负责表格数据适配。
