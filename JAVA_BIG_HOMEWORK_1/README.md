# Java 大作业 1：AI 记事本

本目录是 Java 大作业 1 的提交材料，程序使用 Java Swing 实现，功能尽量贴近 Windows 记事本。

## 目录结构

```text
JAVA_BIG_HOMEWORK_1/
├── src/          # 记事本源码
├── screenshots/  # 实验报告截图
├── report/       # Word 实验报告
└── examples/     # 演示保存文件
```

## 编译运行

在本目录下执行：

```bash
javac -encoding UTF-8 -d bin src/*.java
java -cp bin NotepadApp
```

生成实验报告截图：

```bash
java -cp bin ScreenshotExporter
```

## 已实现功能

- 文本输入、删除、修改、剪切、复制、粘贴、全选。
- 新建、打开、保存、另存为、退出。
- 新建、打开、退出前检测未保存内容，并提示保存。
- 查找下一个、替换、全部替换。
- 自动换行开关。
- 状态栏显示当前行列号和缩放比例。
- 字体族、字形、字号选择。
- 放大、缩小、恢复默认缩放。
- 时间/日期插入。
- 关于对话框显示作业和作者信息。

## 视频录制建议

运行视频由学生自行录制。建议录制时按以下顺序演示：

1. 在终端中编译并运行程序。
2. 在记事本正文输入 `22920242203267 李富悦`。
3. 继续输入几行测试文本。
4. 演示保存为 `.txt` 文件。
5. 演示重新打开刚保存的文件。
6. 演示查找和替换。
7. 演示字体设置。
8. 演示放大、缩小、恢复默认缩放，并观察状态栏。
9. 演示自动换行开关。
10. 演示未保存内容时退出会提示保存。

## 代码说明

- `NotepadApp.java`：程序入口。
- `NotepadFrame.java`：主窗口和菜单命令。
- `DocumentModel.java`：当前文档路径和修改状态。
- `FileService.java`：UTF-8 文本文件读写。
- `FindReplaceDialog.java`：查找替换对话框。
- `FontChooserDialog.java`：字体设置对话框。
- `StatusBar.java`：状态栏组件。
- `ScreenshotExporter.java`：生成报告截图的辅助类，不影响主程序功能。
