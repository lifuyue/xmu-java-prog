# LAB8 Java GUI 编程

本实验围绕 Swing GUI 编程，包含登录框、猜数字游戏、事件处理示例和 JTable/MVC 学生管理系统。程序运行后会打开窗口，需要在有桌面环境的电脑上执行。

## 实验内容

| 题号 | 程序 | 主要知识点 |
| --- | --- | --- |
| 1 | `LoginFrameApp` | `JFrame`、文本框、密码框、按钮事件 |
| 2 | `Main` / `GuessGame` | 抽象类、控制台版与 GUI 版复用流程 |
| 3 | `EventDemoApp` | `ActionEvent`、`MouseEvent`、监听器 |
| 5 | `Main` / MVC 类组 | `JTable`、MVC、表格数据模型 |

## 目录结构

```text
LAB8/
├── 1/src/LoginFrameApp.java
├── 2/src/Main.java
├── 2/src/GuessGame.java
├── 2/src/ConsoleGuessGame.java
├── 2/src/GUIGame.java
├── 2/src/GuessGamePanel.java
├── 3/src/EventDemoApp.java
├── 5/src/Main.java
├── 5/src/Student.java
├── 5/src/StudentModel.java
├── 5/src/StudentTableModel.java
├── 5/src/StudentView.java
├── 5/src/StudentController.java
├── run.sh
├── ScreenshotExporter.java
├── screenshots/
└── build_report.py
```

## 快速启动

在仓库根目录执行。脚本参数是题号：

```bash
bash LAB8/run.sh 1
bash LAB8/run.sh 2
bash LAB8/run.sh 3
bash LAB8/run.sh 5
```

第 2 题默认打开 GUI 对话框版猜数字游戏。如果想运行控制台版：

```bash
bash LAB8/run.sh 2 console
```

## 手动编译运行

以第 5 题学生管理系统为例：

```bash
javac -d LAB8/5/bin LAB8/5/src/*.java
java -cp LAB8/5/bin Main
```

## 设计说明

- 第 1 题直接构造登录窗口，按钮的 `ActionListener` 负责读取输入框并更新提示文字。
- 第 2 题把猜数字的固定流程放在抽象类 `GuessGame` 中，`ConsoleGuessGame` 和 `GUIGame` 只负责“怎么输入、怎么输出、是否继续”。
- 第 3 题用按钮、下拉框和鼠标进入/离开演示事件源、事件对象和监听器之间的关系。
- 第 5 题采用 MVC：`StudentModel` 保存数据和规则，`StudentView` 负责界面，`StudentController` 连接按钮事件和模型操作，`StudentTableModel` 把学生列表适配给 `JTable`。
- `ScreenshotExporter` 用 Swing 组件离屏绘制生成实验报告截图，不影响各题正常运行。

## 截图与报告

生成截图：

```bash
javac -d LAB8/bin LAB8/1/src/*.java LAB8/2/src/*.java LAB8/3/src/*.java LAB8/5/src/*.java LAB8/ScreenshotExporter.java
java -cp LAB8/bin ScreenshotExporter
```

生成实验报告：

```bash
python3 LAB8/build_report.py
```

## 注意事项

- Swing 程序会打开窗口，不能只在没有图形界面的纯终端环境中运行。
- 如果窗口没有出现在最前面，可以从任务栏或 Dock 中切换出来。
- `bin/` 是编译输出目录，删除后重新运行脚本即可生成。
